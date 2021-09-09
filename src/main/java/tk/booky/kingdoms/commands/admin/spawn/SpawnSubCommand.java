package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class SpawnSubCommand extends CommandAPICommand {

    public SpawnSubCommand(KingdomsManager manager) {
        super("spawn");

        withPermission("kingdoms.command.admin.spawn");

        withSubcommand(new GetLocationSubCommand(manager));
        withSubcommand(new SetLocationSubCommand(manager));
        withSubcommand(new GetRadiusSubCommand(manager));
        withSubcommand(new SetRadiusSubCommand(manager));
    }
}
