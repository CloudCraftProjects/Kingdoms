package tk.booky.kingdoms.commands.admin.end;
// Created by booky10 in CraftAttack (15:10 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class GetRadiusSubCommand extends CommandAPICommand implements CommandExecutor {

    public GetRadiusSubCommand() {
        super("radius");

        withPermission("kingdoms.command.admin.end.radius.get");
        withArguments(new LiteralArgument("get"));
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int radius = KingdomsManager.getEndRadius();

        if (radius <= -1) {
            fail("The end radius has not been set yet!");
        } else {
            message(sender, "The end radius is currently at " + radius + " blocks!");
        }
    }
}
