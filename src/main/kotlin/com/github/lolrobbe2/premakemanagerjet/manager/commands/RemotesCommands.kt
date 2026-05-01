package com.github.lolrobbe2.premakemanagerjet.manager.commands

import com.github.lolrobbe2.premakemanagerjet.manager.CommandExecutor
import com.intellij.openapi.project.Project

object RemotesCommands {
    suspend fun checkValid(list: List<String?>): Boolean {
        val filteredList = list.filter { !it.equals("", ignoreCase = true) }
        return filteredList.isNotEmpty();
    }

    suspend fun RemotesView(project: Project) {
        CommandExecutor.executeCommand("remotes view", project);
    }

    suspend fun RemotesAdd(owner: String?, repo: String?, project: Project) {
        if (checkValid(listOf(owner, repo))) {
            CommandExecutor.executeCommand("remotes add $owner $repo", project);
        } else {
            CommandExecutor.executeCommand("remotes add", project);
        }
    }

    suspend fun RemotesUpdate(force: Boolean, project: Project) {
        if (force) {
            CommandExecutor.executeCommand("remotes update True", project);
        } else {
            CommandExecutor.executeCommand("remotes update False", project);
        }
    }
    suspend fun RemotesRemove(owner: String?, repo: String?,project: Project) {
        if (checkValid(listOf(owner, repo))) {
            CommandExecutor.executeCommand("remotes remove $owner $repo", project);
        } else {
            CommandExecutor.executeCommand("remotes remove", project);
        }
    }
    suspend fun RemotesReset(project: Project) {
        CommandExecutor.executeCommand("remotes reset", project);
    }
}