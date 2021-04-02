package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:51 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class GetSpawnLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetSpawnLocationSubCommand() {
        super("getSpawnLocation");

        withPermission("kingdoms.command.admin.spawn.get");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = KingdomsManager.getSpawnLocation();

        if (location == null) {
            CommandAPI.fail("The spawn location has not been set yet!");
        } else {
            sender.sendMessage(String.format("The spawn location is currently at %s %s %s in %s!",
                    location.getX(), location.getY(), location.getZ(), location.getWorld().getName()));
        }
    }
}