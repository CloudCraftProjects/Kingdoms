package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (19:40 26.08.21)

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class KingdomsUtilities {

    private static final Component PREFIX = text()
        .append(text('[', GRAY))
        .append(text('K', WHITE, BOLD))
        .append(text('D', AQUA, BOLD))
        .append(text(']', GRAY))
        .append(space()).build();

    private static World overworld;

    public static World overworld() throws IllegalStateException {
        if (overworld == null) {
            overworld = Bukkit.getWorlds().stream()
                .filter(world -> world.getEnvironment() == World.Environment.NORMAL)
                .findAny().orElse(null);

            if (overworld == null) {
                throw new IllegalStateException("No overworld found!");
            }
        }
        return overworld;
    }

    @SuppressWarnings("deprecation") // it's "just" unsafe ¯\_(ツ)_/¯
    public static void fail(String message) throws WrapperCommandSyntaxException {
        String prefixed = Bukkit.getUnsafe().legacyComponentSerializer().serialize(prefix(text(message, RED)));
        throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(prefixed)).create());
    }

    @SuppressWarnings("deprecation") // it's "just" unsafe ¯\_(ツ)_/¯
    public static void fail(Component component) throws WrapperCommandSyntaxException {
        String message = Bukkit.getUnsafe().legacyComponentSerializer().serialize(prefix(component.color(RED)));
        throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(message)).create());
    }

    public static void message(Audience audience, String message) {
        audience.sendMessage(Identity.nil(), prefix(text(message, GREEN)), MessageType.SYSTEM);
    }

    public static void message(Audience audience, Component component) {
        audience.sendMessage(Identity.nil(), prefix(component.color(GREEN)), MessageType.SYSTEM);
    }

    public static Component prefix(String message) {
        return PREFIX.append(text(message));
    }

    public static Component prefix(Component component) {
        return PREFIX.append(component);
    }
}
