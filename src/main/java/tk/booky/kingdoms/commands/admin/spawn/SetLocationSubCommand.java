package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in CraftAttack (15:52 01.03.21)

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

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class SetLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetLocationSubCommand() {
        super("location");

        withPermission("kingdoms.command.admin.spawn.location.set");
        withArguments(new LiteralArgument("set"), new LocationArgument("location", LocationType.PRECISE_POSITION), new AngleArgument("yaw"));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = (Location) args[0];
        location.setYaw((float) args[1]);

        if (location.equals(KingdomsManager.getSpawnLocation())) {
            fail("The spawn location is already at the exact same position!");
        } else {
            KingdomsManager.setSpawnLocation(location);
            KingdomsManager.save(true);

            message(sender, "The spawn location has been set!");
        }
    }
}
