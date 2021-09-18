package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:27 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AngleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

public class SetLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetLocationSubCommand(KingdomsManager manager) {
        super("location");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new LocationArgument("location", LocationType.PRECISE_POSITION),
            new AngleArgument("yaw")
        );

        withPermission("kingdoms.command.admin.nether.location.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = (Location) args[0];
        location.setYaw((float) args[1]);

        if (location.equals(manager.config().netherLocation())) {
            manager.fail("The nether location is already at the exact same position!");
        } else {
            manager.config().netherLocation(location);
            manager.message(sender, "The nether location has been set!");
        }
    }
}
