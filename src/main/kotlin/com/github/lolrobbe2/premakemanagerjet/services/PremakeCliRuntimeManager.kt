package com.github.lolrobbe2.premakemanagerjet.services

import com.github.lolrobbe2.premakemanagerjet.actions.PremakeTerminalRunner
import com.github.lolrobbe2.premakemanagerjet.manager.GitHubAuthService
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.ComponentManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.reportRawProgress
import java.nio.file.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions
import org.jetbrains.plugins.terminal.TerminalTabState
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

@Service(Service.Level.PROJECT)
class PremakeCliRuntimeManager(var project: Project) : Disposable {
    private val log = Logger.getInstance(PremakeCliRuntimeManager::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var terminalRunner: PremakeTerminalRunner? = null

    fun init() {
        scope.launch {
            checkVersion()
        }
    }

    suspend fun checkVersion() {
        val platformAsset = GithubUtils.getLatestReleaseAsset()
        val stateService = service<PremakeCliStateService>() // Use service retrieval
        val test: String = PathManager.getBinPath()
        stateService.state.installedSha256 = "";
        log.info(test)
        // Use your logic to determine if update is needed
        if (platformAsset.digest != stateService.state.installedSha256) {

            // Get the first active project to show the progress card, or null for global
            val project = ProjectManager.getInstance().openProjects.firstOrNull()
            val destination = Path.of(LocalStorage.getCurrentCLIDir() + platformAsset.name)
            if (project == null)
                return

            withBackgroundProgress(project, "Downloading Premake CLI...", cancellable = true) {
                reportRawProgress { reporter ->
                    installAsset(
                        asset = platformAsset,
                        targetPath = destination,
                        onProgress = { current, total ->
                            if (total > 0) {
                                reporter.fraction(current.toDouble() / total.toDouble())
                                reporter.text("Downloading: ${current / 1024} KB")
                            } else {
                                reporter.text("Downloading cli!");
                            }
                        }
                    )
                }
                stateService.state.installedSha256 = platformAsset.digest

            }

            // Update state after successful download
        }
    }


    fun installAsset(asset: Asset, targetPath: Path, onProgress: ((Long, Long) -> Unit)? = null) {


        log.info("Downloading Premake CLI from: ${asset.downloadUrl}")

        LocalStorage.downloadToPath(
            sourceUrl = asset.downloadUrl,
            targetPath = targetPath,
            onProgress = onProgress
        )
    }


    suspend fun createManagerTerminalTab(terminalManager: TerminalToolWindowManager, command: String = "") {
        if(terminalRunner == null) {
            val token = GitHubAuthService.getToken(project)
            terminalRunner = PremakeTerminalRunner(project = project, token!!)
        }

        val tabState = TerminalTabState().apply {
            myTabName = "Premake Manager"
            myWorkingDirectory = project.basePath
            myShellCommand = listOf(
                LocalStorage.getCurrentCLIPath(),
                "--interactive",command
            )
        }

        withContext(Dispatchers.EDT) {
            terminalManager.createNewSession(terminalRunner!!, tabState)
        }
    }

    override fun dispose() {
        scope.cancel()
    }
}