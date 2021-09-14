package tk.booky.kingdoms.listener;
// Created by booky10 in Kingdoms (16:54 14.09.21)

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Score;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING;
import static org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;
import static org.bukkit.SoundCategory.AMBIENT;

public record TeamListener(KingdomsManager manager) implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.hasExplicitlyChangedBlock() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
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
                        manager.message(team, text(event.getPlayer().getName() + " took 500 coins from your team.", RED));
                        manager.message(event.getPlayer(), "You have taken 500 coins from team " + team.name().toLowerCase() + ".");
                        event.getPlayer().playSound(event.getTo(), BLOCK_NOTE_BLOCK_PLING, AMBIENT, 1f, 2f);

                        manager.coinsObjective().getScore(event.getPlayer().getName()).setScore(500);
                        team.coins(team.coins() - 500);
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
}
