package com.github.lolrobbe2.premakemanagerjet.startup

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class PremakeManagerActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }
}