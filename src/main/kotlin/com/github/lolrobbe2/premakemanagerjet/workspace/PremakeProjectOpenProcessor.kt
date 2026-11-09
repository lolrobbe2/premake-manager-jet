package com.github.lolrobbe2.premakemanagerjet.workspace
import com.intellij.openapi.diagnostic.logger
import com.github.lolrobbe2.premakemanagerjet.Icons
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.projectImport.ProjectOpenProcessor
import javax.swing.Icon

class PremakeProjectOpenProcessor : ProjectOpenProcessor()  {
    override val icon: Icon
        get() = Icons.PremakeManager
    private val log = Logger.getInstance(PremakeProjectOpenProcessor::class.java)

    init {
        log.warn("PremakeProjectOpenProcessor instantiated")
    }
    override fun canOpenProject(file: VirtualFile): Boolean {
        if (!file.isDirectory) {
            return false
        }

        val premakeFile = file.findChild("premake5.lua")
        return premakeFile != null && premakeFile.isValid && !premakeFile.isDirectory
    }

    override val name: String
        get() = "Premake"
    override val isStrongProjectInfoHolder: Boolean
        get() = true
}