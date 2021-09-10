package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:19 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

public class GetTeamSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetTeamSubCommand(KingdomsManager manager) {
        super("team");
        this.manager = manager;

        withArguments(
            new LiteralArgument("get"),
            new OfflinePlayerArgument("target")
        );

        withPermission("kingdoms.command.admin.team.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        OfflinePlayer target = (OfflinePlayer) args[0];

        if (!target.isOnline() && !target.hasPlayedBefore()) {
            manager.fail(target.getName() + " has not played before.");
        } else {
            for (KingdomsTeam team : KingdomsTeam.values()) {
                if (team.members().contains(target.getUniqueId())) {
                    manager.message(sender, target.getName() + " is in team " + team.name().toLowerCase() + ".");
                }
            }
        }
    }
}
