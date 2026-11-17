package com.github.lolrobbe2.premakemanagerjet.workspace

import com.github.lolrobbe2.premakemanagerjet.Icons
import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.GeneratorNewProjectWizard
import com.intellij.ide.wizard.NewProjectWizardBaseStep
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectGenerator
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class PremakeNewProjectWizard : GeneratorNewProjectWizard {
    override val id = "premake5";
    override val name = "premake5";
    override val icon = Icons.PremakeManager
    override fun createStep(context: WizardContext): NewProjectWizardStep {
       return RootNewProjectWizardStep(context)
            .nextStep(::NewProjectWizardBaseStep)
    }
}