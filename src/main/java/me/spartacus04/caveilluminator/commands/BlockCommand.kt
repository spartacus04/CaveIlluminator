package me.spartacus04.caveilluminator.commands

import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import org.bukkit.entity.Player

class BlockCommand(plugin: ColosseumPlugin) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("block") {
        description = "Gets the cave illuminator"
        permissions = hashSetOf("caveilluminator.block")
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        // TODO: i18n message

        ctx.sender.inventory.addItem(BlockManager.getItem())
    }
}