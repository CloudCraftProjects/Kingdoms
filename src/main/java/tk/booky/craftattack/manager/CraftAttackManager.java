package tk.booky.craftattack.manager;
// Created by booky10 in CraftAttack (14:51 01.03.21)

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tk.booky.craftattack.CraftAttackMain;

import java.util.*;

public final class CraftAttackManager {

    private static Location endLocation, spawnLocation;
    private static int endRadius, spawnRadius;
    private static final Map<UUID, Integer> breeds = new HashMap<>();

    private static final CraftAttackMain plugin = CraftAttackMain.main;
    private static boolean saving;

    public static void load() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        endLocation = config.getLocation("end.location", null);
        endRadius = config.getInt("end.radius", -1);
        spawnLocation = config.getLocation("spawn.location", null);
        spawnRadius = config.getInt("spawn.radius", -1);

        ConfigurationSection section = config.getConfigurationSection("breeds");
        if (section != null) section.getKeys(false).forEach(key -> breeds.put(UUID.fromString(key), section.getInt(key)));
    }

    public static void save(boolean async) {
        if (saving) return;
        saving = true;

        Runnable runnable = () -> {
            plugin.reloadConfig();
            FileConfiguration config = plugin.getConfig();

            config.set("end.location", endLocation);
            config.set("end.radius", endRadius);
            config.set("spawn.location", spawnLocation);
            config.set("spawn.radius", spawnRadius);

            breeds.forEach((uuid, breeds) -> config.set("breeds." + uuid, breeds));

            plugin.saveConfig();
            saving = false;
        };

        if (async) Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        else runnable.run();
    }

    public static Location getEndLocation() {
        return endLocation;
    }

    public static int getEndRadius() {
        return endRadius;
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static int getSpawnRadius() {
        return spawnRadius;
    }

    public static void setEndLocation(Location endLocation) {
        CraftAttackManager.endLocation = endLocation;
    }

    public static void setEndRadius(int endRadius) {
        CraftAttackManager.endRadius = endRadius;
    }

    public static void setSpawnRadius(int spawnRadius) {
        CraftAttackManager.spawnRadius = spawnRadius;
    }

    public static void setSpawnLocation(Location spawnLocation) {
        CraftAttackManager.spawnLocation = spawnLocation;
    }

    public static void giveBoat(HumanEntity entity) {
        if (Arrays.stream(entity.getInventory().getContents()).filter(Objects::nonNull).anyMatch(item -> Tag.ITEMS_BOATS.isTagged(item.getType()))) return;
        entity.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
    }

    public static boolean isInSpawn(HumanEntity entity) {
        return isInSpawn(entity.getLocation(), entity);
    }

    public static boolean isInSpawn(Location location, @Nullable HumanEntity entity) {
        return isInSpawn(location, entity, getSpawnRadius());
    }

    public static boolean isInSpawn(Location location, @Nullable HumanEntity entity, int distance) {
        if (getSpawnLocation() == null || distance <= -1 || (entity != null && entity.getGameMode().equals(GameMode.CREATIVE))) return false;
        else if (location.getWorld().getUID() != getSpawnLocation().getWorld().getUID()) return false;
        else return !(location.distance(getSpawnLocation()) > distance);
    }

    public static boolean isInEnd(HumanEntity entity) {
        return isInEnd(entity.getLocation(), entity);
    }

    public static boolean isInEnd(Location location, @Nullable HumanEntity entity) {
        return isInEnd(location, entity, getEndRadius());
    }

    public static boolean isInEnd(Location location, @Nullable HumanEntity entity, int distance) {
        if (getEndLocation() == null || distance <= -1 || (entity != null && entity.getGameMode().equals(GameMode.CREATIVE))) return false;
        else if (location.getWorld().getUID() != getEndLocation().getWorld().getUID()) return false;
        else return !(location.distance(getEndLocation()) > distance);
    }

    public static Map<UUID, Integer> getBreeds() {
        return Collections.unmodifiableMap(breeds);
    }

    public static void addBreed(UUID uuid) {
        breeds.put(uuid, breeds.getOrDefault(uuid, 0) + 1);
        save(true);
    }

    public static int getBreeds(UUID uuid) {
        return breeds.getOrDefault(uuid, 0);
    }

    public static void resetBreeds() {
        breeds.clear();
    }

    public static Map.Entry<UUID, Integer> getHighestBreedEntry() {
        return Collections.max(breeds.entrySet(), Map.Entry.comparingByValue());
    }
}