package tk.booky.kingdoms.commands.admin;
// Created by booky10 in CraftAttack (15:04 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.manager.KingdomsManager;

public class SetSpawnRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public SetSpawnRadiusSubCommand() {
        super("setSpawnRadius");

        withPermission("kingdoms.command.admin.spawn-radius.set");
        withArguments(new IntegerArgument("radius", 0));

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (KingdomsManager.getSpawnRadius() == radius) {
            CommandAPI.fail("The spawn radius is already at this size!");
        } else {
            KingdomsManager.setSpawnRadius(radius);
            KingdomsManager.save(true);

            sender.sendMessage("The spawn radius has been set to " + radius + "!");
        }
    }
}