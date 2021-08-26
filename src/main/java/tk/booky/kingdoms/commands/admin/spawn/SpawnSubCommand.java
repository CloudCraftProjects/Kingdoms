package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;

public class SpawnSubCommand extends CommandAPICommand {

    public SpawnSubCommand() {
        super("spawn");

        withPermission("kingdoms.command.admin.spawn");

        withSubcommand(new GetLocationSubCommand());
        withSubcommand(new SetLocationSubCommand());
        withSubcommand(new GetRadiusSubCommand());
        withSubcommand(new SetRadiusSubCommand());
    }
}
