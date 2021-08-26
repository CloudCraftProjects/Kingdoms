package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in Kingdoms (20:34 26.08.21)

import dev.jorel.commandapi.CommandAPICommand;

public class EndSubCommand extends CommandAPICommand {

    public EndSubCommand() {
        super("end");

        withPermission("kingdoms.command.admin.end");

        withSubcommand(new GetLocationSubCommand());
        withSubcommand(new SetLocationSubCommand());
        withSubcommand(new ActivationSubCommand());
        withSubcommand(new GetRadiusSubCommand());
        withSubcommand(new SetRadiusSubCommand());
    }
}
