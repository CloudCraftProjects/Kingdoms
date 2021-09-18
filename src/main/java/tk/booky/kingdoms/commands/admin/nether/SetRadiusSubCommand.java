package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:28 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

public class SetRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetRadiusSubCommand(KingdomsManager manager) {
        super("radius");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new IntegerArgument("radius", 0)
        );

        withPermission("kingdoms.command.admin.nether.radius.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (manager.config().netherRadius() == radius) {
            manager.fail("The nether radius is already at this size!");
        } else {
            manager.config().netherRadius(radius);
            manager.message(sender, "The nether radius has been set to " + radius + "!");
        }
    }
}
