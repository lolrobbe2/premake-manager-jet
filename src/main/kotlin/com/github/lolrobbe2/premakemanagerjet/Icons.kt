package com.github.lolrobbe2.premakemanagerjet

import com.intellij.execution.actions.RunConfigurationsComboBoxAction.EMPTY_ICON
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

@NonNls
object Icons {
    @JvmField val PremakeManagerFull = load("/icons/premake_manager.png");
    @JvmField val PremakeManager = load("/icons/premake_manager.png");
    @JvmStatic private fun load(icon: String): Icon = IconLoader.getIcon(icon, Icons::class.java)

    @JvmStatic private fun dup(icon: Icon): Icon = runCatching {
        IconUtil.createImageIcon(IconUtil.toBufferedImage(icon))
    }
        .getOrElse { EMPTY_ICON }
}