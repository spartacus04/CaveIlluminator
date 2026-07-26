package me.spartacus04.caveilluminator.config

import me.spartacus04.caveilluminator.CaveIlluminator.Companion.CONFIG
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.INSTANCE
import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.caveilluminator.block.CraftingManager
import me.spartacus04.colosseum.config.FileBind

object ConfigManager {
    fun getConfig(): Config {
        if(!INSTANCE.dataFolder.exists()) INSTANCE.dataFolder.mkdirs()
        return FileBind.create(Config::class.java)
    }

    fun reloadConfig() {
        CONFIG.read()
        BlockManager.load()

        CraftingManager.removeAllRecipes()

        CONFIG.CUSTOM_BLOCK_CRAFTING.forEach {
            try {
                CraftingManager.addRecipe(it)
            } catch (e: IllegalArgumentException) {
                INSTANCE.colosseumLogger.error("Failed to add crafting recipe for Cave Illuminator: ${e.message}")
            }
        }
    }
}