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
            new IntegerArgument("start", 0, 24),
            new IntegerArgument("end", 0, 24)
        );

        withPermission("kingdoms.command.admin.pvp.times.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        int start = (int) args[0], end = (int) args[1];

        if (start > end) {
            manager.fail("The starting hour is later than the ending hour.");
        } else {
            manager.config().pvpTimesStart(start);
            manager.config().pvpTimesEnd(end);

            if (end == start) {
                broadcast(manager.prefix(text("Timed pvp has been disabled.", RED)));
            } else {
                manager.message(sender, "The ending and starting hours have been applied.");
            }
        }
    }
}
