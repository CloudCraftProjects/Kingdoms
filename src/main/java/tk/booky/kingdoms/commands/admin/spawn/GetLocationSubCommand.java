package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in CraftAttack (15:51 01.03.21)

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

        withPermission("kingdoms.command.admin.spawn.location.get");
        withArguments(new LiteralArgument("get"));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Location location = manager.config().spawnLocation();

        if (location == null) {
            manager.fail("The spawn location has not been set yet!");
        } else {
            manager.message(sender, String.format(
                "The spawn location is currently at %s %s %s %s %s in %s!",
                location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(),
                location.getWorld().getName()
            ));
        }
    }
}
