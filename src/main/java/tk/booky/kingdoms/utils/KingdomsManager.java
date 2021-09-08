package tk.booky.kingdoms.utils;
// Created by booky10 in CraftAttack (14:51 01.03.21)

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class KingdomsManager {

    private static Location endLocation, spawnLocation;
    private static boolean saving, endActivated;
    private static int endRadius, spawnRadius;

    public static void load(Plugin plugin) {
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();
        endLocation = config.getLocation("end.location", null);
        endRadius = config.getInt("end.radius", -1);
        endActivated = config.getBoolean("end.activated", false);
        spawnLocation = config.getLocation("spawn.location", null);
        spawnRadius = config.getInt("spawn.radius", -1);
    }

    public static void save(Plugin plugin, boolean async) {
        if (!saving) {
            saving = true;

            Runnable runnable = () -> {
                plugin.reloadConfig();

                FileConfiguration config = plugin.getConfig();
                config.set("end.location", endLocation);
                config.set("end.radius", endRadius);
                config.set("end.activated", endActivated);
                config.set("spawn.location", spawnLocation);
                config.set("spawn.radius", spawnRadius);

                plugin.saveConfig();
                saving = false;
            };

            if (async) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
            } else {
                runnable.run();
            }
        }
    }

    public static boolean isInSpawn(Location location, @Nullable HumanEntity entity) {
        if (spawnLocation == null || spawnRadius <= -1) {
            return false;
        } else if (entity != null && entity.getGameMode() == GameMode.CREATIVE) {
            return false;
        } else if (location.getWorld() != spawnLocation.getWorld()) {
            return false;
        } else {
            return !(location.distance(spawnLocation) > spawnRadius);
        }
    }

    public static boolean isInEnd(Location location, @Nullable HumanEntity entity) {
        if (endLocation == null || endRadius <= -1) {
            return false;
        } else if (entity != null && entity.getGameMode() == GameMode.CREATIVE) {
            return false;
        } else if (location.getWorld() != endLocation.getWorld()) {
            return false;
        } else {
            return !(location.distance(endLocation) > endRadius);
        }
    }

    public static @Nullable Location getEndLocation() {
        return endLocation;
    }

    public static int getEndRadius() {
        return endRadius;
    }

    public static @Nullable Location getSpawnLocation() {
        return spawnLocation;
    }

    public static int getSpawnRadius() {
        return spawnRadius;
    }

    public static boolean isEndActivated() {
        return endActivated;
    }

    public static void setEndLocation(Plugin plugin, Location endLocation) {
        KingdomsManager.endLocation = endLocation;
        save(plugin, true);
    }

    public static void setEndRadius(Plugin plugin, int endRadius) {
        KingdomsManager.endRadius = endRadius;
        save(plugin, true);
    }

    public static void setSpawnRadius(Plugin plugin, int spawnRadius) {
        KingdomsManager.spawnRadius = spawnRadius;
        save(plugin, true);
    }

    public static void setSpawnLocation(Plugin plugin, Location spawnLocation) {
        KingdomsManager.spawnLocation = spawnLocation;
        save(plugin, true);
    }

    public static void setEndActivated(Plugin plugin, boolean activate) {
        endActivated = activate;
        save(plugin, true);
    }
}
