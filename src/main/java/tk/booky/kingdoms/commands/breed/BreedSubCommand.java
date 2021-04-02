package tk.booky.kingdoms.commands.breed;
// Created by booky10 in CraftAttack (12:10 19.03.21)

import dev.jorel.commandapi.CommandAPICommand;

public class BreedSubCommand extends CommandAPICommand {

    public BreedSubCommand() {
        super("breed");

        withPermission("kingdoms.command.breed");

        withSubcommand(new CurrentBreedsSubCommand());
        withSubcommand(new BreedLeaderboardSubCommand());
    }
}