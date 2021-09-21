package tk.booky.kingdoms.listener;
// Created by booky10 in Kingdoms (20:28 12.09.21)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.util.HashSet;
import java.util.Set;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;
import static org.bukkit.SoundCategory.AMBIENT;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class SelectionListener implements Listener {

    private static final PotionEffect BLINDNESS = new PotionEffect(PotionEffectType.BLINDNESS, 1_000_000, 0, false, false);
    private static final Component SELECTION_MESSAGE;
    private final Set<Player> selecting = new HashSet<>();
    private final KingdomsManager manager;

    static {
        TextComponent.Builder builder = text()
            .append(newline())
            .append(text(" === [ ", GRAY))
            .append(text("Team Selection", RED, BOLD))
            .append(text(" ] ===", GRAY))
            .append(newline()).append(newline());

        for (KingdomsTeam team : KingdomsTeam.values()) {
            builder
                .append(text("  » ", GRAY))
                .append(text("Team " + StringUtils.capitalize(team.name().toLowerCase()), team.color())
                    .clickEvent(runCommand("/kingdoms$select_team " + team.name()))
                    .hoverEvent(text("Click to join team " + team.name().toLowerCase() + ".", GRAY)))
                .append(newline());
        }

        SELECTION_MESSAGE = builder
            .append(newline())
            .append(text(" === [ ", GRAY))
            .append(text("Team Selection", RED, BOLD))
            .append(text(" ] ===", GRAY))
            .append(newline())
            .build();
    }

    public SelectionListener(KingdomsManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        KingdomsTeam team = KingdomsTeam.byMember(event.getPlayer().getUniqueId());
        if (team != null) {
            if (team.coins() <= 0) {
                manager.message(event.getPlayer(), text("Your team has died, please select a new team.", RED).append(newline()));

                AttributeInstance maxHealth = event.getPlayer().getAttribute(GENERIC_MAX_HEALTH);
                event.getPlayer().setHealth(maxHealth == null ? 20 : maxHealth.getValue());
                for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
                    event.getPlayer().removePotionEffect(effect.getType());
                }

                event.getPlayer().getInventory().clear();
                event.getPlayer().setFoodLevel(20);

                event.getPlayer().setTotalExperience(0);
                event.getPlayer().setLevel(0);
                event.getPlayer().setExp(0);
            }
            return;
        }

        for (Player player : selecting) {
            player.hidePlayer(manager.plugin(), event.getPlayer());
            event.getPlayer().hidePlayer(manager.plugin(), player);
        }
        selecting.add(event.getPlayer());

        event.getPlayer().addPotionEffect(BLINDNESS);
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleportAsync(new Location(event.getPlayer().getWorld(), 0, -10, 0))
            .whenComplete((success, throwable) -> {
                if (success && throwable != null) {
                    event.getPlayer().kick(manager.prefix(text("An internal error occurred while preparing team selection.", RED)));
                } else {
                    event.getPlayer().sendMessage(SELECTION_MESSAGE);
                }
            });
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/kingdoms$select_team ")) {
            event.setCancelled(true);

            if (!selecting.contains(event.getPlayer())) {
                manager.message(event.getPlayer(), text("You have already selected your team.", RED));
            } else {
                KingdomsTeam team = KingdomsTeam.valueOf(event.getMessage().substring(22));

                if (team.coins() <= 0) {
                    manager.message(event.getPlayer(), text("This team is currently dead.", RED));
                } else {
                    event.getPlayer().teleportAsync((team.treasureLocation() == null
                            ? manager.overworld().getSpawnLocation() : team.treasureLocation()).toCenterLocation())
                        .whenComplete((success, throwable) -> {
                            if (success && throwable == null) {
                                event.getPlayer().setGameMode(GameMode.SURVIVAL);
                                event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                                team.members().add(event.getPlayer().getUniqueId());

                                manager.message(event.getPlayer(), "You have selected team " + team.name().toLowerCase() + ".");
                                broadcast(manager.prefix(text(event.getPlayer().getName() + " has selected team " + team.name().toLowerCase() + ".", GREEN)));
                                event.getPlayer().playSound(event.getPlayer().getLocation(), ENTITY_PLAYER_LEVELUP, AMBIENT, 1f, 1f);
                            } else {
                                event.getPlayer().kick(manager.prefix(text("An internal error occurred while selecting team.", RED)));
                            }
                        });

                    selecting.remove(event.getPlayer());
                    for (Player player : selecting) {
                        player.showPlayer(manager.plugin(), event.getPlayer());
                        event.getPlayer().showPlayer(manager.plugin(), player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        selecting.remove(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != TeleportCause.PLUGIN && selecting.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (selecting.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
