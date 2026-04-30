package com.github.lolrobbe2.premakemanagerjet.manager.commands

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.project.Project

object LibraryCommands {
    suspend fun LibraryInfo(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("library info $githubLink", project)
        } else {
            CommandExecutor.executeCommand("library info", project)
        }
    }
    suspend fun LibraryAdd(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("library add $githubLink", project)
        } else {
            CommandExecutor.executeCommand("library add", project)
        }
    }
    suspend fun LibraryInstall(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("library install $githubLink", project)
        } else {
            CommandExecutor.executeCommand("library install", project)
        }
    }
    suspend fun LibraryRemove(githubLink: String?, project: Project) {
        if (githubLink != null && githubLink != "") {
            CommandExecutor.executeCommand("library remove $githubLink", project)
        } else {
            CommandExecutor.executeCommand("library remove", project)
        }
    }
}