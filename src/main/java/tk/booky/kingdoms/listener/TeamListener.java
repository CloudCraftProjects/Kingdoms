package tk.booky.kingdoms.listener;
// Created by booky10 in Kingdoms (16:54 14.09.21)

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;
import static org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;
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
    public void onDeath(PlayerDeathEvent event) {
        for (KingdomsTeam team : KingdomsTeam.values()) {
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
                break;
            }
        }

        Score score = manager.coinsObjective().getScore(event.getEntity().getName());
        if (score.isScoreSet() && score.getScore() > 0) {
            Player killer = event.getEntity().getKiller();

            if (killer != null) {
                for (KingdomsTeam team : KingdomsTeam.values()) {
                    if (team.members().contains(killer.getUniqueId())) {
                        manager.message(team, killer.getName() + " has obtained " + score.getScore() + " coins for your team. (Kill)");
                        manager.message(killer, "You have obtained " + score.getScore() + " coins for your team.");
                        event.getEntity().playSound(event.getEntity().getLocation(), ENTITY_PLAYER_LEVELUP, AMBIENT, 1f, 2f);

                        team.coins(team.coins() + score.getScore());
                        score.setScore(0);
                        return;
                    }
                }
            }

            manager.plugin().getSLF4JLogger().warn("Had to delete {} coins, a killer could not be found.", score.getScore());
            manager.message(event.getEntity(), text("Could not find killer, deleting " + score.getScore() + " coins...", RED));
            score.setScore(0);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().showBossBar(manager.coinBossbar().bossBar());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.getPlayer().hideBossBar(manager.coinBossbar().bossBar());
    }
}
