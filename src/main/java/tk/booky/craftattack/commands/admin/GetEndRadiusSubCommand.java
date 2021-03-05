package tk.booky.craftattack.commands.admin;
// Created by booky10 in CraftAttack (14:33 05.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class GetEndRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetEndRadiusSubCommand() {
        super("getEndRadius");

        withPermission("craftattack.command.admin.end-radius.get");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = CraftAttackManager.getEndRadius();

        if (radius <= -1) {
            CommandAPI.fail("The end radius has not been set yet!");
        } else {
            sender.sendMessage("The end radius is currently at " + radius + " blocks!");
        }
    }
}