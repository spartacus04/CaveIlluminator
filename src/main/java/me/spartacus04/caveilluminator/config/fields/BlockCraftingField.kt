import me.spartacus04.colosseum.config.ConfigField
import org.bukkit.Material

@Suppress("propertyName")
data class BlockCraftingField(
    @ConfigField(
        "shape",
        "The shape of the crafting recipe. Use a 3x3 grid of characters to represent the recipe.",
        "[]"
    )
    var SHAPE: List<List<Char>> = listOf(listOf(' ', ' ', ' '), listOf(' ', ' ', ' '), listOf(' ', ' ', ' ')),
    
    @ConfigField(
        "ingredients",
        "The ingredients of the crafting recipe. Use a map of characters to material names to represent the ingredients.",
        "{}"
    )    
    var INGREDIENTS: Map<Char, String> = mapOf(' ' to "minecraft:air")
) {
    init {
        if (SHAPE.size != 3 || SHAPE.any { it.size != 3 }) {
            throw IllegalArgumentException("Shape must be a 3x3 grid.")
        }
        
        // ignore spaces in the shape when checking for ingredients
        val usedIngredients = SHAPE.flatten().filter { it != ' ' }.toSet()
        
        if (INGREDIENTS.keys.any { it != ' ' && !usedIngredients.contains(it) }) {
            throw IllegalArgumentException("All ingredients must be used in the shape.")
        }
        
        // do not allow char ' ' to be mapped
        if (INGREDIENTS.containsKey(' ')) {
            throw IllegalArgumentException("The space character ' ' cannot be used as an ingredient key.")
        }
        
        // check if all ingredients are valid materials
        if(INGREDIENTS.values.any { Material.matchMaterial(it) == null}) {
            throw IllegalArgumentException("All ingredients must be valid Minecraft materials.")
        }
    }
}
