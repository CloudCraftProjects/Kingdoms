package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:22 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

public class GetCoinsSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetCoinsSubCommand(KingdomsManager manager) {
        super("coins");
        this.manager = manager;

        withArguments(
            new LiteralArgument("get"),
            new MultiLiteralArgument("yellow", "green", "blue", "red")
        );

        withPermission("kingdoms.command.admin.team.coins.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        manager.message(sender, "Team " + team.name().toLowerCase() + " has currently " + team.coins() + " coins.");
    }
}
