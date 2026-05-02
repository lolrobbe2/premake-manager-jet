package com.github.lolrobbe2.premakemanagerjet.actions

import com.github.lolrobbe2.premakemanagerjet.services.LocalStorage
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions

class PremakeCliTerminalRunner(
    project: Project,
    private val token: String,
) : LocalTerminalDirectRunner(project) {

    override fun configureStartupOptions(baseOptions: ShellStartupOptions): ShellStartupOptions {
        val builder =  baseOptions.builder()
        // Inject working directory
        project.basePath?.let { wd ->
            builder.workingDirectory = wd
        }

        // Inject command
        builder.shellCommand(
            listOf(
                LocalStorage.getCurrentCLIPath(),
                "--interactive"
            )
        )

        // Inject environment variables
        val mergedEnv = (baseOptions.envVariables ?: emptyMap()) + mapOf(
            "GITHUB_TOKEN" to token
        )
        builder.envVariables(mergedEnv)
        val updated = builder.build()
        super.configureStartupOptions(updated)
        return updated
    }

    override fun getDefaultTabTitle(): String = "Premake Manager"
}