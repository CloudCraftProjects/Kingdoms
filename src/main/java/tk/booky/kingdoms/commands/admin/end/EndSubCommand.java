package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.Plugin;

public class EndSubCommand extends CommandAPICommand {

    public EndSubCommand(Plugin plugin) {
        super("end");

        withPermission("kingdoms.command.admin.end");

        withSubcommand(new GetLocationSubCommand());
        withSubcommand(new SetLocationSubCommand(plugin));
        withSubcommand(new ActivationSubCommand(plugin));
        withSubcommand(new GetRadiusSubCommand());
        withSubcommand(new SetRadiusSubCommand(plugin));
    }
}
