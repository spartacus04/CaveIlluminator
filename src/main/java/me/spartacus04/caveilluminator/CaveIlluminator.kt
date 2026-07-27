package me.spartacus04.caveilluminator

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.caveilluminator.block.IlluminatorTypeAdapter
import me.spartacus04.caveilluminator.commands.MainCommand
import me.spartacus04.caveilluminator.config.Config
import me.spartacus04.caveilluminator.config.ConfigManager
import me.spartacus04.caveilluminator.listeners.BlockListener
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.utils.version.MinecraftServerVersion
import org.bstats.bukkit.Metrics
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey

@Suppress("unused")
class CaveIlluminator : ColosseumPlugin() {
    override fun buildGson(block: GsonBuilder.() -> Unit): Gson {
        return super.buildGson {
            registerTypeAdapter(
                object : TypeToken<Map<Location, Map<Location, Material>>>() {}.type,
                IlluminatorTypeAdapter()
            )
            block()
        }
    }

    override fun onEnable() {
        INSTANCE = this
        VERSION = this.serverVersion
        KEY = NamespacedKey(this, "cave_illuminator")
        CONFIG = ConfigManager.getFirstTimeConfig()

        ConfigManager.reloadConfig()

        buildI18nManager {
            loadInternalLanguageDirectory("langs")
            loadExternalLanguageFiles(dataFolder.resolve("lang.json"), "custom", "langs/en_US")
            setDefaultLocale("en_us")
            this.setLanguagesToLower(true)
        }

        registerCommands {
            addCommand(MainCommand::class.java)
        }

        BlockListener(this).register()

        if(CONFIG.ALLOW_METRICS)
            Metrics(this, 32812)

        if(CONFIG.CHECK_FOR_UPDATES)
            checkForUpdates("spartacus04/CaveIlluminator") {
                if(it != description.version) {
                    colosseumLogger.infoI18n(this, "update-available")
                    colosseumLogger.url("https://modrinth.com/plugin/cave-illuminator")
                }
            }

        colosseumLogger.confirm("Enabled CaveIlluminator!")
    }

    override fun onDisable() {
        colosseumLogger.warn("Disabled CaveIlluminator!")
    }

    companion object {
        lateinit var INSTANCE: CaveIlluminator
        lateinit var CONFIG: Config
        lateinit var KEY: NamespacedKey
        lateinit var VERSION: MinecraftServerVersion
    }
}