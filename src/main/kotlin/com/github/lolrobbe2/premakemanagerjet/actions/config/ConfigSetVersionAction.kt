package com.github.lolrobbe2.premakemanagerjet.actions.config

import com.github.lolrobbe2.premakemanagerjet.manager.commands.ConfigCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigSetVersionAction : AnAction(
"Set Version",
"Set the premake version of the premakeConfig.yml",
null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tag = Messages.showInputDialog(
            project,
            "Enter the tag of the version",
            "Set Version",
            Messages.getQuestionIcon()
        )
        CoroutineScope(Dispatchers.Default).launch {
            ConfigCommands.configSetVersion(tag, project)
        }
    }
}