package me.spartacus04.caveilluminator.listeners

import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.listeners.ColosseumListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class BlockListener(plugin: ColosseumPlugin) : ColosseumListener(plugin) {
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if(!BlockManager.isCaveIlluminator(event.itemInHand)) return

        BlockManager.placeBlock(event.blockPlaced)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if(!BlockManager.isCaveIlluminator(event.block)) return

        BlockManager.destroyBlock(event.block)
    }

    @EventHandler
    fun onBlockClick(event: PlayerInteractEvent) {
        if(!BlockManager.isCaveIlluminator(event.clickedBlock ?: return)) return
        if(event.action != Action.LEFT_CLICK_BLOCK) return

        // open GUI
    }
}