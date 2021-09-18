package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:26 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class NetherSubCommand extends CommandAPICommand {

    public NetherSubCommand(KingdomsManager manager) {
        super("nether");

        withPermission("kingdoms.command.admin.nether");

        withSubcommand(new GetLocationSubCommand(manager));
        withSubcommand(new SetLocationSubCommand(manager));
        withSubcommand(new ActivationSubCommand(manager));
        withSubcommand(new GetRadiusSubCommand(manager));
        withSubcommand(new SetRadiusSubCommand(manager));
    }
}
