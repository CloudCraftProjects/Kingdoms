package tk.booky.craftattack.commands.teleport;
// Created by booky10 in CraftAttack (14:55 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import tk.booky.craftattack.manager.CraftAttackManager;

import java.util.HashMap;
import java.util.UUID;

public class SpawnSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private static final HashMap<UUID, Long> lastUse = new HashMap<>();

    public SpawnSubCommand() {
        super("spawn");

        withPermission("craftattack.command.teleport.spawn");

        executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (!sender.hasPermission("craftattack.bypass.cooldown.spawn") && lastUse.getOrDefault(sender.getUniqueId(), 0L) + 60000 > System.currentTimeMillis()) {
            long seconds = (lastUse.get(sender.getUniqueId()) + 60000 - System.currentTimeMillis()) / 1000 + 1;
            CommandAPI.fail("You still have to wait " + seconds + " more seconds before you can use this command again!");
        } else {
            lastUse.put(sender.getUniqueId(), System.currentTimeMillis());

            if (CraftAttackManager.getSpawnLocation() == null) {
                CommandAPI.fail("The spawn location has not been set yet!");
            } else {
                sender.teleportAsync(CraftAttackManager.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                sender.sendMessage("You have been brought to the spawn location!");
            }
        }
    }
}