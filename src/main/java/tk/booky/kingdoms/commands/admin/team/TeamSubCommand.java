package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (18:29 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class TeamSubCommand extends CommandAPICommand {

    public TeamSubCommand(KingdomsManager manager) {
        super("team");

        withPermission("kingdoms.command.admin.team");

        withSubcommand(new GetTeamSubCommand(manager));
        withSubcommand(new SetTeamSubCommand(manager));
        withSubcommand(new GetKingSubCommand(manager));
        withSubcommand(new SetKingSubCommand(manager));
        withSubcommand(new GetCoinsSubCommand(manager));
        withSubcommand(new SetCoinsSubCommand(manager));
        withSubcommand(new GetTreasureSubCommand(manager));
        withSubcommand(new SetTreasureSubCommand(manager));
    }
}
