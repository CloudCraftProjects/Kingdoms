package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in Kingdoms (12:29 09.04.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class ActivationSubCommand extends CommandAPICommand implements CommandExecutor {

    public ActivationSubCommand() {
        super("activate");

        withPermission("kingdoms.command.admin.end.activate");
        withArguments(new BooleanArgument("active"));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        boolean activate = (boolean) args[0];

        if (KingdomsManager.isEndActivated() == activate) {
            fail("The end is already " + (activate ? "" : "de") + "activated!");
        } else {
            KingdomsManager.setEndActivated(activate);
            message(sender, "The end has been " + (activate ? "" : "de") + "activated!");
        }
    }
}
