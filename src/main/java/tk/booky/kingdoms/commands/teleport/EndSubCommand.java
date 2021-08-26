package tk.booky.kingdoms.commands.teleport;
// Created by booky10 in CraftAttack (14:54 03.01.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.util.HashMap;
import java.util.UUID;

import static tk.booky.kingdoms.utils.KingdomsUtilities.fail;
import static tk.booky.kingdoms.utils.KingdomsUtilities.message;

public class EndSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private static final HashMap<UUID, Long> LAST_USE = new HashMap<>();

    public EndSubCommand() {
        super("end");
        withPermission("kingdoms.command.teleport.end").executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (!sender.hasPermission("kingdoms.bypass.cooldown.end") && LAST_USE.getOrDefault(sender.getUniqueId(), 0L) + 60000 > System.currentTimeMillis()) {
            long seconds = (LAST_USE.get(sender.getUniqueId()) + 60000 - System.currentTimeMillis()) / 1000 + 1;
            fail("You still have to wait " + seconds + " more seconds before you can use this command again!");
        } else {
            LAST_USE.put(sender.getUniqueId(), System.currentTimeMillis());

            if (KingdomsManager.getEndLocation() == null) {
                fail("The end location has not been set yet!");
            } else {
                sender.teleportAsync(KingdomsManager.getEndLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                message(sender, "You have been brought to the end location!");
            }
        }
    }
}
