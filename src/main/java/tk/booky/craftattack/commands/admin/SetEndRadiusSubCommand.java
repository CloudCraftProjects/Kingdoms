package tk.booky.craftattack.commands.admin;
// Created by booky10 in CraftAttack (14:31 05.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

public class SetEndRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetEndRadiusSubCommand() {
        super("setEndRadius");

        withPermission("craftattack.command.admin.end-radius.set");
        withArguments(new IntegerArgument("radius", 0));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (CraftAttackManager.getEndRadius() == radius) {
            CommandAPI.fail("The end radius is already at this size!");
        } else {
            CraftAttackManager.setEndRadius(radius);
            CraftAttackManager.save(true);

            sender.sendMessage("The end radius has been set to " + radius + "!");
        }
    }
}