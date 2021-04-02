package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:03 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.commands.admin.breed.BreedSubCommand;

public class AdminSubCommand extends CommandAPICommand {

    public AdminSubCommand() {
        super("admin");

        withPermission("kingdoms.command.admin");

        withSubcommand(new GetEndRadiusSubCommand());
        withSubcommand(new SetEndRadiusSubCommand());

        withSubcommand(new GetSpawnRadiusSubCommand());
        withSubcommand(new SetSpawnRadiusSubCommand());

        withSubcommand(new GetEndLocationSubCommand());
        withSubcommand(new SetEndLocationSubCommand());

        withSubcommand(new GetSpawnLocationSubCommand());
        withSubcommand(new SetSpawnLocationSubCommand());

        withSubcommand(new BreedSubCommand());
    }
}