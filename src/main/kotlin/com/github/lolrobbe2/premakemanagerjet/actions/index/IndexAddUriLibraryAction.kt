package com.github.lolrobbe2.premakemanagerjet.actions.index

import com.github.lolrobbe2.premakemanagerjet.manager.commands.IndexCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndexAddUriLibraryAction: AnAction(
    "Add Uri Library",
    "Add a library to the index",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val remote = Messages.showInputDialog(
            project,
            "Enter the github link of the remote github library",
            "Index Add Uri Library",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            IndexCommands.indexAddUriLibrary(remote,project)
        }
    }
}