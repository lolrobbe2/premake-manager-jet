package com.github.lolrobbe2.premakemanagerjet.services

import com.github.lolrobbe2.premakemanagerjet.actions.PremakeCliTerminalRunner
import com.github.lolrobbe2.premakemanagerjet.actions.PremakeTerminalRunner
import com.github.lolrobbe2.premakemanagerjet.manager.GitHubAuthService
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.terminal.ui.TerminalWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.terminal.TerminalTabState
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

@Service(Service.Level.PROJECT)
class PremakeRuntimeManager(var project: Project) {
    private val log = Logger.getInstance(PremakeRuntimeManager::class.java)
    private var terminalRunner: PremakeTerminalRunner? = null

    suspend fun createManagerTerminalTab(terminalManager: TerminalToolWindowManager, command: String) {
        if(terminalRunner == null) {
            terminalRunner = PremakeTerminalRunner(project = project)
        }

        val tabState = TerminalTabState().apply {
            myTabName = "Premake5"
        }

        withContext(Dispatchers.EDT) {
            var widget:  TerminalWidget? = terminalManager.terminalWidgets
                .firstOrNull { it.terminalTitle.defaultTitle == "Premake5" }
            if (widget == null) {
                terminalManager.createNewSession(terminalRunner!!,tabState)
                widget = terminalManager.terminalWidgets
                    .firstOrNull { it.terminalTitle.defaultTitle == "Premake5" }
            }
            if (widget != null && command.isNotBlank()) {
                widget.sendCommandToExecute(command)
            }
        }
    }
}