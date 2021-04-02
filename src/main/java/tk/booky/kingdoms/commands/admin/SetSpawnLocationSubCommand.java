package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:52 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class SetSpawnLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetSpawnLocationSubCommand() {
        super("setSpawnLocation");

        withPermission("kingdoms.command.admin.spawn.set");
        withArguments(new LocationArgument("location", LocationType.PRECISE_POSITION));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = (Location) args[0];

        if (KingdomsManager.getSpawnLocation() == location) {
            CommandAPI.fail("The spawn location is already at the exact same position!");
        } else {
            KingdomsManager.setSpawnLocation(location);
            KingdomsManager.save(true);

            sender.sendMessage("The spawn location has been set!");
        }
    }
}