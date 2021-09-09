package tk.booky.kingdoms.commands.admin.pvp;
// Created by booky10 in Kingdoms (17:12 09.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class PvpSubCommand extends CommandAPICommand {

    public PvpSubCommand(KingdomsManager manager) {
        super("pvp");
        withPermission("kingdoms.command.admin.pvp");

        withSubcommand(new GetPvpTimesSubCommand(manager));
        withSubcommand(new SetPvpTimesSubCommand(manager));
    }
}
