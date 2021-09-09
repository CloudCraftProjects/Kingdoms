package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class EndSubCommand extends CommandAPICommand {

    public EndSubCommand(KingdomsManager manager) {
        super("end");

        withPermission("kingdoms.command.admin.end");

        withSubcommand(new GetLocationSubCommand(manager));
        withSubcommand(new SetLocationSubCommand(manager));
        withSubcommand(new ActivationSubCommand(manager));
        withSubcommand(new GetRadiusSubCommand(manager));
        withSubcommand(new SetRadiusSubCommand(manager));
    }
}
