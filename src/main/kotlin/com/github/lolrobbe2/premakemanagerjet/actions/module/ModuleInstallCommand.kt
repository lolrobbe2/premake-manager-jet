package com.github.lolrobbe2.premakemanagerjet.actions.module

import com.github.lolrobbe2.premakemanagerjet.manager.commands.ModuleCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleInstallCommand : AnAction(
    "Install Module",
    "Install/download a module",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tag = Messages.showInputDialog(
            project,
            "Enter the github link of the module:",
            "Install Module",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            ModuleCommands.ModuleInstall(tag,project)
        }
    }
}