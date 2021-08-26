package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:03 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.commands.admin.end.EndSubCommand;
import tk.booky.kingdoms.commands.admin.spawn.SpawnSubCommand;

public class AdminSubCommand extends CommandAPICommand {

    public AdminSubCommand() {
        super("admin");
        withPermission("kingdoms.command.admin");

        withSubcommand(new EndSubCommand());
        withSubcommand(new SpawnSubCommand());
    }
}
