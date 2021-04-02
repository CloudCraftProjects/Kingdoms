package tk.booky.kingdoms.commands;
// Created by booky10 in CraftAttack (14:36 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.commands.admin.AdminSubCommand;
import tk.booky.kingdoms.commands.breed.BreedSubCommand;
import tk.booky.kingdoms.commands.teleport.TeleportSubCommand;

public class KingdomsRootCommand extends CommandAPICommand {

    public KingdomsRootCommand() {
        super("kingdoms");

        withAliases("kd");
        withPermission("kingdoms.command");

        withSubcommand(new TeleportSubCommand());
        withSubcommand(new AdminSubCommand());
        withSubcommand(new BreedSubCommand());
    }
}