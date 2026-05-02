package com.github.lolrobbe2.premakemanagerjet.actions

import com.github.lolrobbe2.premakemanagerjet.services.PremakeCliRuntimeManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.platform.lang.lsWidget.OpenSettingsAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class TerminalActionInstaller: ProjectActivity {
    override suspend fun execute(project: Project) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val terminalToolWindow = toolWindowManager.getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)

        terminalToolWindow?.let { toolWindow ->
            withContext(Dispatchers.EDT) {
                toolWindow.setTitleActions(listOf(PremakeManagerCLIAction(),PremakeCLIAction()))
            }
        }
    }
}