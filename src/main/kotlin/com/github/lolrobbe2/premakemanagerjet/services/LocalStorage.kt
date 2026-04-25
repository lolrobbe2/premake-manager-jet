package com.github.lolrobbe2.premakemanagerjet.services

import com.intellij.openapi.diagnostic.Logger

import com.intellij.openapi.application.PathManager
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object LocalStorage {

    private val log = Logger.getInstance(LocalStorage::class.java)

    fun getSystemPath(): String {
        return PathManager.getSystemPath();
    }

    public fun getPluginBinPath(): String {
        return PathManager.getBinPath() + "/plugins/premakemanagerjet";
    }

    public fun getCurrentCLIDir(): String {
        return getPluginBinPath() + "/" + PremakeCliStateService().getVersionType() + "/";
    }

    public fun downloadToPath(
        sourceUrl: String,
        targetPath: Path,
        onProgress: ((downloaded: Long, total: Long) -> Unit)? = null
    ) {
        // Change your builder to this:
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL) // This fixes the 302!
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(sourceUrl))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofInputStream())

        if (response.statusCode() !in 200..299) {
            log.error("Download failed: HTTP ${response.statusCode()}")
            return
        }

        val total = response.headers()
            .firstValueAsLong("content-length")
            .orElse(0L)

        Files.createDirectories(targetPath.parent)

        response.body().use { input ->
            Files.newOutputStream(
                targetPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use { output ->

                val buffer = ByteArray(8 * 1024)
                var downloaded = 0L

                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break

                    output.write(buffer, 0, read)
                    downloaded += read

                    onProgress?.invoke(downloaded, total)
                }
            }
        }

        try {
            targetPath.toFile().setExecutable(true, false)
        } catch (e: Exception) {
            log.warn("Failed to set executable bit", e)
        }
    }
}