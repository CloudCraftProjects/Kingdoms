package tk.booky.craftattack.commands.admin.breed;
// Created by booky10 in CraftAttack (11:55 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class ResetBreedsSubCommand extends CommandAPICommand implements CommandExecutor {

    public ResetBreedsSubCommand() {
        super("reset");

        withPermission("craftattack.command.admin.breed.reset");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        if (CraftAttackManager.getBreeds().size() <= 0) {
            CommandAPI.fail("There are no breeds yet!");
        } else {
            CraftAttackManager.resetBreeds();
            sender.sendMessage("The breeds have been successfully reset!");
        }
    }
}