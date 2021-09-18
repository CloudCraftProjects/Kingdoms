package tk.booky.kingdoms.commands.admin.nether;
// Created by booky10 in Kingdoms (14:25 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

public class ActivationSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public ActivationSubCommand(KingdomsManager manager) {
        super("activate");
        this.manager = manager;

        withArguments(new BooleanArgument("active"));

        withPermission("kingdoms.command.admin.nether.activate").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        boolean activate = (boolean) args[0];

        if (manager.config().netherActivated() == activate) {
            manager.fail("The nether is already " + (activate ? "" : "de") + "activated!");
        } else {
            manager.config().netherActivated(activate);
            manager.message(sender, "The nether has been " + (activate ? "" : "de") + "activated!");
        }
    }
}
