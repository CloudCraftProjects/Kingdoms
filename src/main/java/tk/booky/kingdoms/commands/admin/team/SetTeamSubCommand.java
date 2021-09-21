package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (19:20 10.09.21)

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

public class SetTeamSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetTeamSubCommand(KingdomsManager manager) {
        super("team");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new MultiLiteralArgument("yellow", "green", "blue", "red"),
            new OfflinePlayerArgument("target")
        );

        withPermission("kingdoms.command.admin.team.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        String targetTeam = (String) args[0];
        OfflinePlayer target = (OfflinePlayer) args[1];

        if (!target.isOnline() && !target.hasPlayedBefore()) {
            manager.fail(target.getName() + " has not played before.");
        } else {
            KingdomsTeam currentTeam = KingdomsTeam.byMember(target.getUniqueId());
            if (currentTeam != null) {
                currentTeam.members().remove(target.getUniqueId());
            }

            KingdomsTeam newTeam = KingdomsTeam.valueOf(targetTeam.toUpperCase());
            newTeam.members().add(target.getUniqueId());

            manager.message(sender, target.getName() + " has been added to " + newTeam.name().toLowerCase() + ".");
            manager.config().saveConfiguration();
        }
    }
}
