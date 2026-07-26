package me.spartacus04.caveilluminator.commands

import me.spartacus04.caveilluminator.CaveIlluminator.Companion.CONFIG
import me.spartacus04.caveilluminator.config.ConfigManager
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nWarn
import org.bukkit.command.CommandSender

class ReloadCommand(private val plugin: ColosseumPlugin) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("reload") {
        description = "Reloads the plugin configuration"
        permissions = hashSetOf("caveilluminator.reload")
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        ctx.sender.sendI18nWarn(plugin, "reloading")

        try {
            ConfigManager.reloadConfig()
        } catch (e: Exception) {
            ctx.sender.sendI18nWarn(plugin, "reload-failed")
            plugin.colosseumLogger.error("Failed to reload configuration: ${e.message}")
            return
        }

        ctx.sender.sendI18nConfirm(plugin, "reloaded")
    }
}