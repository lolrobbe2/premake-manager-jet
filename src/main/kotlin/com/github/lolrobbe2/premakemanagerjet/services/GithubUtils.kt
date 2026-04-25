package com.github.lolrobbe2.premakemanagerjet.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import jdk.internal.net.http.common.Log.headers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Release(
    val assets: List<Asset>
)

@Serializable
data class Asset(
    val name: String,

    @SerialName("browser_download_url")
    val downloadUrl: String,

    val digest: String? // already matches JSON name
)

object GithubUtils {

    private val client = HttpClient()

    suspend fun getLatestReleaseAssets(): List<Asset> {
        val response: String = client.get(
            "https://api.github.com/repos/lolrobbe2/premake-manager-cli/releases/latest"
        ) {
            headers {
                append(HttpHeaders.Accept, "application/vnd.github+json")
                append(HttpHeaders.UserAgent, "ktor-client")
            }
        }.body()

        val release = Json { ignoreUnknownKeys = true }.decodeFromString<Release>(response)
        return release.assets
    }
}