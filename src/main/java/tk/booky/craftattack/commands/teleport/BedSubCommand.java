package tk.booky.craftattack.commands.teleport;
// Created by booky10 in CraftAttack (14:45 01.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class BedSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private static final HashMap<UUID, Long> lastUse = new HashMap<>();

    public BedSubCommand() {
        super("bed");

        withPermission("craftattack.command.teleport.bed");

        executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (!sender.hasPermission("craftattack.bypass.cooldown.bed") && lastUse.getOrDefault(sender.getUniqueId(), 0L) + 60000 > System.currentTimeMillis()) {
            long seconds = (lastUse.get(sender.getUniqueId()) + 60000 - System.currentTimeMillis()) / 1000 + 1;
            CommandAPI.fail("You still have to wait " + seconds + " more seconds before you can use this command again!");
        } else {
            lastUse.put(sender.getUniqueId(), System.currentTimeMillis());
            Location location = sender.getBedSpawnLocation();

            if (location == null) {
                sender.teleportAsync(Bukkit.getWorlds().get(0).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                sender.sendMessage("Your bed was broken and you have been send to the world spawn!");
            } else {
                sender.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                sender.sendMessage("You have been brought back to your bed!");
            }
        }
    }
}