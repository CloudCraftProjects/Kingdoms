package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in CraftAttack (15:51 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class GetLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetLocationSubCommand() {
        super("location");

        withPermission("kingdoms.command.admin.end.location.get");
        withArguments(new LiteralArgument("get"));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = KingdomsManager.getEndLocation();

        if (location == null) {
            fail("The end location has not been set yet!");
        } else {
            message(sender, String.format(
                "The end location is currently at %s %s %s %s %s in %s!",
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(),
                location.getWorld().getName()
            ));
        }
    }
}
