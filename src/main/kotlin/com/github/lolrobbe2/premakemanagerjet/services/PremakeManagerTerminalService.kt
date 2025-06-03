package com.github.lolrobbe2.premakemanagerjet.services
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.components.Service
import com.intellij.platform.ide.progress.ModalTaskOwner.project
import com.intellij.terminal.TerminalExecutionConsole
import java.io.OutputStream
import com.intellij.openapi.project.Project
import java.io.File

@Service(Service.Level.PROJECT)
class PremakeManagerTerminalService(private val project: Project) {

    fun executeCommand(command: String) {
        val commandLine = GeneralCommandLine(command)
        val processHandler = object : ProcessHandler() {
            override fun startNotify() {
                super.startNotify()
                commandLine.createProcess()
            }

            override fun detachProcessImpl() {}
            override fun destroyProcessImpl() {}
            override fun detachIsDefault(): Boolean = false
            override fun getProcessInput(): OutputStream? = null
        }

        val console = TerminalExecutionConsole(project, processHandler)
        processHandler.startNotify()
    }

    fun runPremakeInteractive() {
        // Determine platform subfolder name
        val platformFolder = when {
            System.getProperty("os.name").lowercase().contains("win") -> "windows"
            System.getProperty("os.name").lowercase().contains("mac") -> "macos"
            System.getProperty("os.name").lowercase().contains("linux") -> "linux"
            else -> throw UnsupportedOperationException("Unsupported OS: ${System.getProperty("os.name")}")
        }

// Determine executable name based on platform
        val executableName = if (platformFolder == "windows") "premake-manager-cli.exe" else "premake-manager-cli"
        val resourceURL = javaClass.classLoader.getResource("/")
        resourceURL?.let {
            val file = File(it.toURI())
            file.listFiles()?.forEach { println("Resource File: ${it.name}") }
        } ?: println("Folder not found: bin")

// Locate executable inside resources/bin/{platform}/
        val resourcePath = javaClass.classLoader.getResource("bin/$platformFolder/$executableName")?.toURI()?.path
            ?: throw IllegalStateException("Executable not found in bin/$platformFolder!")



        val executableFile = File(resourcePath)
        if (!executableFile.exists() || !executableFile.canExecute()) {
            throw IllegalStateException("Executable file is missing or not executable: $resourcePath")
        }

        // Prepare and execute command
        val commandLine = GeneralCommandLine(executableFile.absolutePath, "--interactive")
        val processHandler = OSProcessHandler(commandLine)

        val console = TerminalExecutionConsole(project, processHandler)
        processHandler.startNotify()
    }
}