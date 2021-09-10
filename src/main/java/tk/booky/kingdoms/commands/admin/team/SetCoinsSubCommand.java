package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:23 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

public class SetCoinsSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetCoinsSubCommand(KingdomsManager manager) {
        super("coins");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new MultiLiteralArgument("yellow", "green", "blue", "red"),
            new IntegerArgument("amount")
        );

        withPermission("kingdoms.command.admin.team.coins.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        int amount = (int) args[1];

        team.coins(amount);
        manager.config().saveConfiguration();

        manager.message(sender, "Team " + team.name().toLowerCase() + " has now " + amount + " coins.");
    }
}
