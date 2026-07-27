package me.spartacus04.caveilluminator.listeners

import me.spartacus04.caveilluminator.CaveIlluminator.Companion.KEY
import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.listeners.ColosseumListener
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Skull
import org.bukkit.block.data.Rotatable
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

@Suppress("unused")
class BlockListener(plugin: ColosseumPlugin) : ColosseumListener(plugin) {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if(!BlockManager.isCaveIlluminator(event.itemInHand)) return

        // Set identifier
        val state = event.blockPlaced.state as? Skull ?: return
        state.persistentDataContainer.set(KEY, PersistentDataType.BOOLEAN, true)

        // Avoid strange angles
        state.blockData = state.blockData.apply {
            if (this is Rotatable) {
                rotation = BlockFace.NORTH
            }
        }

        state.update()

        BlockManager.placeBlock(event.blockPlaced)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if(!BlockManager.isCaveIlluminator(event.block)) return

        event.isDropItems = false
        event.block.world.dropItemNaturally(event.block.location.add(0.5, 0.5, 0.5), BlockManager.getItem())

        BlockManager.destroyBlock(event.block)
    }

    @EventHandler
    fun onBlockClick(event: PlayerInteractEvent) {
        if(!BlockManager.isCaveIlluminator(event.clickedBlock ?: return)) return
        if(event.action != Action.LEFT_CLICK_BLOCK) return

        // open GUI
    }
}