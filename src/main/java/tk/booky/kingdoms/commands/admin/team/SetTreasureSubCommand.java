package tk.booky.kingdoms.commands.admin.team;
// Created by booky10 in Kingdoms (22:23 10.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

public class SetTreasureSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public SetTreasureSubCommand(KingdomsManager manager) {
        super("treasure");
        this.manager = manager;

        withArguments(
            new LiteralArgument("set"),
            new MultiLiteralArgument("yellow", "green", "blue", "red"),
            new LocationArgument("location", LocationType.BLOCK_POSITION)
        );

        withPermission("kingdoms.command.admin.team.treasure.set").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam team = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        Location location = (Location) args[1];

        team.treasureLocation(location);
        manager.config().saveConfiguration();

        manager.message(sender, "The treasure location of team " + team.name().toLowerCase() + " is now at " +
            String.format("%s %s %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()) + ".");
    }
}
