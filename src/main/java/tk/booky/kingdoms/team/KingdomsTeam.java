package tk.booky.kingdoms.team;
// Created by booky10 in Kingdoms (15:31 10.09.21)

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.identity.Identity.nil;

@SerializableAs("kingdoms-team")
public enum KingdomsTeam implements ConfigurationSerializable {

    YELLOW(NamedTextColor.YELLOW),
    GREEN(NamedTextColor.GREEN),
    BLUE(NamedTextColor.BLUE),
    RED(NamedTextColor.RED);

    private final Set<UUID> members = new HashSet<>();
    private final NamedTextColor color;
    private Location treasureLocation;
    private UUID king = nil().uuid();
    private int coins = 10_000;

    KingdomsTeam(NamedTextColor color) {
        this.color = color;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public static KingdomsTeam deserialize(Map<String, Object> serialized) {
        KingdomsTeam team = valueOf((String) serialized.get("name"));

        team.treasureLocation = (Location) serialized.getOrDefault("treasure-location", team.treasureLocation);
        team.king = UUID.fromString((String) serialized.getOrDefault("king", team.king.toString()));
        team.coins = (int) serialized.getOrDefault("coins", team.coins);

        team.members.clear();
        for (String member : (List<String>) serialized.getOrDefault("members", Collections.emptyList())) {
            team.members.add(UUID.fromString(member));
        }

        return team;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        List<String> members = new ArrayList<>();
        for (UUID member : this.members) {
            members.add(member.toString());
        }

        Map<String, Object> serialized = new HashMap<>();
        serialized.put("treasure-location", treasureLocation);
        serialized.put("king", king.toString());
        serialized.put("members", members);
        serialized.put("coins", coins);
        serialized.put("name", name());
        return serialized;
    }

    public Set<UUID> members() {
        return members;
    }

    public NamedTextColor color() {
        return color;
    }

    public Location treasureLocation() {
        return treasureLocation;
    }

    public void treasureLocation(Location treasureLocation) {
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
    }
}
