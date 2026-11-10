// Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package com.github.lolrobbe2.premakemanagerjet.manager.auth

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.BrowserUtil
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.Urls
import com.intellij.util.io.HttpRequests.post
import io.netty.handler.codec.http.QueryStringDecoder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.ide.BuiltInServerManager
import org.kohsuke.github.GitHubBuilder
import java.net.URI
import java.net.http.HttpResponse
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.nio.charset.StandardCharsets
import java.time.Duration

internal const val SERVICE_NAME = "myplugin"
private const val DEVICE_CODE_URL =
    "https://github.com/login/device/code"

private const val ACCESS_TOKEN_URL =
    "https://github.com/login/oauth/access_token"

private const val VERIFICATION_URL =
    "https://github.com/login/device"
private const val CLIENT_ID = "Ov23li9K0srasU3S1QOZ"
private const val SCOPES = "read:user user:email"

sealed interface AuthState {
    data object Disconnected : AuthState

    data class AwaitingAuthorization(
        val userCode: String,
        val verificationUri: String
    ) : AuthState

    data class Connected(
        val username: String? = null
    ) : AuthState
}
@Serializable
data class DeviceCodeResponse(
    @SerialName("device_code")
    val deviceCode: String,

    @SerialName("expires_in")
    val expiresIn: Int,

    val interval: Int,

    @SerialName("user_code")
    val userCode: String,

    @SerialName("verification_uri")
    val verificationUri: String
)
private val httpClient: HttpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build()
private val json = Json {
    ignoreUnknownKeys = true
}
@Service(Service.Level.APP)
class AuthService(val coroutineScope: CoroutineScope) {

    internal val callbacks = ConcurrentHashMap<String, CompletableDeferred<String>>()
    private val credentials = CredentialAttributes(generateServiceName("MyPluginAuth", "OAuthToken"))
    private val _state = MutableStateFlow<AuthState>(AuthState.Disconnected)

    val state = _state.asStateFlow()

    @Volatile private var loginJob: Job? = null

    init {
        coroutineScope.launch {
            val token = PasswordSafe.instance.getPassword(credentials) ?: return@launch
            _state.value = AuthState.Connected(fetchUserProfile(token))
        }
    }

    fun login() {
        if (_state.value is AuthState.Connected) return
        loginJob = coroutineScope.launch {
            try {
               val token = requestToken();
                storeToken(token)
                _state.value = AuthState.Connected(fetchUserProfile(token))
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                storeToken(null)
                _state.value = AuthState.Disconnected
                thisLogger().warn("OAuth login failed", t)
            } finally {
                loginJob = null
            }
        }
    }

    fun logout() = coroutineScope.launch {
        loginJob?.cancel()
        loginJob = null
        storeToken(null)
        _state.value = AuthState.Disconnected
    }

    private fun storeToken(token: String?) = PasswordSafe.instance.setPassword(credentials, token)
    private suspend fun requestToken(): String {
        val deviceCode = requestDeviceCode()

        _state.value = AuthState.AwaitingAuthorization(
            userCode = deviceCode.userCode,
            verificationUri = deviceCode.verificationUri
        )

        BrowserUtil.browse(deviceCode.verificationUri)

        return exchangeCodeForToken(deviceCode.deviceCode,"")
    }
    suspend fun requestDeviceCode(): DeviceCodeResponse =
        withContext(Dispatchers.IO) {
            val body = "client_id=${
                URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8)
            }"

            val request = HttpRequest.newBuilder()
                .uri(URI.create(DEVICE_CODE_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .header(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
                )
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()

            val response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            )

            if (response.statusCode() !in 200..299) {
                throw kotlinx.io.IOException(
                    "GitHub device code request failed: ${response.body()}"
                )
            }

            json.decodeFromString<DeviceCodeResponse>(response.body())
        }


    private fun authorizationUrl() = url(
        DEVICE_CODE_URL,
        "client_id" to CLIENT_ID,
        "scope" to SCOPES,
    )



    private fun tokenUrl(code: String, grantType: String) = url(
        ACCESS_TOKEN_URL,
        "client_id" to CLIENT_ID,
        "device_code" to code,
        "grant_type" to grantType,
    )
    private suspend fun exchangeCodeForToken(code: String, grantType: String) = withContext(Dispatchers.IO) {
        parseAccessToken(post(tokenUrl(code, grantType), null).readString())
    }
    private suspend fun fetchUserProfile(token: String): String? = withContext(Dispatchers.IO) {
        runCatching { GitHubBuilder().withOAuthToken(token).build().myself.login }
            .onFailure { thisLogger().warn("Failed to fetch user profile", it) }
            .getOrNull()
    }

    private fun url(base: String, vararg parameters: Pair<String, String>) =
        Urls.newFromEncoded(base).addParameters(mapOf(*parameters)).toExternalForm()

    private fun parseAccessToken(body: String) =
        QueryStringDecoder("/?$body").parameters()["access_token"]?.firstOrNull()
            ?: error("Failed to exchange code for token")
}