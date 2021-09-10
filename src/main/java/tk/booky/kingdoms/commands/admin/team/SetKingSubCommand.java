package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:17 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static org.bukkit.Bukkit.broadcast;

public class SetKingSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetKingSubCommand(KingdomsManager manager) {
        super("king");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new MultiLiteralArgument("yellow", "green", "blue", "red"),
            new OfflinePlayerArgument("target")
        );

        withPermission("kingdoms.command.admin.team.king.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        OfflinePlayer target = (OfflinePlayer) args[1];

        if (!target.isOnline() && !target.hasPlayedBefore()) {
            manager.fail(target.getName() + " has not played before.");
        } else {
            team.king(target.getUniqueId());
            manager.config().saveConfiguration();

            broadcast(manager.prefix(text("The king of " + team.name().toLowerCase() + " has been set to " + target.getName() + ".", GREEN)));
        }
    }
}
