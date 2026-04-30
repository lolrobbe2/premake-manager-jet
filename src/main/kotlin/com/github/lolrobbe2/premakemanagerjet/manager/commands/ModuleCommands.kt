package com.github.lolrobbe2.premakemanagerjet.manager.commands

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.project.Project

object ModuleCommands {
    suspend fun ModuleInfo(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("module info $githubLink", project)
        } else {
            CommandExecutor.executeCommand("module info", project)
        }
    }
    suspend fun ModuleAdd(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("module add $githubLink", project)
        } else {
            CommandExecutor.executeCommand("module add", project)
        }
    }
    suspend fun ModuleInstall(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("module install $githubLink", project)
        } else {
            CommandExecutor.executeCommand("module install", project)
        }
    }
    suspend fun ModuleRemove(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("module remove $githubLink", project)
        } else {
            CommandExecutor.executeCommand("module remove", project)
        }
    }
}