package tk.booky.kingdoms.commands;
// Created by booky10 in CraftAttack (14:36 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.commands.admin.AdminSubCommand;
import tk.booky.kingdoms.commands.team.TeamSubCommand;
import tk.booky.kingdoms.commands.teleport.TeleportSubCommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class KingdomsRootCommand extends CommandAPICommand {

    public KingdomsRootCommand(KingdomsManager manager) {
        super("kingdoms");

        withAliases("kd");
        withPermission("kingdoms.command");

        withSubcommand(new TeleportSubCommand(manager));
        withSubcommand(new AdminSubCommand(manager));
        withSubcommand(new TeamSubCommand(manager));
    }
}
