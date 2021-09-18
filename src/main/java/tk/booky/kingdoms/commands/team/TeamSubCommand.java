package tk.booky.kingdoms.commands.team;
// Created by booky10 in Kingdoms (14:35 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class TeamSubCommand extends CommandAPICommand {

    public TeamSubCommand(KingdomsManager manager) {
        super("team");
        withPermission("kingdoms.command.team");

        withSubcommand(new SwitchSubCommand(manager));
    }
}
