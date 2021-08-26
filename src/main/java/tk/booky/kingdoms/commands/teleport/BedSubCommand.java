package tk.booky.kingdoms.commands.teleport;
// Created by booky10 in CraftAttack (14:45 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;
import static tk.booky.kingdoms.utils.KingdomsUtilities.overworld;

public class BedSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private static final HashMap<UUID, Long> LAST_USED = new HashMap<>();

    public BedSubCommand() {
        super("bed");
        withPermission("kingdoms.command.teleport.bed").executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (!sender.hasPermission("kingdoms.bypass.cooldown.bed") && LAST_USED.getOrDefault(sender.getUniqueId(), 0L) + 60000 > System.currentTimeMillis()) {
            long seconds = (LAST_USED.get(sender.getUniqueId()) + 60000 - System.currentTimeMillis()) / 1000 + 1;
            fail("You still have to wait " + seconds + " more seconds before you can use this command again!");
        } else {
            LAST_USED.put(sender.getUniqueId(), System.currentTimeMillis());
            Location location = sender.getBedSpawnLocation();

            if (location == null) {
                sender.teleportAsync(overworld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                message(sender, "Your bed was broken and you have been send to the world spawn!");
            } else {
                sender.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                message(sender, "You have been brought back to your bed!");
            }
        }
    }
}
