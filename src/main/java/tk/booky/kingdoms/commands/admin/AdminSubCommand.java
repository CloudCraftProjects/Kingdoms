package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:03 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.commands.admin.end.EndSubCommand;
import tk.booky.kingdoms.commands.admin.pvp.PvpSubCommand;
import tk.booky.kingdoms.commands.admin.spawn.SpawnSubCommand;
import tk.booky.kingdoms.commands.admin.team.TeamSubCommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class AdminSubCommand extends CommandAPICommand {

    public AdminSubCommand(KingdomsManager manager) {
        super("admin");
        withPermission("kingdoms.command.admin");

        withSubcommand(new EndSubCommand(manager));
        withSubcommand(new TeamSubCommand(manager));
        withSubcommand(new SpawnSubCommand(manager));
        withSubcommand(new StartSubCommand(manager));

        if (manager.isRunningCloudPlane()) {
            withSubcommand(new PvpSubCommand(manager));
        }
    }
}
