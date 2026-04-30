package com.github.lolrobbe2.premakemanagerjet.manager.commands

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.project.Project

object IndexCommands {
    suspend fun checkValid(list: List<String?>): Boolean {
        val filteredList = list.filter { !it.equals("", ignoreCase = true) }
        return filteredList.isNotEmpty();
    }

    suspend fun indexNew(remote: String?, project: Project) {
        if (checkValid(listOf(remote))) {
            CommandExecutor.executeCommand("index new $remote", project)
        } else {
            CommandExecutor.executeCommand("index new", project)
        }
    }

    suspend fun indexAddLibrary(remote: String?, repo: String?, description: String?, project: Project) {
        if (checkValid(listOf(remote, repo, description))) {
            CommandExecutor.executeCommand("index add library $remote $remote $description", project)
        } else {
            CommandExecutor.executeCommand("index add library", project)
        }
    }

    suspend fun indexUriLibrary(uri: String?, project: Project) {
        if (checkValid(listOf(uri))) {
            CommandExecutor.executeCommand("index add uri library $uri", project)
        } else {
            CommandExecutor.executeCommand("index add library", project)
        }
    }

    suspend fun indexAddDependency(
        githubLink: String?,
        owner: String?,
        repo: String?,
        range: String?,
        project: Project
    ) {
        if (checkValid(listOf(githubLink))) {
            CommandExecutor.executeCommand("index add dependency $githubLink $owner $repo $range", project)
        }
    }
}