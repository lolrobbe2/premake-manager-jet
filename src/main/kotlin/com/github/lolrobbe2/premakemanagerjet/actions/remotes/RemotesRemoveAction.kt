package com.github.lolrobbe2.premakemanagerjet.actions.remotes

import com.github.lolrobbe2.premakemanagerjet.manager.commands.RemotesCommands
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class OwnerRepoDialog(project: Project) : DialogWrapper(project) {
    private val ownerField = JBTextField()
    private val repoField = JBTextField()

    init {
        title = "Remove Remote"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridLayout(2, 2, 8, 8))

        panel.add(JLabel("Owner:"))
        panel.add(ownerField)

        panel.add(JLabel("Repository:"))
        panel.add(repoField)

        return panel
    }

    fun getRepoOwner() = ownerField.text.trim()
    fun getRepo() = repoField.text.trim()
}
class RemotesRemoveAction : AnAction(
    "Remove",
    "Remove a remote given it's owner and repo",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        val dialog = OwnerRepoDialog(project)
        if (!dialog.showAndGet()) return
        val owner = dialog.getRepoOwner()
        val repo = dialog.getRepo()

        CoroutineScope(Dispatchers.Default).launch {
            RemotesCommands.RemotesRemove(owner,repo,project)
        }
    }
}