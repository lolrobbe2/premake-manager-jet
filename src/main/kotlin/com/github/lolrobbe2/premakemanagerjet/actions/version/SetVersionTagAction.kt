package com.github.lolrobbe2.premakemanagerjet.actions.version

import com.github.lolrobbe2.premakemanagerjet.manager.VersionCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetVersionTagAction : AnAction(
    "Set Version",
    "Set the preferred installed premium version",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tag = Messages.showInputDialog(
            project,
            "Enter version tag (leave empty for none):",
            "Set Version",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            VersionCommands.setVersion(tag,project)
        }
    }
}