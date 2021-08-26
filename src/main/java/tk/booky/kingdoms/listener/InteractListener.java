package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (15:02 01.03.21)

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.booky.kingdoms.utils.KingdomsManager;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Location location = event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : event.getPlayer().getLocation();
        if (KingdomsManager.isInSpawn(location, event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (KingdomsManager.isInSpawn(event.getRightClicked().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
