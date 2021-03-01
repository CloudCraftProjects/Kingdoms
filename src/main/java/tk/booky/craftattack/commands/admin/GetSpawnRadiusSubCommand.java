package tk.booky.craftattack.commands.admin;
// Created by booky10 in CraftAttack (15:10 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class GetSpawnRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetSpawnRadiusSubCommand() {
        super("getSpawnRadius");

        withPermission("craftattack.command.admin.spawn-radius.get");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = CraftAttackManager.getSpawnRadius();

        if (radius <= -1) {
            CommandAPI.fail("The spawn radius has not been set yet!");
        } else {
            sender.sendMessage("The spawn radius is currently at " + radius + " blocks!");
        }
    }
}