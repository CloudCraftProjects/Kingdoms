package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:26 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

public class GetLocationSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetLocationSubCommand(KingdomsManager manager) {
        super("location");
        this.manager = manager;

        withArguments(new LiteralArgument("get"));

        withPermission("kingdoms.command.admin.nether.location.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = manager.config().netherLocation();

        if (location == null) {
            manager.fail("The nether location has not been set yet!");
        } else {
            manager.message(sender, String.format(
                "The nether location is currently at %s %s %s %s %s in %s!",
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(),
                location.getWorld().getName()
            ));
        }
    }
}
