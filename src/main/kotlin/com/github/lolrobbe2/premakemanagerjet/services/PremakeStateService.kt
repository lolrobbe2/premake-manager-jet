package com.github.lolrobbe2.premakemanagerjet.services

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.APP)
@State(
    name = "PremakeCliState",
    storages = [Storage("premake-cli.xml")]
)
enum class VersionType{
    LATEST,
    ARTIFACT,
    PATH,
}
class PremakeCliStateService : PersistentStateComponent<PremakeCliStateService.State> {

    class State {
        var versionType: VersionType = VersionType.LATEST
        var installedSha256: String? = null
    }

    private var state = State()

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    public fun setVersionType(versionType: VersionType) {
        state.versionType = versionType;
    }

    public fun checkVersionType(versionType: VersionType) : Boolean {
        return state.versionType == versionType;
    }
    public fun getVersionType() : VersionType { return state.versionType }
    public fun setInstalledSha256(installedSha256: String) {
        state.installedSha256 = installedSha256;
    }
}