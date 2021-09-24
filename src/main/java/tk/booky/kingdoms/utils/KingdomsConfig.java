package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (15:46 09.09.21)

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import tk.booky.kingdoms.team.KingdomsTeam;

import java.io.File;
import java.io.IOException;

public class KingdomsConfig {

    private final File configurationFile;
    private int endRadius = 0, spawnRadius = 0, netherRadius = 0, pvpTimesStart = 0, pvpTimesEnd = 0;
    private int endRadiusSquared = 0, spawnRadiusSquared = 0, netherRadiusSquared = 0;
    private Location endLocation, spawnLocation, netherLocation;
    private boolean endActivated = true, netherActivated = true;

    public KingdomsConfig(File configurationFile) {
        this.configurationFile = configurationFile;
        ConfigurationSerialization.registerClass(KingdomsTeam.class);
    }

    public KingdomsConfig reloadConfiguration() {
        if (!configurationFile.exists()) {
            return saveConfiguration();
        } else {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(configurationFile);

            pvpTimesStart = configuration.getInt("pvp-times.start", pvpTimesStart);
            pvpTimesEnd = configuration.getInt("pvp-times.end", pvpTimesEnd);

            endActivated = configuration.getBoolean("end.activated", endActivated);
            endLocation = configuration.getLocation("end.location", endLocation);
            endRadius = configuration.getInt("end.radius", endRadius);

            spawnLocation = configuration.getLocation("spawn.location", spawnLocation);
            spawnRadius = configuration.getInt("spawn.radius", spawnRadius);

            netherActivated = configuration.getBoolean("nether.activated", netherActivated);
            netherLocation = configuration.getLocation("nether.location", netherLocation);
            netherRadius = configuration.getInt("nether.radius", netherRadius);

            for (KingdomsTeam team : KingdomsTeam.values()) {
                // No need to do any more stuff here because it
                // gets loaded into the instance automatically
                configuration.get("teams." + team.name().toLowerCase());
            }

            netherRadiusSquared = netherRadius * netherRadius;
            spawnRadiusSquared = spawnRadius * spawnRadius;
            endRadiusSquared = endRadius * endRadius;
            return this;
        }
    }

    public KingdomsConfig saveConfiguration() {
        try {
            FileConfiguration configuration = new YamlConfiguration();

            configuration.set("pvp-times.start", pvpTimesStart);
            configuration.set("pvp-times.end", pvpTimesEnd);

            configuration.set("end.activated", endActivated);
            configuration.set("end.location", endLocation);
            configuration.set("end.radius", endRadius);

            configuration.set("spawn.location", spawnLocation);
            configuration.set("spawn.radius", spawnRadius);

            configuration.set("nether.activated", netherActivated);
            configuration.set("nether.location", netherLocation);
            configuration.set("nether.radius", netherRadius);

            for (KingdomsTeam team : KingdomsTeam.values()) {
                configuration.set("teams." + team.name().toLowerCase(), team);
            }

            configuration.save(configurationFile);
            return this;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public File configurationFile() {
        return configurationFile;
    }

    public int endRadius() {
        return endRadius;
    }

    public int spawnRadius() {
        return spawnRadius;
    }

    public int netherRadius() {
        return netherRadius;
    }

    public int pvpTimesStart() {
        return pvpTimesStart;
    }

    public int pvpTimesEnd() {
        return pvpTimesEnd;
    }

    public int endRadiusSquared() {
        return endRadiusSquared;
    }

    public int spawnRadiusSquared() {
        return spawnRadiusSquared;
    }

    public int netherRadiusSquared() {
        return netherRadiusSquared;
    }

    public Location endLocation() {
        return endLocation;
    }

    public Location spawnLocation() {
        return spawnLocation;
    }

    public Location netherLocation() {
        return netherLocation;
    }

    public boolean endActivated() {
        return endActivated;
    }

    public boolean netherActivated() {
        return netherActivated;
    }

    public void endRadius(int endRadius) {
        this.endRadius = endRadius;
        endRadiusSquared = endRadius * endRadius;
        saveConfiguration();
    }

    public void spawnRadius(int spawnRadius) {
        this.spawnRadius = spawnRadius;
        spawnRadiusSquared = spawnRadius * spawnRadius;
        saveConfiguration();
    }

    public void netherRadius(int netherRadius) {
        this.netherRadius = netherRadius;
        netherRadiusSquared = netherRadius * netherRadius;
        saveConfiguration();
    }

    public void pvpTimesStart(int pvpTimesStart) {
        this.pvpTimesStart = pvpTimesStart;
        saveConfiguration();
    }

    public void pvpTimesEnd(int pvpTimesEnd) {
        this.pvpTimesEnd = pvpTimesEnd;
        saveConfiguration();
    }

    public void endLocation(Location endLocation) {
        this.endLocation = endLocation;
        saveConfiguration();
    }

    public void spawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        saveConfiguration();
    }

    public void netherLocation(Location netherLocation) {
        this.netherLocation = netherLocation;
        saveConfiguration();
    }

    public void endActivated(boolean endActivated) {
        this.endActivated = endActivated;
        saveConfiguration();
    }

    public void netherActivated(boolean netherActivated) {
        this.netherActivated = netherActivated;
        saveConfiguration();
    }
}
