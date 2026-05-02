package com.github.lolrobbe2.premakemanagerjet.manager

import com.intellij.openapi.project.Project
import com.intellij.collaboration.auth.services.OAuthService
import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.plugins.github.authentication.AuthorizationType
import org.jetbrains.plugins.github.authentication.GHAccountAuthData
import org.jetbrains.plugins.github.authentication.GHAccountsUtil
import org.jetbrains.plugins.github.authentication.GHLoginSource
import java.util.concurrent.CompletableFuture

object GitHubAuthService {
    private val ATTRS =
        CredentialAttributes("premake.manager.github.token")

    fun getToken(project: Project): String? {

        // 1. Try cached token first
        val cached = PasswordSafe.instance[ATTRS]?.getPasswordAsString()
        if (!cached.isNullOrEmpty()) {
            return cached
        }

        // 2. Otherwise login (blocking)
        val future = java.util.concurrent.CompletableFuture<String?>()

        ApplicationManager.getApplication().invokeLater {

            val result: GHAccountAuthData? = GHAccountsUtil.requestNewAccount(
                null,
                null,
                project,
                null,
                AuthorizationType.UNDEFINED,
                GHLoginSource.GIT
            )

            val token: String? = result?.token

            if (!token.isNullOrEmpty()) {
                // 3. Store in PasswordSafe
                PasswordSafe.instance.set(
                    ATTRS,
                    Credentials("github", token)
                )
            }

            future.complete(token)
        }

        return future.get()
    }
}