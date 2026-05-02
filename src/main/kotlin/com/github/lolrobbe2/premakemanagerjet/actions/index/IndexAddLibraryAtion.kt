package com.github.lolrobbe2.premakemanagerjet.actions.index

import com.github.lolrobbe2.premakemanagerjet.manager.commands.IndexCommands
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

class RepoDialog(project: Project) : DialogWrapper(project) {
    private val repoField = JBTextField()
    private val descriptionField = JBTextField()
    private val remoteField = JBTextField()

    init {
        init()
        title = "Index New"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridLayout(2, 2))
        panel.add(JLabel("Remote library: "))
        panel.add(remoteField)
        panel.add(JLabel("Repo: "))
        panel.add(repoField)
        panel.add(JLabel("Repository URL:"))
        panel.add(repoField)
        panel.add(JLabel("Description:"))
        panel.add(descriptionField)
        return panel
    }

    fun getRemote(): String? = repoField.text;
    fun getRepo(): String? = repoField.text
    fun getDescription(): String? = descriptionField.text
}

class IndexAddLibraryAtion : AnAction(
    "Add Library",
    "Create a new index repository",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val dialog = RepoDialog(project)
        if (dialog.showAndGet()) {
            val repo = dialog.getRepo()
            val description = dialog.getDescription()
            val remote = dialog.getRemote()
            CoroutineScope(Dispatchers.Default).launch {
                IndexCommands.indexAddLibrary(remote, repo, description, project)
            }
        }
    }
}