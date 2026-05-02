package com.github.lolrobbe2.premakemanagerjet.actions.version

import com.github.lolrobbe2.premakemanagerjet.manager.commands.VersionCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListReleasesAction : AnAction(
    "List Realeases",
    "List all available Realeases for premake5",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        CoroutineScope(Dispatchers.Default).launch {
            VersionCommands.listReleases(project)
        }
    }
}