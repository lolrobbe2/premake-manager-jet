package com.github.lolrobbe2.premakemanagerjet.workspace

import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator

class PremakeProjectDirectoryGenerator :
    NewProjectWizardDirectoryGeneratorAdapter<Unit>(
        PremakeNewProjectWizard()
    ),
    CustomStepProjectGenerator<Unit> {

    override fun createStep(
        projectGenerator: DirectoryProjectGenerator<Unit>?,
        callback: AbstractNewProjectStep.AbstractCallback<Unit>?
    ): AbstractActionWithPanel {
        return NewProjectWizardProjectSettingsStep(this)
    }
}