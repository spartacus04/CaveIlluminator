package me.spartacus04.caveilluminator.block

import com.google.gson.reflect.TypeToken
import de.tr7zw.nbtapi.NBT
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.INSTANCE
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.KEY
import me.spartacus04.caveilluminator.CaveIlluminator.Companion.VERSION
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Skull
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.lang.reflect.Type
import java.net.URI
import java.net.URL
import java.util.UUID

/**
 * Manages the custom Cave Illuminator block.
 * This object provides methods to create and manage the custom block item.
 */
object BlockManager {
    // https://minesk.in/0e1403bf558447c5a20024510aee23a8
    private const val TEXTURE_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTc4NTA4NTgwMjA0NiwKICAicHJvZmlsZUlkIiA6ICI3MWU4ODU5ODVkOGM0ZDg2ODMxODYxNDU1NjE3MDY2YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJKb3NoeV9XYXNoeSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYTMxOWZhMWU4YzhkNmFjMTE0ZjMyYzgxMjA1Yzg0YjNkMzQyOTNlN2VlN2RiNGExZDM0ZmU1N2YwMTJiZGU2IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
    private val TEXTURE_URL : URL =
        URI("https://textures.minecraft.net/texture/fa319fa1e8c8d6ac114f32c81205c84b3d34293e7ee7db4a1d34fe57f012bde6").toURL()
    private val ILLUMINATOR_PROFILE_UUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")

    val illuminatorsFile = INSTANCE.dataFolder.resolve("saved.json")
    var illuminatorLocations = hashMapOf<Location, HashMap<Location, Material>>()
    val illuminatorTypeToken: Type = object : TypeToken<Map<Location, Map<Location, Material>>>() {}.type
    private val particleManager = ParticleManager(INSTANCE)

    private fun buildItemStack(): ItemStack {
        val stack = ItemStack(Material.PLAYER_HEAD)
        
        if(VERSION >= "1.18.1") {
            // Use spigot API
            val profile = Bukkit.createPlayerProfile(ILLUMINATOR_PROFILE_UUID, "")
            
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
                
                skullOwnerCompound.setUUID("Id", ILLUMINATOR_PROFILE_UUID)
                
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
    
    private val caveIlluminatorItem : ItemStack by lazy { buildItemStack() }
    /**
     * Creates an ItemStack representing the custom Cave Illuminator block.
     * The block is represented as a player head with a custom texture.
     *
     * @return An ItemStack of the custom Cave Illuminator block.
     */
    fun getItem(): ItemStack {
        return caveIlluminatorItem.clone()
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

    fun placeBlock(block: Block) {
        illuminatorLocations[block.location] = hashMapOf()
        particleManager.startParticles(block)
        save()
    }

    fun destroyBlock(block: Block) {
        val removed = illuminatorLocations.remove(block.location)
        particleManager.stopParticles(block.location)

        // TODO: handle destruction of built blocks by either reclaiming or not

        save()
    }

    fun load() {
        // save file
        if(!INSTANCE.dataFolder.exists()) {
            INSTANCE.dataFolder.mkdirs()
        }

        if(!illuminatorsFile.exists()) return

        val text = illuminatorsFile.bufferedReader().readText()

        illuminatorLocations = INSTANCE.gson.fromJson(text, illuminatorTypeToken)

        illuminatorLocations.keys.forEach {
            particleManager.startParticles(it.block)
        }
    }

    fun save() {
        val text = INSTANCE.gson.toJson(illuminatorLocations, illuminatorTypeToken)

        illuminatorsFile.bufferedWriter().use { writer ->
            writer.write(text)
        }
    }
}