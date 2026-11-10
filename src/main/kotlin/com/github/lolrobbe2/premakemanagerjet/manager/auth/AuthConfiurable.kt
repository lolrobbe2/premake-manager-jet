package com.github.lolrobbe2.premakemanagerjet.manager.auth

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.components.panels.Wrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection

class AuthConfigurable : BoundConfigurable("Premake Manager Authentication") {

    private val authService by lazy { service<AuthService>() }

    override fun createPanel() = panel {
        val content = Wrapper()

        row {
            cell(content).align(AlignX.FILL)
        }

        authService.coroutineScope.launch {
            authService.state.collect {
                content.setContent(createView(it))
            }
        }
    }

    private fun createView(state: AuthState) = panel {
        when (state) {
            is AuthState.Disconnected -> {
                row {
                    button("Login with GitHub") {
                        authService.login()
                    }
                }
            }

            is AuthState.AwaitingAuthorization -> {
                row("Authorization required") {
                    label("Enter this code on GitHub:")
                }

                row("Device code") {
                    label(state.userCode)

                    button("Copy") {
                        CopyPasteManager.getInstance().setContents(
                            StringSelection(state.userCode)
                        )
                    }
                }

                row {
                    button("Open GitHub") {
                        BrowserUtil.browse(state.verificationUri)
                    }
                }

                row {
                    label("Waiting for authorization...")
                }
            }

            is AuthState.Connected -> {
                row("Username") {
                    label(state.username ?: "Unknown")

                    button("Logout") {
                        authService.logout()
                    }
                }
            }
        }
    }
}