package com.github.lolrobbe2.premakemanagerjet.manager.commands

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.project.Project

object ConfigCommands {
    suspend fun configSetVersion(tag: String?, project: Project){
        if (tag != null && tag != "") {
            CommandExecutor.executeCommand("config set $tag", project)
        } else {
            CommandExecutor.executeCommand("config set", project)
        }
    }
    suspend fun configView(project: Project) {
        CommandExecutor.executeCommand("config view", project)
    }
    suspend fun configure(project: Project){
        CommandExecutor.executeCommand("configure", project)
    }
}