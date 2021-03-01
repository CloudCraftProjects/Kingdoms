package tk.booky.craftattack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.craftattack.listener.BlockListener;
import tk.booky.craftattack.listener.BoatListener;
import tk.booky.craftattack.listener.InteractListener;
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

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
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
}
