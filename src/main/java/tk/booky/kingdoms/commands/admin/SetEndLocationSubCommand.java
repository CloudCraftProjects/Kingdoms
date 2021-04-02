package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:45 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class SetEndLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetEndLocationSubCommand() {
        super("setEndLocation");

        withPermission("kingdoms.command.admin.end.set");
        withArguments(new LocationArgument("location", LocationType.PRECISE_POSITION));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = (Location) args[0];

        if (KingdomsManager.getEndLocation() == location) {
            CommandAPI.fail("The end location is already at the exact same position!");
        } else {
            KingdomsManager.setEndLocation(location);
            KingdomsManager.save(true);

            sender.sendMessage("The end location has been set!");
        }
    }
}