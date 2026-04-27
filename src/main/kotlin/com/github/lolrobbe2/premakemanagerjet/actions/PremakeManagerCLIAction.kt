package com.github.lolrobbe2.premakemanagerjet.actions

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.github.lolrobbe2.premakemanagerjet.services.PremakeCliRuntimeManager
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.IconLoader
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PremakeManagerCLIAction : AnAction(
    "Run Premake Manager",
    "Run Premake Manager CLI",
    IconLoader.getIcon("/icons/premake_manager.png", PremakeManagerCLIAction::class.java)
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        CoroutineScope(Dispatchers.Default).launch {
            CommandExecutor.executeCommand("", project)
        }
    }
}