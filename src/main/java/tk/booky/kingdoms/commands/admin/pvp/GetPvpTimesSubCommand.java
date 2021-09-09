package tk.booky.kingdoms.commands.admin.pvp;
// Created by booky10 in Kingdoms (17:11 09.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.text.DecimalFormat;

public class GetPvpTimesSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetPvpTimesSubCommand(KingdomsManager manager) {
        super("times");
        this.manager = manager;

        withArguments(new LiteralArgument("get"));

        withPermission("kingdoms.command.admin.pvp.times.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int start = manager.config().pvpTimesStart();
        int end = manager.config().pvpTimesEnd();

        if (end == start) {
            manager.fail("Timed pvp is currently disabled.");
        } else {
            DecimalFormat format = new DecimalFormat("00");
            manager.message(sender, String.format(
                "Currently timed pvp will be activated from %s:%s to %s:%s.",
                format.format(start % 60), format.format(start / 60),
                format.format(end % 60), format.format(end / 60)
            ));
        }
    }
}
