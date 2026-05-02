package com.github.lolrobbe2.premakemanagerjet.actions.index

import com.github.lolrobbe2.premakemanagerjet.manager.commands.IndexCommands
import com.github.lolrobbe2.premakemanagerjet.manager.commands.LibraryCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndexNewAction : AnAction(
    "New",
    "Create a new index repository",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val remote = Messages.showInputDialog(
            project,
            "Enter the github link of the remote github repository",
            "Index New",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            IndexCommands.indexNew(remote,project)
        }
    }
}