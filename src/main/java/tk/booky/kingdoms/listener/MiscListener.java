package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (15:02 01.03.21)

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.*;
import tk.booky.kingdoms.manager.KingdomsManager;

public class MiscListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.BOAT)) {
            if (KingdomsManager.isInSpawn(event.getEntity().getLocation(), null) || KingdomsManager.isInEnd(event.getEntity().getLocation(), null)) {
                if (!(event instanceof EntityDamageByEntityEvent) || !((EntityDamageByEntityEvent) event).getDamager().getType().equals(EntityType.PLAYER) || !((Player) ((EntityDamageByEntityEvent) event).getDamager()).getGameMode().equals(GameMode.CREATIVE)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (KingdomsManager.isInSpawn(event.getEntity().getLocation(), null)) {
            switch (event.getSpawnReason()) {
                case CUSTOM:
                case CURED:
                case EGG:
                case INFECTION:
                case ENDER_PEARL:
                case OCELOT_BABY:
                case SLIME_SPLIT:
                case SPAWNER_EGG:
                case DEFAULT:
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        if (event.getBreeder() == null) return;
        KingdomsManager.addBreeds(event.getBreeder().getUniqueId(), 1);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.CREEPER) || KingdomsManager.isInSpawn(event.getLocation(), null)) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        if (KingdomsManager.isInSpawn(event.getBlock().getLocation(), null)) event.setNewCurrent(0);
    }
}