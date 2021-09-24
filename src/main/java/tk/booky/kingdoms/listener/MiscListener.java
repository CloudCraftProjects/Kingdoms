package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (15:02 01.03.21)

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import tk.booky.kingdoms.utils.KingdomsManager;
import tk.booky.kingdoms.utils.TeamRenderer;

public class MiscListener implements Listener {

    private final TeamRenderer chatRenderer = new TeamRenderer();
    private final KingdomsManager manager;

    public MiscListener(KingdomsManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (manager.isInSpawn(event.getEntity().getLocation(), null)) {
            event.setCancelled(true);
        } else if (manager.isInEnd(event.getEntity().getLocation(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (manager.isInSpawn(event.getEntity().getLocation(), null)) {
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
    public void onExplosion(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.CREEPER)) {
            event.blockList().clear();
        } else if (manager.isInSpawn(event.getLocation(), null)) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (manager.isInSpawn(event.getEntity().getLocation(), null)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent event) {
        if (manager.isInSpawn(event.getBlock().getLocation(), null)) {
            event.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onEndPortal(BlockPlaceEvent event) {
        if (event.getItemInHand().getType().equals(Material.ENDER_EYE)) {
            if (event.getBlock().getType().equals(Material.END_PORTAL_FRAME)) {
                if (!manager.config().endActivated()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onNetherPortal(PortalCreateEvent event) {
        if (event.getReason() == CreateReason.FIRE && !manager.config().netherActivated()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(chatRenderer);
    }
}
