package tk.booky.kingdoms.commands.admin.spawn;
// Created by booky10 in CraftAttack (15:04 01.03.21)

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

        withPermission("kingdoms.command.admin.spawn.radius.set");
        withArguments(new LiteralArgument("set"), new IntegerArgument("radius", 0));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (manager.config().spawnRadius() == radius) {
            manager.fail("The spawn radius is already at this size!");
        } else {
            manager.config().spawnRadius(radius);
            manager.message(sender, "The spawn radius has been set to " + radius + "!");
        }
    }
}
