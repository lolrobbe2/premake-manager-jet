
package com.github.lolrobbe2.premakemanagerjet.workspace

import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.GeneratorNewProjectWizard
import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStepPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectGeneratorBase
import com.intellij.platform.GeneratorPeerImpl
import com.intellij.platform.ProjectGeneratorPeer
import java.nio.file.Path
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

private val Log = logger<NewProjectWizardDirectoryGeneratorAdapter<*>>()

open class NewProjectWizardDirectoryGeneratorAdapter<T : Any>(
    val wizard: GeneratorNewProjectWizard
) : DirectoryProjectGeneratorBase<T>() {

    internal lateinit var panel: NewProjectWizardStepPanel

    internal lateinit var wizardDisposable: Disposable

    override fun getName(): String = wizard.name

    override fun getLogo(): Icon = wizard.icon

    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: T,
        module: Module
    ) {
        try {
            panel.step.setupProject(project)
        } catch (error: ProcessCanceledException) {
            throw error
        } catch (error: Exception) {
            Messages.showErrorDialog(
                project,
                error.message ?: "The project generator failed",
                "Premake Project Generation Failed"
            )
            Log.warn("Premake project generation failed", error)
        }
    }

    override fun createPeer(): ProjectGeneratorPeer<T> {
        wizardDisposable = Disposer.newDisposable("Premake wizard context")

        val context = WizardContext(null, wizardDisposable)

        return object : GeneratorPeerImpl<T>() {

            override fun getComponent(
                myLocationField: TextFieldWithBrowseButton,
                checkValid: Runnable
            ): JComponent {
                panel = NewProjectWizardStepPanel(
                    wizard.createStep(context)
                )

                return panel.component
            }
        }
    }
}

open class NewProjectWizardProjectSettingsStep<T : Any>(
    private val projectGenerator: NewProjectWizardDirectoryGeneratorAdapter<T>
) : ProjectSettingsStepBase<T>(projectGenerator, null) {

    init {
        myCallback = AbstractNewProjectStep.AbstractCallback()
    }

    override fun createAndFillContentPanel(): JPanel {
        val content = JPanel(VerticalFlowLayout()).apply {
            add(peer.getComponent(TextFieldWithBrowseButton()) {})
        }

        Disposer.register(this, projectGenerator.wizardDisposable)

        return content
    }

    override fun registerValidators() {
    }

    override fun getProjectLocation(): String {
        val base = projectGenerator.panel.step.baseData
            ?: return super.getProjectLocation()

        return Path.of(base.path).resolve(base.name).toString()
    }

    override fun getActionButton(): JButton {
        return super.getActionButton().apply {
            addActionListener {
                projectGenerator.panel.apply()
            }
        }
    }
}
