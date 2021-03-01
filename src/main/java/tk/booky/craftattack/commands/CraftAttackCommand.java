package tk.booky.craftattack.commands;
// Created by booky10 in CraftAttack (14:36 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;

public class CraftAttackCommand extends CommandAPICommand {

    public CraftAttackCommand() {
        super("craftattack");

        withAliases("ca");
        withPermission("craftattack.command");
    }
}