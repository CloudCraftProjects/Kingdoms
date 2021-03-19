package tk.booky.craftattack.commands.breed;
// Created by booky10 in CraftAttack (12:10 19.03.21)

import dev.jorel.commandapi.CommandAPICommand;

public class BreedSubCommand extends CommandAPICommand {

    public BreedSubCommand() {
        super("breed");

        withPermission("craftattack.command.breed");
    }
}