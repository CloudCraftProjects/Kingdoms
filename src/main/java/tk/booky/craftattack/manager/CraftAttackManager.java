package tk.booky.craftattack.manager;
// Created by booky10 in CraftAttack (14:51 01.03.21)

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Nullable;
import tk.booky.craftattack.CraftAttackMain;

public final class CraftAttackManager {

    private static Location endLocation, spawnLocation;
    private static int spawnRadius;

    private static final CraftAttackMain plugin = CraftAttackMain.main;
    private static boolean saving;

    public static void load() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        endLocation = config.getLocation("end.location", null);
        spawnLocation = config.getLocation("spawn.location", null);
        spawnRadius = config.getInt("spawn.radius", -1);
    }

    public static void save(boolean async) {
        if (saving) return;
        saving = true;

        Runnable runnable = () -> {
            plugin.reloadConfig();
            FileConfiguration config = plugin.getConfig();

            config.set("end.location", endLocation);
            config.set("spawn.location", spawnLocation);
            config.set("spawn.radius", spawnRadius);

            plugin.saveConfig();
            saving = false;
        };

        if (async) Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        else runnable.run();
    }

    public static Location getEndLocation() {
        return endLocation;
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static int getSpawnRadius() {
        return spawnRadius;
    }

    public static boolean isInSpawn(HumanEntity entity) {
        return isInSpawn(entity.getLocation(), entity);
    }

    public static boolean isInSpawn(Location location, @Nullable HumanEntity entity) {
        if (getSpawnLocation() == null || getSpawnRadius() <= -1 || (entity != null && entity.getGameMode().equals(GameMode.CREATIVE))) return false;
        else if (location.getWorld().getUID() != getSpawnLocation().getWorld().getUID()) return false;
        else return !(location.distance(getSpawnLocation()) > getSpawnRadius());
    }

    public static void setSpawnRadius(int spawnRadius) {
        CraftAttackManager.spawnRadius = spawnRadius;
    }

    public static void setSpawnLocation(Location spawnLocation) {
        CraftAttackManager.spawnLocation = spawnLocation;
    }

    public static void setEndLocation(Location endLocation) {
        CraftAttackManager.endLocation = endLocation;
    }
}