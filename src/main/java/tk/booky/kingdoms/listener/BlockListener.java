package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (14:55 01.03.21)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import tk.booky.kingdoms.manager.KingdomsManager;

public class BlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (KingdomsManager.isInSpawn(event.getPlayer()) || KingdomsManager.isInEnd(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (KingdomsManager.isInSpawn(event.getPlayer()) || KingdomsManager.isInEnd(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onBucket(PlayerBucketFillEvent event) {
        if (KingdomsManager.isInSpawn(event.getPlayer()) || KingdomsManager.isInEnd(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        if (KingdomsManager.isInSpawn(event.getPlayer()) || KingdomsManager.isInEnd(event.getPlayer())) event.setCancelled(true);
    }
}