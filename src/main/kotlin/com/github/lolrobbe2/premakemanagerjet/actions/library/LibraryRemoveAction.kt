package com.github.lolrobbe2.premakemanagerjet.actions.library

import com.github.lolrobbe2.premakemanagerjet.manager.commands.LibraryCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryRemoveAction : AnAction(
    "Remove Library",
    "Remove a library from the premakeConfig.yml",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tag = Messages.showInputDialog(
            project,
            "Enter the github link of the library:",
            "Library Remove",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            LibraryCommands.LibraryRemove(tag,project)
        }
    }
}