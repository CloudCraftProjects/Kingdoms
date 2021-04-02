package tk.booky.kingdoms.commands.admin.breed;
// Created by booky10 in CraftAttack (12:13 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

import java.util.Map;
import java.util.UUID;

public class BestBreederSubCommand extends CommandAPICommand implements CommandExecutor {

    public BestBreederSubCommand() {
        super("best");

        withPermission("kingdoms.command.admin.breed.best");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        if (KingdomsManager.getBreeds().size() <= 0) {
            CommandAPI.fail("There are currently no breeds!");
        } else {
            Map.Entry<UUID, Integer> highest = KingdomsManager.getHighestBreedEntry();
            OfflinePlayer player = Bukkit.getOfflinePlayer(highest.getKey());

            if (!player.hasPlayedBefore()) {
                CommandAPI.fail("The top player hasn't played before!");
            } else {
                sender.sendMessage("Currently player " + player.getName() + " is the highest breeder, with an amount of " + highest.getValue() + "!");
            }
        }
    }
}