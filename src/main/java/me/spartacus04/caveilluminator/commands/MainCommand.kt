package me.spartacus04.caveilluminator.commands

import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.commandHandling.command.ColosseumBaseCommand

class MainCommand(plugin: ColosseumPlugin) : ColosseumBaseCommand(plugin, "caveilluminator", listOf(
    ReloadCommand(plugin),
    BlockCommand(plugin)
))