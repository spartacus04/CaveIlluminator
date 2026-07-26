package me.spartacus04.caveilluminator.config

import me.spartacus04.caveilluminator.CaveIlluminator.Companion.INSTANCE
import me.spartacus04.caveilluminator.config.fields.BlockCraftingField
import me.spartacus04.caveilluminator.config.fields.ChunkShapeField
import me.spartacus04.caveilluminator.config.fields.PlacingModeField
import me.spartacus04.colosseum.config.ConfigField
import me.spartacus04.colosseum.config.FileBind

@Suppress("PropertyName")
data class Config(
    @ConfigField(
        "minimum-light-level",
        "The minimum light level that will be illuminated by the plugin. (0-15)",
        "0"
    )
    var MINIMUM_LIGHT_LEVEL: Int = 0,

    @ConfigField(
        "ignore-sky-light",
        "If true, the plugin will ignore sky light when determining if a block should be illuminated.",
        "true"
    )
    var IGNORE_SKY_LIGHT: Boolean = true,

    @ConfigField(
        "reclaim-blocks-on-break",
        "If true, the plugin will reclaim blocks placed by the illuminator, when it gets broken.",
        "true"
    )
    var RECLAIM_BLOCKS_ON_BREAK: Boolean = true,

    @ConfigField(
        "allowed-block-placing-modes",
        "The block placing modes that are allowed to be used by the plugin.",
        "[]"
    )
    var DISABLED_BLOCK_PLACING_MODES: ArrayList<PlacingModeField> = PlacingModeField.entries.toCollection(arrayListOf()),

    @ConfigField(
        "allowed-chunk-shapes",
        "The chunk shapes that are allowed to be used by the plugin.",
        "[]"
    )
    var DISABLED_CHUNK_SHAPES: ArrayList<ChunkShapeField> = ChunkShapeField.entries.toCollection(arrayListOf()),

    @ConfigField(
        "custom-block-crafting",
        "The custom crafting recipes for the cave illuminator used by the plugin.",
        "[]"
    )
    var CUSTOM_BLOCK_CRAFTING: ArrayList<BlockCraftingField> = arrayListOf(),

    @ConfigField(
        "allow-metrics",
        "If true, the plugin will send anonymous usage data to the developer.",
        "true"
    )
    var ALLOW_METRICS: Boolean = true,

    @ConfigField(
        "check-for-updates",
        "If true, the plugin will check for updates on startup.",
        "true"
    )
    var CHECK_FOR_UPDATES: Boolean = true
) : FileBind("config.json", Config::class.java, INSTANCE)
