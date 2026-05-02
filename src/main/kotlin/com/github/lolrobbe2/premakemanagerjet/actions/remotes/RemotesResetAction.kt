package com.github.lolrobbe2.premakemanagerjet.actions.remotes

import com.github.lolrobbe2.premakemanagerjet.manager.commands.RemotesCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemotesResetAction : AnAction(
    "Reset",
    "Reset the local remotes to default",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return

        val confirm = Messages.showYesNoDialog(
            project,
            "This will reset all local remotes to their default state.\n\nDo you want to continue?",
            "Confirm Reset",
            Messages.getWarningIcon()
        )

        if (confirm != Messages.YES) return
        CoroutineScope(Dispatchers.Default).launch {
            RemotesCommands.RemotesReset(project)
        }
    }
}