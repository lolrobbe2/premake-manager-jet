package com.github.lolrobbe2.premakemanagerjet.actions.config

import com.github.lolrobbe2.premakemanagerjet.manager.commands.ConfigCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigViewAction : AnAction(
    "View",
    "View the premakeConfig.yml",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        CoroutineScope(Dispatchers.Default).launch {
            ConfigCommands.configView(project)
        }
    }
}