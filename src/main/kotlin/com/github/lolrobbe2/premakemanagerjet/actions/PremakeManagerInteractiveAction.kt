package com.github.lolrobbe2.premakemanagerjet.actions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.github.lolrobbe2.premakemanagerjet.services.PremakeManagerTerminalService

class PremakeManagerInteractiveAction :  AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val terminalService = project.getService(PremakeManagerTerminalService::class.java)
        terminalService.runPremakeInteractive()
    }
}