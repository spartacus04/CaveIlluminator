import de.tr7zw.nbtapi.NBT
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.KEY
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.VERSION
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Skull
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.net.URI
import java.net.URL
import java.util.UUID

/**
 * Manages the custom Cave Illuminator block.
 * This object provides methods to create and manage the custom block item.
 */
object BlockManager {
    // https://minesk.in/274232b770374f9ea741e241e112829b
    const val TEXTURE_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTc4NTA2OTU3ODA3NSwKICAicHJvZmlsZUlkIiA6ICI4NzA1MzkxMDBlOGQ0MzU2YjJiYjlmZGI1NjFhOGU3ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJfcmVncmV0dHQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5M2Q4MDgxZjQzMGUyZTIzZDNhNjQ2MmNjYzQ4YjEyN2VjZjNmNjNkOGJkYWE2MGU0OGE2Yzc5NmRkMjU2NiIKICAgIH0KICB9Cn0="
    val TEXTURE_URL : URL =
        URI("https://textures.minecraft.net/texture/d893d8081f430e2e23d3a6462ccc48b127ecf3f63d8bdaa60e48a6c796dd2566").toURL()
    
    private fun buildItemStack(): ItemStack {
        val stack = ItemStack(Material.PLAYER_HEAD)
        
        if(VERSION >= "1.18.1") {
            // Use spigot API
            val profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CaveIlluminatorBlock")
            
            @Suppress("UsePropertyAccessSyntax")
            profile.setTextures(profile.textures.apply {
                skin = TEXTURE_URL
            })

            stack.itemMeta = stack.itemMeta.apply {
                (this as SkullMeta).ownerProfile = profile
            }
        } else {
            NBT.modify(stack) {
                val skullOwnerCompound = it.getOrCreateCompound("SkullOwner")
                
                skullOwnerCompound.setUUID("Id", UUID.randomUUID())
                
                skullOwnerCompound
                    .getOrCreateCompound("Properties")
                    .getCompoundList("textures")
                    .addCompound()
                    .setString("Value", TEXTURE_VALUE)
            }
        }
        
        stack.itemMeta = stack.itemMeta!!.apply {
            // TODO: use i18n
            setDisplayName("§6Cave Illuminator")
            setLore(listOf(
                "§7A block that illuminates nearby caves.",
            ))
            
            persistentDataContainer.set(
                KEY,
                PersistentDataType.BOOLEAN,
                true
            )
        }

        return stack
    }
    
    private val item : ItemStack by lazy { buildItemStack() }
    
    /**
     * Creates an ItemStack representing the custom Cave Illuminator block.
     * The block is represented as a player head with a custom texture.
     *
     * @return An ItemStack of the custom Cave Illuminator block.
     */
    fun getItem(): ItemStack {
        return item.clone()
    }
    
    /**
     * Marks the given block as a Cave Illuminator block by setting a persistent data key on it. This allows the plugin to identify the block later.
     */
    fun markAsCaveIlluminatorBlock(block: Block) {
        val skull = block.state as? Skull ?: return
        
        skull.persistentDataContainer.set(
            KEY,
            PersistentDataType.BOOLEAN,
            true
        )
    }
    
    /**
     * Checks if the given ItemStack is a Cave Illuminator itemstack.
     * 
     * @param stack The ItemStack to check.
     * @return true if the ItemStack is a Cave Illuminator itemstack, false otherwise.
     */
    fun isCaveIlluminator(stack: ItemStack): Boolean {
        val meta = stack.itemMeta ?: return false
        
        return meta.persistentDataContainer.has(
            KEY,
            PersistentDataType.BOOLEAN
        )
    }
    
    /**
     * Checks if the given block is a Cave Illuminator block.
     *
     * @param block The block to check.
     * @return true if the block is a Cave Illuminator block, false otherwise.
     */
    fun isCaveIlluminator(block: Block): Boolean {
        val skull = block.state as? Skull ?: return false
        
        return skull.persistentDataContainer.has(
            KEY,
            PersistentDataType.BOOLEAN
        )
    }
}