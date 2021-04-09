package tk.booky.kingdoms.commands.admin;
// Created by booky10 in Kingdoms (12:29 09.04.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class ActivationEndSubCommand extends CommandAPICommand implements CommandExecutor {

    public ActivationEndSubCommand() {
        super("activateEnd");

        withPermission("kingdoms.command.admin.end.activation");
        withArguments(new BooleanArgument("activate"));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        boolean activate = (boolean) args[0];

        if (KingdomsManager.isEndActivated() == activate) {
            CommandAPI.fail("The end is already activated!");
        } else {
            KingdomsManager.setEndActivated(activate);
            sender.sendMessage("The end has been " + (activate ? "" : "de") + "activated!");
        }
    }
}