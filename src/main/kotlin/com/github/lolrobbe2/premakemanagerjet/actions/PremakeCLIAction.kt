package com.github.lolrobbe2.premakemanagerjet.actions

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.IconLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PremakeCLIAction : AnAction(
    "Run Premake5",
    "Run Premake5",
    IconLoader.getIcon("/icons/premake_manager.png", PremakeManagerCLIAction::class.java)
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        CoroutineScope(Dispatchers.Default).launch {
            CommandExecutor.executePremakeCommand("", project)
        }
    }
}