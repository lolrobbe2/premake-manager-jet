package com.github.lolrobbe2.premakemanagerjet.runner

import com.github.lolrobbe2.premakemanagerjet.services.LocalStorage
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions

class PremakeTerminalRunner(project: Project) : LocalTerminalDirectRunner(project) {

    // This is the name that appears in the session selection
    override fun getDefaultTabTitle(): String {
            return "Premake Session";
    }

    override fun configureStartupOptions(baseOptions: ShellStartupOptions): ShellStartupOptions {
        val options = super.configureStartupOptions(baseOptions);
        return options.builder().shellCommand(listOf(LocalStorage.getCurrentCLIPath())).build();
    }
}