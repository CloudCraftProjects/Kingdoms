package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (14:33 05.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class GetEndRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetEndRadiusSubCommand() {
        super("getEndRadius");

        withPermission("kingdoms.command.admin.end-radius.get");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = KingdomsManager.getEndRadius();

        if (radius <= -1) {
            CommandAPI.fail("The end radius has not been set yet!");
        } else {
            sender.sendMessage("The end radius is currently at " + radius + " blocks!");
        }
    }
}