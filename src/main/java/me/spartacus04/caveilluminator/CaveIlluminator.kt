package me.spartacus04.caveilluminator

import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.config.FileBind
import org.bstats.bukkit.Metrics
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class CaveIlluminator : ColosseumPlugin() {
    override fun onEnable() {
        INSTANCE = this

        KEY = NamespacedKey(this, "cave_illuminator")

        buildI18nManager {
            loadInternalLanguageDirectory("langs")
            loadExternalLanguageFiles(dataFolder.resolve("lang.json"), "custom", "langs/en_US")
            setDefaultLocale("en_us")
            this.setLanguagesToLower(true)
        }

        /*if(CONFIG.ALLOW_METRICS)
            Metrics(this, 32812)

        if(CONFIG.CHECK_UPDATE)
            checkForUpdates("spartacus04/CaveIlluminator") {
                if(it != description.version) {
                    colosseumLogger.infoI18n(this, "update-available")
                    colosseumLogger.url("https://modrinth.com/plugin/cave-illuminator")
                }
            } */

        colosseumLogger.confirm("Enabled CaveIlluminator!")
    }

    override fun onDisable() {
        colosseumLogger.warn("Disabled CaveIlluminator!")
    }

    companion object {
        lateinit var INSTANCE: CaveIlluminator
        lateinit var KEY: NamespacedKey
    }
}