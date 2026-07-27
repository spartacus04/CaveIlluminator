package me.spartacus04.caveilluminator.config.fields

import me.spartacus04.colosseum.config.ConfigField
import org.bukkit.Material

@Suppress("PropertyName")
data class BlockCraftingField(
    @ConfigField(
        "shape",
        "The shape of the crafting recipe. A list of 3 strings, each 3 characters long, representing the 3x3 grid.",
        "[]"
    )
    var SHAPE: List<String> = listOf(),

    @ConfigField(
        "ingredients",
        "The ingredients of the crafting recipe. Use a map of characters to material names to represent the ingredients.",
        "{}"
    )
    var INGREDIENTS: Map<Char, String> = mapOf()
)