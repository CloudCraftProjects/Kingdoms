package tk.booky.kingdoms.listener;
// Created by booky10 in Kingdoms (16:54 14.09.21)

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;
import static org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;
import static org.bukkit.Sound.ITEM_TOTEM_USE;
import static org.bukkit.SoundCategory.AMBIENT;

public record TeamListener(KingdomsManager manager) implements Listener {

    @SuppressWarnings("unchecked")
    private static final GameRule<Boolean> ALLOW_PVP_GAMERULE = (GameRule<Boolean>) GameRule.getByName("allowPvP");

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.hasExplicitlyChangedBlock() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if (ALLOW_PVP_GAMERULE != null) {
                Boolean value = event.getTo().getWorld().getGameRuleValue(ALLOW_PVP_GAMERULE);
                if (value != null && !value) {
                    return;
                }
            }

            KingdomsTeam team = KingdomsTeam.byTreasure(event.getTo().getBlock());
            if (team != null) {
                if (team.members().contains(event.getPlayer().getUniqueId())) {
                    Score score = manager.coinsObjective().getScore(event.getPlayer().getName());
                    if (score.isScoreSet() && score.getScore() > 0) {
                        manager.message(team, event.getPlayer().getName() + " has obtained " + score.getScore() + " coins for your team. (Running)");
                        manager.message(event.getPlayer(), "You have gotten " + score.getScore() + " coins for your team.");
                        event.getPlayer().playSound(event.getTo(), ENTITY_PLAYER_LEVELUP, AMBIENT, 1f, 2f);

                        team.coins(team.coins() + score.getScore());
                        score.setScore(0);
                    }
                } else if (team.coins() > 0) {
                    Score score = manager.coinsObjective().getScore(event.getPlayer().getName());
                    if (!score.isScoreSet() || score.getScore() <= 0) {
                        int takenCoins = Math.min(500, team.coins());

                        manager.message(team, text(event.getPlayer().getName() + " took " + takenCoins + " coins from your team.", RED));
                        manager.message(event.getPlayer(), "You have taken " + takenCoins + " coins from team " + team.name().toLowerCase() + ".");
                        event.getPlayer().playSound(event.getTo(), BLOCK_NOTE_BLOCK_PLING, AMBIENT, 1f, 2f);

                        manager.coinsObjective().getScore(event.getPlayer().getName()).setScore(takenCoins);
                        team.coins(team.coins() - takenCoins);

                        if (team.coins() <= 0) {
                            broadcast(text("Team " + team.name().toLowerCase() + " has no more coins. THE MEMBER WILL NOT RESPAWN.", RED));
                            event.getPlayer().getWorld().playSound(team.treasureLocation(), ITEM_TOTEM_USE, Short.MAX_VALUE, 0.25f);
                        }
                    } else {
                        manager.message(event.getPlayer(), text("You have already coins on you.", RED));
                        event.getPlayer().playSound(event.getTo(), ENTITY_ENDERMAN_TELEPORT, AMBIENT, 1f, 0.5f);
                    }
                } else {
                    manager.message(event.getPlayer(), text("This team has currently no coins.", RED));
                    event.getPlayer().playSound(event.getTo(), ENTITY_ENDERMAN_TELEPORT, AMBIENT, 1f, 0.5f);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            ItemStack item = event.getCurrentItem();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        KingdomsTeam team = KingdomsTeam.byMember(event.getPlayer().getUniqueId());
        if (team != null) {
            if (team.coins() <= 0) {
                broadcast(manager.prefix(text("Player " + event.getPlayer().getName() + " has died and has to select a new team!", RED)));
                event.getPlayer().kick(text("Rejoin to select a new team.", RED));
                team.members().remove(event.getPlayer().getUniqueId());
            } else {
                event.getPlayer().getEquipment().setHelmet(team.coloredHelmet(), true);
                if (team.treasureLocation() != null) {
                    if (event.getPlayer().getBedSpawnLocation() == null) {
                        event.setRespawnLocation(team.treasureLocation().toCenterLocation());
                    }

                    if (!manager.config().started()) {
                        getScheduler().runTask(manager.plugin(), () ->
                            manager.worldBorderApi().setBorder(event.getPlayer(), 9, team.treasureLocation().toCenterLocation()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> item.getItemMeta().hasCustomModelData());
        KingdomsTeam team = KingdomsTeam.byMember(event.getEntity().getUniqueId());

        if (team != null) {
            if (event.getEntity().getUniqueId().equals(team.king())) {
                broadcast(manager.prefix(text("The king " + event.getEntity().getName() + " of team " + team.name() + " has died! Members have received slowness and weakness for five minutes.", GREEN)));
                List<PotionEffect> effects = Arrays.asList(
                    new PotionEffect(PotionEffectType.SLOW, 300, 2, false, false),
                    new PotionEffect(PotionEffectType.WEAKNESS, 300, 0, false, false)
                );

                for (UUID uuid : team.members()) {
                    Player player = getPlayer(uuid);
                    if (player != null && !player.getUniqueId().equals(team.king())) {
                        player.addPotionEffects(effects);
                    }
                }
            }
        }

        Score score = manager.coinsObjective().getScore(event.getEntity().getName());
        if (score.isScoreSet() && score.getScore() > 0) {
            Player killer = event.getEntity().getKiller();

            if (killer != null) {
                Score killerScore = manager.coinsObjective().getScore(killer.getName());
                killerScore.setScore(killerScore.getScore() + score.getScore());

                manager.message(killer, "You have obtained " + score.getScore() + " coins.");
                killer.playSound(event.getEntity().getLocation(), ENTITY_PLAYER_LEVELUP, AMBIENT, 1f, 2f);
            } else {
                List<Player> players = new ArrayList<>(getOnlinePlayers());
                Collections.shuffle(players, new SecureRandom());
                Player target = players.get(0);

                Score targetScore = manager.coinsObjective().getScore(target.getName());
                targetScore.setScore(targetScore.getScore() + score.getScore());

                broadcast(manager.prefix(target.getName() + " has gotten " + score.getScore() + " coins through random selection."));
                target.playSound(event.getEntity().getLocation(), ENTITY_PLAYER_LEVELUP, AMBIENT, 1f, 2f);
            }

            score.setScore(0);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER && event.getEntityType() == EntityType.PLAYER && !event.isCancelled()) {
            event.setCancelled(((Player) event.getDamager()).getGameMode() != GameMode.CREATIVE &&
                KingdomsTeam.byMember(event.getDamager().getUniqueId()) == KingdomsTeam.byMember(event.getEntity().getUniqueId()));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().showBossBar(manager.coinBossbar().bossBar());
    }

    // @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.getPlayer().hideBossBar(manager.coinBossbar().bossBar());

        if (ALLOW_PVP_GAMERULE != null) {
            Boolean pvp = event.getPlayer().getWorld().getGameRuleValue(ALLOW_PVP_GAMERULE);
            if (pvp != null && pvp) {
                Score score = manager.coinsObjective().getScore(event.getPlayer().getName());
                if (score.isScoreSet() && score.getScore() > 0) {
                    switch (event.getReason()) {
                        case KICKED, DISCONNECTED -> event.getPlayer().damage(Integer.MAX_VALUE, null);
                    }
                }
            }
        }
    }
}
