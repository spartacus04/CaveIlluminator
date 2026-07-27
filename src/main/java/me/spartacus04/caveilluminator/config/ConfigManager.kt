package me.spartacus04.caveilluminator.config

import me.spartacus04.caveilluminator.CaveIlluminator.Companion.CONFIG
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.INSTANCE
import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.caveilluminator.block.CraftingManager
import me.spartacus04.colosseum.config.FileBind

/**
 * Manages the configuration loading of the plugin
 */
object ConfigManager {
    /**
     * Loads the config into the plugin at launch. If the config does not exist, it will be created with default values.
     * Should only be called once.
     */
    fun getFirstTimeConfig(): Config {
        if(!INSTANCE.dataFolder.exists()) INSTANCE.dataFolder.mkdirs()
        return FileBind.create(Config::class.java)
    }

    /**
     * Reloads the config across the plugin
     */
    fun reloadConfig() {
        CONFIG.read()
        println("CONFIG.CUSTOM_BLOCK_CRAFTING size: ${CONFIG.CUSTOM_BLOCK_CRAFTING.size}")

        BlockManager.load()
        CraftingManager.removeAllRecipes()

        CONFIG.CUSTOM_BLOCK_CRAFTING.forEach {
            println("Attempting recipe: shape=${it.SHAPE} ingredients=${it.INGREDIENTS}")
            try {
                CraftingManager.addRecipe(it)
                println("Recipe added successfully")
            } catch (e: IllegalArgumentException) {
                println("FAILED: ${e.message}")
                INSTANCE.colosseumLogger.error("Failed to add crafting recipe for Cave Illuminator: ${e.message}")
            }
        }
    }
}