package com.github.lolrobbe2.premakemanagerjet.actions

import com.github.lolrobbe2.premakemanagerjet.services.LocalStorage
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions
import org.jetbrains.plugins.terminal.TerminalOptionsProvider
import org.jetbrains.plugins.terminal.settings.TerminalLocalOptions

class PremakeTerminalRunner(project: Project) : LocalTerminalDirectRunner(project)   {
    override fun getDefaultTabTitle(): String = "Premake5"

    override fun configureStartupOptions(baseOptions: ShellStartupOptions): ShellStartupOptions {
        val builder = baseOptions.builder()
        // Inject working directory
        project.basePath?.let { wd ->
            builder.workingDirectory = wd
        }
        val refreshedPath = getRefreshedPath()
        if (!refreshedPath.isNullOrBlank()) {
            val envs = HashMap(builder.envVariables ?: emptyMap())
            envs["PATH"] = refreshedPath
            envs["Path"] = refreshedPath // Windows compatibility
            builder.envVariables = envs
        }
        val configuredShell = TerminalLocalOptions.getInstance().shellPath
        val shellPath = if (!configuredShell.isNullOrBlank()) {
            configuredShell
        } else {
            when {
                SystemInfo.isWindows -> "powershell.exe"
                SystemInfo.isMac || SystemInfo.isLinux -> System.getenv("SHELL") ?: "/bin/bash"
                else -> "/bin/sh"
            }
        }

        builder.shellCommand(listOf(shellPath))
        return builder.build()
    }
    private fun getRefreshedPath(): String? {
        return try {
            val process = if (System.getProperty("os.name").lowercase().contains("win")) {
                ProcessBuilder(
                    "powershell.exe",
                    "-NoProfile",
                    "-Command",
                    "[Environment]::GetEnvironmentVariable('PATH','Machine') + ';' + [Environment]::GetEnvironmentVariable('PATH','User')"
                )
            } else {
                ProcessBuilder("bash", "-ilc", "echo \$PATH")
            }

            process.redirectErrorStream(true)
            val result = process.start()
                .inputStream.bufferedReader()
                .readText()
                .trim()

            result.takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }
}