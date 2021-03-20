package tk.booky.craftattack.commands.admin.breed;
// Created by booky10 in CraftAttack (11:57 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class SetBreedsSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetBreedsSubCommand() {
        super("set");

        withPermission("craftattack.command.admin.breed.set");
        withArguments(new ScoreHolderArgument("target"), new IntegerArgument("breeds", 0));

        executes(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        OfflinePlayer player = Bukkit.getOfflinePlayer((String) args[0]);
        int newBreeds = (int) args[1];

        if (player.hasPlayedBefore()) {
            int breeds = CraftAttackManager.getBreeds(player.getUniqueId());

            if (breeds == newBreeds) {
                CommandAPI.fail("The player has already that amount of breeds!");
            } else {
                breeds = newBreeds - breeds;
                CraftAttackManager.addBreeds(player.getUniqueId(), breeds);

                if (breeds <= 0) {
                    sender.sendMessage("The animal breeds of the player have been set to zero!");
                } else if (breeds == 1) {
                    sender.sendMessage("The animal breeds of the player have been set to one!");
                } else {
                    sender.sendMessage("The animal breeds of the player have been set to " + breeds + "!");
                }
            }
        } else {
            CommandAPI.fail("The player could not be found!");
        }
    }
}