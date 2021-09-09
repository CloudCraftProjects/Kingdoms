package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (15:46 09.09.21)

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class KingdomsConfig {

    private final File configurationFile;
    private Location endLocation, spawnLocation;
    private int endRadius = 0, spawnRadius = 0;
    private boolean endActivated = true;

    public KingdomsConfig(File configurationFile) {
        this.configurationFile = configurationFile;
        reloadConfiguration().saveConfiguration();
    }

    public KingdomsConfig reloadConfiguration() {
        if (!configurationFile.exists()) {
            return saveConfiguration();
        } else {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(configurationFile);

            endActivated = configuration.getBoolean("end.activated", endActivated);
            endLocation = configuration.getLocation("end.location", endLocation);
            endRadius = configuration.getInt("end.radius", endRadius);

            spawnLocation = configuration.getLocation("spawn.location", spawnLocation);
            spawnRadius = configuration.getInt("spawn.radius", spawnRadius);

            return this;
        }
    }

    public KingdomsConfig saveConfiguration() {
        try {
            FileConfiguration configuration = new YamlConfiguration();

            configuration.set("end.activated", endActivated);
            configuration.set("end.location", endLocation);
            configuration.set("end.radius", endRadius);

            configuration.set("spawn.location", spawnLocation);
            configuration.set("spawn.radius", spawnRadius);

            configuration.save(configurationFile);
            return this;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public File configurationFile() {
        return configurationFile;
    }

    public Location endLocation() {
        return endLocation;
    }

    public void endLocation(Location endLocation) {
        this.endLocation = endLocation;
        saveConfiguration();
    }

    public Location spawnLocation() {
        return spawnLocation;
    }

    public void spawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        saveConfiguration();
    }

    public boolean endActivated() {
        return endActivated;
    }

    public void endActivated(boolean endActivated) {
        this.endActivated = endActivated;
        saveConfiguration();
    }

    public int endRadius() {
        return endRadius;
    }

    public void endRadius(int endRadius) {
        this.endRadius = endRadius;
        saveConfiguration();
    }

    public int spawnRadius() {
        return spawnRadius;
    }

    public void spawnRadius(int spawnRadius) {
        this.spawnRadius = spawnRadius;
        saveConfiguration();
    }
}
