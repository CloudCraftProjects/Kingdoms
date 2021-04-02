package tk.booky.kingdoms.commands.admin.breed;
// Created by booky10 in CraftAttack (11:57 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class GetBreedsSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetBreedsSubCommand() {
        super("get");

        withPermission("kingdoms.command.admin.breed.get");
        withArguments(new ScoreHolderArgument("target"));

        executes(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        OfflinePlayer player = Bukkit.getOfflinePlayer((String) args[0]);

        if (player.hasPlayedBefore()) {
            int breeds = KingdomsManager.getBreeds(player.getUniqueId());
            if (breeds <= 0) {
                CommandAPI.fail("The player hasn't breed any animals yet!");
            } else if (breeds == 1) {
                sender.sendMessage("The player has currently breed one animal!");
            } else {
                sender.sendMessage("The player has currently breed " + breeds + " animals!");
            }
        } else {
            CommandAPI.fail("The player could not be found!");
        }
    }
}