package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (14:55 01.03.21)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import tk.booky.kingdoms.utils.KingdomsManager;

public record BlockListener(KingdomsManager manager) implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (manager.isInSpawn(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInEnd(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInNether(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (manager.isInSpawn(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInEnd(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInNether(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketFillEvent event) {
        if (manager.isInSpawn(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInEnd(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInNether(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        if (manager.isInSpawn(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInEnd(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        } else if (manager.isInNether(event.getBlock().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
