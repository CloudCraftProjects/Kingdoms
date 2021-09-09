package tk.booky.kingdoms.commands.teleport;
// Created by booky10 in CraftAttack (14:55 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.util.HashMap;
import java.util.UUID;

public class SpawnSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private static final HashMap<UUID, Long> lastUse = new HashMap<>();
    private final KingdomsManager manager;

    public SpawnSubCommand(KingdomsManager manager) {
        super("spawn");
        this.manager = manager;

        withPermission("kingdoms.command.teleport.spawn").executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (!sender.hasPermission("kingdoms.bypass.cooldown.spawn") && lastUse.getOrDefault(sender.getUniqueId(), 0L) + 60000 > System.currentTimeMillis()) {
            long seconds = (lastUse.get(sender.getUniqueId()) + 60000 - System.currentTimeMillis()) / 1000 + 1;
            manager.fail("You still have to wait " + seconds + " more seconds before you can use this command again!");
        } else {
            lastUse.put(sender.getUniqueId(), System.currentTimeMillis());

            if (manager.config().spawnLocation() == null) {
                manager.fail("The spawn location has not been set yet!");
            } else {
                sender.teleportAsync(manager.config().spawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                manager.message(sender, "You have been brought to the spawn location!");
            }
        }
    }
}
