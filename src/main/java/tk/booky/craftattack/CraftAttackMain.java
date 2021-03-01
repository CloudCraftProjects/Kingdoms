package tk.booky.craftattack;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.craftattack.listener.BlockListener;
import tk.booky.craftattack.listener.BoatListener;
import tk.booky.craftattack.listener.MiscListener;
import tk.booky.craftattack.manager.CraftAttackManager;

import java.util.Arrays;
import java.util.List;

public final class CraftAttackMain extends JavaPlugin implements Listener {

    public static CraftAttackMain main;

    public static Location SPAWN_LOCATION;
    public static final List<Material> ALLOWED_BLOCKS = Arrays.asList(Material.ENDER_CHEST, Material.CRAFTING_TABLE);

    @Override
    public void onEnable() {
        main = this;

        saveDefaultConfig();
        CraftAttackManager.load();

        Bukkit.getPluginManager().registerEvents(new MiscListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new BoatListener(), this);

        Bukkit.getPluginManager().registerEvents(this, this);
        SPAWN_LOCATION = new Location(Bukkit.getWorlds().get(0), -119, 65, 134, -90, 0);
    }

    @Override
    public void onDisable() {
        CraftAttackManager.save(false);
    }

    private static boolean isInDistance(Location location1, Location location2, int distance) {
        if (location1.getWorld().getUID() != location2.getWorld().getUID()) return false;
        else return !(location1.distance(location2) > distance);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isInDistance(event.getPlayer().getLocation(), SPAWN_LOCATION, 76)) return;
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        switch (event.getAction()) {
            case PHYSICAL:
                break;
            case RIGHT_CLICK_BLOCK:
                if (event.getClickedBlock() != null && ALLOWED_BLOCKS.contains(event.getClickedBlock().getType())) return;
                if (Tag.ITEMS_BOATS.isTagged(event.getMaterial()) && event.getClickedBlock() != null) {
                    if (event.getClickedBlock().getType().isInteractable() && event.getPlayer().isSneaking()) {
                        return;
                    } else if (event.getClickedBlock().getType().isInteractable()) {
                        break;
                    } else {
                        return;
                    }
                } else {
                    break;
                }
            default:
                return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!isInDistance(event.getPlayer().getLocation(), SPAWN_LOCATION, 76)) return;
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        event.setCancelled(true);
    }
}
