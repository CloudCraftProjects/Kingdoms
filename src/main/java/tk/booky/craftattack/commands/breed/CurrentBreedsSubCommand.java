package tk.booky.craftattack.commands.breed;
// Created by booky10 in CraftAttack (12:13 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import tk.booky.craftattack.manager.CraftAttackManager;

public class CurrentBreedsSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    public CurrentBreedsSubCommand() {
        super("current");

        withPermission("craftattack.command.breed.current");

        executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        int breeds = CraftAttackManager.getBreeds(sender.getUniqueId());
        if (breeds <= 0) {
            CommandAPI.fail("You haven't breed any animals yet!");
        } else if (breeds == 1) {
            sender.sendMessage("You currently have breed one animal!");
        } else {
            sender.sendMessage("You currently have breed " + breeds + " animals!");
        }
    }
}