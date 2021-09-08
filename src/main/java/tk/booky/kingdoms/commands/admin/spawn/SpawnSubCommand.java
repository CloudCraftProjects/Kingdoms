package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.Plugin;

public class SpawnSubCommand extends CommandAPICommand {

    public SpawnSubCommand(Plugin plugin) {
        super("spawn");

        withPermission("kingdoms.command.admin.spawn");

        withSubcommand(new GetLocationSubCommand());
        withSubcommand(new SetLocationSubCommand(plugin));
        withSubcommand(new GetRadiusSubCommand());
        withSubcommand(new SetRadiusSubCommand(plugin));
    }
}
