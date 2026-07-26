import me.spartacus04.caveilluminator.CaveIlluminator
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.INSTANCE
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

/**
 * Manages the crafting recipes for the Cave Illuminator plugin.
 * This object provides methods to add and remove custom crafting recipes for the Cave Illuminator block.
 */
object CraftingManager {
    
    /**
    * Adds a custom crafting recipe to the server using the provided BlockCraftingField.
    * 
    * @param craftingField The BlockCraftingField containing the shape and ingredients for the recipe.
    * @throws IllegalArgumentException if any of the ingredients are invalid materials.
    */
    fun addRecipe(craftingField: BlockCraftingField) {
        val recipe = ShapedRecipe(CaveIlluminator.KEY, BlockManager.getItem())
        
        recipe.shape(
            craftingField.SHAPE[0].joinToString(""),
            craftingField.SHAPE[1].joinToString(""),
            craftingField.SHAPE[2].joinToString("") 
        )
        
        craftingField.INGREDIENTS.forEach { (key, value) ->
            val material = Material.matchMaterial(value)
         
            if (material != null) {
                recipe.setIngredient(key, material)
            } else {
                throw IllegalArgumentException("Invalid material: $value")
            }
        }
        
        INSTANCE.server.addRecipe(recipe)
    }
    
    /**
     * Removes all recipes that have been added by the plugin. This is useful for cleaning up after the plugin is disabled or reloaded.
     */
    fun removeAllRecipes() {
        INSTANCE.server.removeRecipe(CaveIlluminator.KEY)
    }
}