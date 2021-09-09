package tk.booky.kingdoms.commands.admin.pvp;
// Created by booky10 in Kingdoms (17:10 09.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.utils.KingdomsManager;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.broadcast;

public class SetPvpTimesSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetPvpTimesSubCommand(KingdomsManager manager) {
        super("times");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new IntegerArgument("start_hour", 0, 24),
            new IntegerArgument("start_minute", 0, 60),
            new IntegerArgument("end_hour", 0, 24),
            new IntegerArgument("end_minute", 0, 60)
        );

        withPermission("kingdoms.command.admin.pvp.times.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int start = ((int) args[0]) + ((int) args[1]) * 60;
        int end = ((int) args[2]) + ((int) args[3]) * 60;

        if (start > end) {
            manager.fail("The ending time is later than the starting time.");
        } else {
            manager.config().pvpTimesStart(start);
            manager.config().pvpTimesEnd(end);

            if (end == start) {
                broadcast(manager.prefix(text("Timed pvp has been disabled.", RED)));
            } else {
                manager.message(sender, "The times for timed pvp have been applied.");
            }
        }
    }
}
