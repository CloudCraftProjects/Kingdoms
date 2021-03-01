package tk.booky.craftattack.commands.admin;
// Created by booky10 in CraftAttack (15:04 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class SetSpawnRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetSpawnRadiusSubCommand() {
        super("setSpawnRadius");

        withPermission("craftattack.command.admin.spawn-radius.set");
        withArguments(new IntegerArgument("radius", 0));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (CraftAttackManager.getSpawnRadius() == radius) {
            CommandAPI.fail("The spawn radius is already at this size!");
        } else {
            CraftAttackManager.setSpawnRadius(radius);
            CraftAttackManager.save(true);

            sender.sendMessage("The spawn radius has been set to " + radius + "!");
        }
    }
}