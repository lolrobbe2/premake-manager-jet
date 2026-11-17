package com.github.lolrobbe2.premakemanagerjet.services

import com.github.lolrobbe2.premakemanagerjet.actions.PremakeCliTerminalRunner
import com.github.lolrobbe2.premakemanagerjet.manager.auth.AuthService
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.ProjectManager
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.reportRawProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.nio.file.Path

@Service(Service.Level.PROJECT)
class PremakeExportService : Disposable {
    private val log = Logger.getInstance(PremakeExportService::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    fun init() {
        scope.launch {
            checkVersion()
        }
    }

    private suspend fun checkVersion() {
        // Check the digest and download when needed.
        val exportAsset = GithubUtils.getLatestExportReleaseAssets().last();
        val stateService = service<PremakeExportStateService>();
        if(exportAsset.digest != stateService.state.installedSha256) {
            val project = ProjectManager.getInstance().openProjects.firstOrNull()
            val destination = Path.of(LocalStorage.getCurrentExportPath() + exportAsset.name)
            if (project == null)
                return
            withBackgroundProgress(project, "Downloading Premake Export Module", cancellable = true) {
                reportRawProgress { reporter ->
                    installAsset(
                        asset = exportAsset,
                        targetPath = destination,
                        onProgress = { current, total ->
                            if (total > 0) {
                                reporter.fraction(current.toDouble() / total.toDouble())
                                reporter.text("Downloading: ${current / 1024} KB")
                            } else {
                                reporter.text("Downloading export module!");
                            }
                        }
                    )
                }
                stateService.state.installedSha256 = exportAsset.digest

            }
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
    override fun dispose() {

    }
}