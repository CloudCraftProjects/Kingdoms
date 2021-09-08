package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in CraftAttack (15:04 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import tk.booky.kingdoms.utils.KingdomsManager;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class SetRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    private final Plugin plugin;

    public SetRadiusSubCommand(Plugin plugin) {
        super("radius");
        this.plugin = plugin;

        withPermission("kingdoms.command.admin.end.radius.set");
        withArguments(new LiteralArgument("set"), new IntegerArgument("radius", 0));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = (int) args[0];

        if (KingdomsManager.getEndRadius() == radius) {
            fail("The end radius is already at this size!");
        } else {
            KingdomsManager.setEndRadius(plugin, radius);
            message(sender, "The end radius has been set to " + radius + "!");
        }
    }
}
