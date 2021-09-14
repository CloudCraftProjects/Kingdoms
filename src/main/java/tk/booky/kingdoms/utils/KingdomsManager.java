package tk.booky.kingdoms.utils;
// Created by booky10 in CraftAttack (14:51 01.03.21)

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class KingdomsManager {

    private static final Component PREFIX = text()
        .append(text('[', GRAY))
        .append(text('K', WHITE, BOLD))
        .append(text('D', AQUA, BOLD))
        .append(text(']', GRAY))
        .append(space()).build();

    private final KingdomsConfig config;
    private final Plugin plugin;
    private PvpTimerTask task;
    private Boolean cloudPlane;
    private World overworld;

    public KingdomsManager(Plugin plugin, KingdomsConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public boolean isRunningCloudPlane() {
        if (cloudPlane == null) {
            try {
                Class.forName("tk.booky.cloudplane.CloudPlaneConfig");
                return cloudPlane = true;
            } catch (ClassNotFoundException exception) {
                return cloudPlane = false;
            }
        } else {
            return cloudPlane;
        }
    }

    @SuppressWarnings("deprecation") // it's "just" unsafe ¯\_(ツ)_/¯
    public void fail(String message) throws WrapperCommandSyntaxException {
        String prefixed = Bukkit.getUnsafe().legacyComponentSerializer().serialize(prefix(text(message, RED)));
        throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(prefixed)).create());
    }

    @SuppressWarnings("deprecation") // it's "just" unsafe ¯\_(ツ)_/¯
    public void fail(Component component) throws WrapperCommandSyntaxException {
        String message = Bukkit.getUnsafe().legacyComponentSerializer().serialize(prefix(component.color(RED)));
        throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(message)).create());
    }

    public void message(Audience audience, String message) {
        audience.sendMessage(Identity.nil(), prefix(text(message, GREEN)), MessageType.SYSTEM);
    }

    public void message(Audience audience, Component component) {
        audience.sendMessage(Identity.nil(), PREFIX.append(component), MessageType.SYSTEM);
    }

    public Component prefix(String message) {
        return PREFIX.append(text(message));
    }

    public Component prefix(Component component) {
        return PREFIX.append(component);
    }

    public World loadOverworld() {
        for (World world : Bukkit.getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                return overworld = world;
            }
        }

        throw new IllegalStateException("No overworld could be found!");
    }

    public boolean isInSpawn(Location location, @Nullable HumanEntity entity) {
        return isInRadius(location, entity, config.spawnLocation(), config.spawnRadiusSquared());
    }

    public boolean isInEnd(Location location, @Nullable HumanEntity entity) {
        return isInRadius(location, entity, config.endLocation(), config.endRadiusSquared());
    }

    public boolean isInRadius(Location target, @Nullable HumanEntity entity, Location source, int radiusSquared) {
        if (source == null || radiusSquared <= 0) {
            return false;
        } else if (entity != null && entity.getGameMode() == GameMode.CREATIVE) {
            return false;
        } else if (target.getWorld() != source.getWorld()) {
            return false;
        } else {
            return target.distanceSquared(source) <= radiusSquared;
        }
    }

    public PvpTimerTask task() {
        return task == null ? task = new PvpTimerTask(this) : task;
    }

    public KingdomsConfig config() {
        return config;
    }

    public World overworld() {
        return overworld;
    }

    public Plugin plugin() {
        return plugin;
    }
}
