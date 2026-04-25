package com.github.lolrobbe2.premakemanagerjet.services

import com.github.lolrobbe2.premakemanagerjet.services.GithubUtils.getLatestReleaseAssets
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.reportProgress
import com.intellij.platform.util.progress.reportRawProgress
import java.nio.file.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Service(Service.Level.APP)
class PremakeCliRuntimeManager(private val scope: CoroutineScope) {
    private val log = Logger.getInstance(PremakeCliRuntimeManager::class.java)

    init {
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
            val destination = Path.of(LocalStorage.getCurrentCLIDir() +  platformAsset.name)
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
}