package tk.booky.kingdoms.team;
// Created by booky10 in Kingdoms (15:31 10.09.21)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static net.kyori.adventure.identity.Identity.nil;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static org.apache.commons.lang.StringUtils.capitalize;
import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
import static org.bukkit.inventory.ItemFlag.HIDE_DYE;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
import static org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;

@SerializableAs("kingdoms-team")
public enum KingdomsTeam implements ConfigurationSerializable {

    YELLOW(NamedTextColor.YELLOW, 'Y'),
    GREEN(NamedTextColor.GREEN, 'G'),
    BLUE(NamedTextColor.BLUE, 'B'),
    RED(NamedTextColor.RED, 'R');

    private static final Map<Block, KingdomsTeam> BY_TREASURE = new HashMap<>();
    private static final Map<UUID, KingdomsTeam> BY_MEMBER = new HashMap<>();
    private final Set<UUID> members = new TeamMembers();
    private final Component suffixComponent;
    private final ItemStack coloredHelmet;
    private final NamedTextColor color;
    private final char character;
    private ArmorStand coinDisplayStand;
    private Location treasureLocation;
    private UUID king = nil().uuid();
    private int coins = 100_000;

    KingdomsTeam(NamedTextColor color, char character) {
        this.color = color;
        this.character = character;

        coloredHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) coloredHelmet.getItemMeta();
        meta.displayName(text(capitalize(name().toLowerCase()), color).decoration(ITALIC, false));
        meta.addItemFlags(HIDE_DYE, HIDE_ENCHANTS, HIDE_UNBREAKABLE, HIDE_ATTRIBUTES);
        meta.setColor(Color.fromRGB(color.value()));
        meta.setCustomModelData(1);
        meta.setUnbreakable(true);
        coloredHelmet.setItemMeta(meta);

        suffixComponent = text()
            .append(space())
            .append(text('[', GRAY))
            .append(text(character, color, BOLD))
            .append(text(']', GRAY))
            .build();
    }

    public static KingdomsTeam byMember(UUID member) {
        return BY_MEMBER.get(member);
    }

    public static KingdomsTeam byTreasure(Block block) {
        return BY_TREASURE.get(block);
    }

    @SuppressWarnings({"unchecked", "unused"})
    public static KingdomsTeam deserialize(Map<String, Object> serialized) {
        KingdomsTeam team = valueOf((String) serialized.get("name"));

        team.coins((int) serialized.getOrDefault("coins", team.coins()));
        team.king(UUID.fromString((String) serialized.getOrDefault("king", team.king().toString())));
        team.treasureLocation((Location) serialized.getOrDefault("treasure-location", team.treasureLocation()));

        team.members().clear();
        for (String member : (List<String>) serialized.getOrDefault("members", Collections.emptyList())) {
            team.members().add(UUID.fromString(member));
        }

        return team;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        List<String> members = new ArrayList<>();
        for (UUID member : members()) {
            members.add(member.toString());
        }

        Map<String, Object> serialized = new HashMap<>();
        serialized.put("treasure-location", treasureLocation());
        serialized.put("king", king().toString());
        serialized.put("members", members);
        serialized.put("coins", coins());
        serialized.put("name", name());
        return serialized;
    }

    public ArmorStand coinDisplayStand() {
        return coinDisplayStand;
    }

    public Location treasureLocation() {
        return treasureLocation;
    }

    public Component suffixComponent() {
        return suffixComponent;
    }

    public ItemStack coloredHelmet() {
        return coloredHelmet.clone();
    }

    public NamedTextColor color() {
        return color;
    }

    public Set<UUID> members() {
        return members;
    }

    public char character() {
        return character;
    }

    public void coinDisplayStand(ArmorStand coinDisplayStand) {
        this.coinDisplayStand = coinDisplayStand;
    }

    public void treasureLocation(Location treasureLocation) {
        if (this.treasureLocation != null) {
            BY_TREASURE.remove(this.treasureLocation.getBlock());
        }

        if (treasureLocation != null) {
            BY_TREASURE.put(treasureLocation.getBlock(), this);
        }

        if (coinDisplayStand != null) {
            if (treasureLocation != null) {
                coinDisplayStand.teleportAsync(treasureLocation.toCenterLocation().add(0, 2, 0));
            } else {
                coinDisplayStand.remove();
            }
        } else if (treasureLocation != null) {
            coinDisplayStand = treasureLocation.getWorld().spawn(treasureLocation.toCenterLocation().add(0, 2, 0), ArmorStand.class);
            coinDisplayStand.customName(text(coins + " Coins", WHITE).append(suffixComponent));
            coinDisplayStand.setCustomNameVisible(true);
            coinDisplayStand.setInvulnerable(true);
            coinDisplayStand.setPersistent(true);
            coinDisplayStand.setInvisible(true);
            coinDisplayStand.setMarker(true);
        }

        this.treasureLocation = treasureLocation;
    }

    public UUID king() {
        return king;
    }

    public void king(UUID king) {
        this.king = king;
    }

    public int coins() {
        return coins;
    }

    public void coins(int coins) {
        this.coins = coins;

        if (coinDisplayStand != null) {
            coinDisplayStand.customName(text(coins + " Coins", WHITE).append(suffixComponent));
        }
    }

    private class TeamMembers extends HashSet<UUID> {

        @Override
        public boolean add(UUID uuid) {
            BY_MEMBER.put(uuid, KingdomsTeam.this);
            return super.add(uuid);
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> collection) {
            Objects.requireNonNull(collection);
            boolean modified = false;
            Iterator<UUID> it = iterator();

            while (it.hasNext()) {
                Object object = it.next();
                if (!collection.contains(object)) {
                    it.remove();
                    modified = true;
                    BY_MEMBER.remove(object);
                }
            }

            return modified;
        }

        @Override
        public boolean remove(Object object) {
            BY_MEMBER.remove(object);
            return super.remove(object);
        }

        @Override
        public void clear() {
            for (UUID uuid : this) {
                BY_MEMBER.remove(uuid);
            }

            super.clear();
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            boolean modified = false;

            if (size() > collection.size()) {
                for (Object object : collection) {
                    if (modified |= remove(object)) {
                        // noinspection SuspiciousMethodCalls
                        BY_MEMBER.remove(object);
                    }
                }
            } else {
                for (Iterator<?> iterator = iterator(); iterator.hasNext(); ) {
                    Object object = iterator.next();
                    if (collection.contains(object)) {
                        // noinspection SuspiciousMethodCalls
                        BY_MEMBER.remove(iterator.next());
                        iterator.remove();
                        modified = true;
                    }
                }
            }

            return modified;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends UUID> collection) {
            boolean modified = false;
            for (UUID uuid : collection) {
                if (add(uuid)) {
                    BY_MEMBER.put(uuid, KingdomsTeam.this);
                    modified = true;
                }
            }

            return modified;
        }

        @Override
        public boolean removeIf(Predicate<? super UUID> filter) {
            Objects.requireNonNull(filter);
            boolean removed = false;
            Iterator<UUID> each = iterator();

            while (each.hasNext()) {
                UUID uuid = each.next();
                if (filter.test(uuid)) {
                    each.remove();
                    removed = true;
                    BY_MEMBER.remove(uuid);
                }
            }

            return removed;
        }
    }
}
