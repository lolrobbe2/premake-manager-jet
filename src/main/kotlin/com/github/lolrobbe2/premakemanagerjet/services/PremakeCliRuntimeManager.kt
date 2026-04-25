package com.github.lolrobbe2.premakemanagerjet.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Path

@Service(Service.Level.APP)
class PremakeCliRuntimeManager {
    private val log = Logger.getInstance(PremakeCliRuntimeManager::class.java)

    fun checkVersion() {
        installLatestVersion(
            targetPath = Path.of( LocalStorage.getCurrentCLIDir()),

        )
    }

    fun installLatestVersion(targetPath: Path, onProgress: ((Long, Long) -> Unit)? = null) {
        val url = resolveLatestDownloadUrl()

        log.info("Downloading Premake CLI from: $url")

        LocalStorage.downloadToPath(
            sourceUrl = url,
            targetPath = targetPath,
            onProgress = onProgress
        )
    }


    private fun resolveLatestDownloadUrl(): String {
        val base = "https://github.com/lolrobbe2/premake-manager-cli/releases/latest/download"

        return when {
            SystemInfo.isMac -> "$base/premake-manager-cli-darwin"
            SystemInfo.isLinux -> "$base/premake-manager-cli-linux"
            SystemInfo.isWindows -> "$base/premake-manager-cli-win.exe"
            else -> error("Unsupported OS for Premake CLI")
        }
    }
}