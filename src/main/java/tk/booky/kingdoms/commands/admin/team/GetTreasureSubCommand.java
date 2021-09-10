package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:30 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

public class GetTreasureSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public GetTreasureSubCommand(KingdomsManager manager) {
        super("treasure");
        this.manager = manager;

        withArguments(
            new LiteralArgument("get"),
            new MultiLiteralArgument("yellow", "green", "blue", "red")
        );

        withPermission("kingdoms.command.admin.team.treasure.get").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        Location location = team.treasureLocation();

        if (location != null) {
            manager.message(sender, "The treasure location of team " + team.name().toLowerCase() +
                " is currently at " + String.format("%s %s %s", location.getBlockX(),
                location.getBlockY(), location.getBlockZ()) + ".");
        } else {
            manager.fail("The treasure location has not been set yet for team " + team.name().toLowerCase() + ".");
        }
    }
}
