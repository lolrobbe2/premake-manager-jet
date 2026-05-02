package com.github.lolrobbe2.premakemanagerjet.manager

import com.github.lolrobbe2.premakemanagerjet.services.PremakeCliRuntimeManager
import com.github.lolrobbe2.premakemanagerjet.services.PremakeRuntimeManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

object CommandExecutor {
    suspend fun executeCommand(command: String, project: Project) {

            project.service<PremakeCliRuntimeManager>().createManagerTerminalTab(
                terminalManager = TerminalToolWindowManager.getInstance(project),
                command = command
            )

    }
    suspend fun executePremakeCommand(command: String, project: Project) {
        project.service<PremakeRuntimeManager>().createManagerTerminalTab(terminalManager = TerminalToolWindowManager.getInstance(project), command = command)
    }
}