package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:27 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

public class GetRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetRadiusSubCommand(KingdomsManager manager) {
        super("radius");
        this.manager = manager;

        withArguments(new LiteralArgument("get"));

        withPermission("kingdoms.command.admin.nether.radius.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = manager.config().netherRadius();

        if (radius <= -1) {
            manager.fail("The nether radius has not been set yet!");
        } else {
            manager.message(sender, "The nether radius is currently at " + radius + " blocks!");
        }
    }
}
