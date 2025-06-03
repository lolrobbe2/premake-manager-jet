package com.github.lolrobbe2.premakemanagerjet.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.lolrobbe2.premakemanagerjet.MyBundle

@Service(Service.Level.PROJECT)
class PremakeManagerService(project: Project) {

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    fun getRandomNumber() = (1..100).random()
}
