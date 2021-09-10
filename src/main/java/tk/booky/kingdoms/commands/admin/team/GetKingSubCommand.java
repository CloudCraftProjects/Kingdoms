package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:19 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class GetKingSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetKingSubCommand(KingdomsManager manager) {
        super("king");
        this.manager = manager;

        withArguments(
            new LiteralArgument("get"),
            new MultiLiteralArgument("yellow", "green", "blue", "red")
        );

        withPermission("kingdoms.command.admin.team.king.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        manager.message(sender, "The king of " + team.name().toLowerCase() + " is " + getOfflinePlayer(team.king()).getName() + ".");
    }
}
