package com.github.lolrobbe2.premakemanagerjet.workspace

import com.github.lolrobbe2.premakemanagerjet.Icons
import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectGenerator
import org.jetbrains.annotations.Nls
import javax.swing.Icon

class PremakeNewProjectWizard : LanguageGeneratorNewProjectWizard {
    override val name: @Nls(capitalization = Nls.Capitalization.Title) String
        get() = "Premake"
    override val icon: Icon
        get() = Icons.PremakeManager

    override fun createStep(parent: NewProjectWizardStep): NewProjectWizardStep {
        TODO("Not yet implemented")
    }
}