package tk.booky.craftattack.commands.admin.breed;
// Created by booky10 in CraftAttack (11:53 19.03.21)

import dev.jorel.commandapi.CommandAPICommand;

public class BreedSubCommand extends CommandAPICommand {

    public BreedSubCommand() {
        super("breed");

        withPermission("craftattack.command.admin.breed");

        withSubcommand(new ResetBreedsSubCommand());

        withSubcommand(new GetBreedsSubCommand());
    }
}