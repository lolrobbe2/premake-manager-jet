package com.github.lolrobbe2.premakemanagerjet.actions.remotes

import com.github.lolrobbe2.premakemanagerjet.manager.commands.RemotesCommands
import com.github.lolrobbe2.premakemanagerjet.manager.commands.VersionCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemotesViewAction : AnAction(
    "View",
    "View all the local system remotes",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        CoroutineScope(Dispatchers.Default).launch {
            RemotesCommands.RemotesView(project)
        }
    }
}