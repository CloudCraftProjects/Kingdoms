package tk.booky.kingdoms.commands.admin.end;
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
import org.bukkit.plugin.Plugin;
import tk.booky.kingdoms.utils.KingdomsManager;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class SetLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    private final Plugin plugin;

    public SetLocationSubCommand(Plugin plugin) {
        super("location");
        this.plugin = plugin;

        withPermission("kingdoms.command.admin.end.location.set");
        withArguments(new LiteralArgument("set"), new LocationArgument("location", LocationType.PRECISE_POSITION), new AngleArgument("yaw"));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = (Location) args[0];
        location.setYaw((float) args[1]);

        if (location.equals(KingdomsManager.getEndLocation())) {
            fail("The end location is already at the exact same position!");
        } else {
            KingdomsManager.setEndLocation(plugin, location);
            message(sender, "The end location has been set!");
        }
    }
}
