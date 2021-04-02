package tk.booky.kingdoms.commands.admin.breed;
// Created by booky10 in CraftAttack (11:55 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class ResetBreedsSubCommand extends CommandAPICommand implements CommandExecutor {

    public ResetBreedsSubCommand() {
        super("reset");

        withPermission("kingdoms.command.admin.breed.reset");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        if (KingdomsManager.getBreeds().size() <= 0) {
            CommandAPI.fail("There are no breeds yet!");
        } else {
            KingdomsManager.resetBreeds();
            sender.sendMessage("The breeds have been successfully reset!");
        }
    }
}