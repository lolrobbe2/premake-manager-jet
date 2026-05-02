package com.github.lolrobbe2.premakemanagerjet.actions.remotes

import com.github.lolrobbe2.premakemanagerjet.manager.commands.RemotesCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemotesUpdateAction: AnAction(
    "Update",
    "Update all the local remotes",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        val result = Messages.showOkCancelDialog(
            project,
            "Do you want to force update all remotes?",
            "Force Update",
            "Yes",
            "No",
            Messages.getQuestionIcon()
        )

        val force = result == Messages.OK;
        CoroutineScope(Dispatchers.Default).launch {
            RemotesCommands.RemotesUpdate(force,project)
        }
    }
}