package tk.booky.kingdoms.utils;
// Created by booky10 in CraftAttack (14:51 01.03.21)

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Nullable;

import static tk.booky.kingdoms.KingdomsMain.main;

public final class KingdomsManager {

    private static Location endLocation, spawnLocation;
    private static int endRadius, spawnRadius;

    private static boolean saving, endActivated;

    public static void load() {
        main.reloadConfig();
        FileConfiguration config = main.getConfig();

        endLocation = config.getLocation("end.location", null);
        endRadius = config.getInt("end.radius", -1);
        endActivated = config.getBoolean("end.activated", false);
        spawnLocation = config.getLocation("spawn.location", null);
        spawnRadius = config.getInt("spawn.radius", -1);
    }

    public static void save(boolean async) {
        if (!saving) {
            saving = true;

            Runnable runnable = () -> {
                main.reloadConfig();
                FileConfiguration config = main.getConfig();

                config.set("end.location", endLocation);
                config.set("end.radius", endRadius);
                config.set("end.activated", endActivated);
                config.set("spawn.location", spawnLocation);
                config.set("spawn.radius", spawnRadius);

                main.saveConfig();
                saving = false;
            };

            if (async) {
                Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
            } else {
                runnable.run();
            }
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

    public static void setEndLocation(Location endLocation) {
        KingdomsManager.endLocation = endLocation;
    }

    public static void setEndRadius(int endRadius) {
        KingdomsManager.endRadius = endRadius;
    }

    public static void setSpawnRadius(int spawnRadius) {
        KingdomsManager.spawnRadius = spawnRadius;
    }

    public static void setSpawnLocation(Location spawnLocation) {
        KingdomsManager.spawnLocation = spawnLocation;
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

    public static boolean isEndActivated() {
        return endActivated;
    }

    public static void setEndActivated(boolean activate) {
        endActivated = activate;
        save(true);
    }
}
