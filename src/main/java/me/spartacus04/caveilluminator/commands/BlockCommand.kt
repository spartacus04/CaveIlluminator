package me.spartacus04.caveilluminator.commands

import me.spartacus04.caveilluminator.block.BlockManager
import me.spartacus04.caveilluminator.config.ConfigManager
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand.CommandContext
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nWarn
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BlockCommand(private val plugin: ColosseumPlugin) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("block") {
        description = "Gets the cave illuminator"
        permissions = hashSetOf("caveilluminator.block")
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        // TODO: i18n message

        ctx.sender.inventory.addItem(BlockManager.getItem())
    }
}