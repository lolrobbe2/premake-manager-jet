package com.github.lolrobbe2.premakemanagerjet.manager

import com.intellij.openapi.project.Project

object VersionCommands {
    suspend fun setVersion(tag: String?, project: Project){
        if (tag != null) {
            CommandExecutor.executeCommand("version set $tag", project)
        } else {
            CommandExecutor.executeCommand("version set", project)
        }
    }
    suspend fun listInstalled(project: Project){
        CommandExecutor.executeCommand("version list --installed", project)
    }
    suspend fun listReleases(project: Project){
        CommandExecutor.executeCommand("version list --releases", project)
    }
}