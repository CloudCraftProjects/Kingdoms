package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (19:02 14.09.21)

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import tk.booky.kingdoms.team.KingdomsTeam;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class CoinBossbarTask extends BukkitRunnable {

    private BossBar bossBar;

    @Override
    public void run() {
        TextComponent.Builder builder = text(); // Y: %s | G: %s | B: %s | R: %s

        KingdomsTeam[] teams = KingdomsTeam.values();
        for (int i = 0; i < teams.length; i++) {
            KingdomsTeam team = teams[i];
            NamedTextColor coinColor;

            if (team.coins() <= 0) {
                coinColor = RED;
            } else if (team.coins() <= 3333) {
                coinColor = GOLD;
            } else if (team.coins() <= 6666) {
                coinColor = YELLOW;
            } else {
                coinColor = GREEN;
            }

            builder
                .append(text(team.character(), team.color()))
                .append(text(": ", WHITE))
                .append(text(team.coins(), coinColor));

            if (i != teams.length - 1) {
                builder.append(text(" | ", GRAY));
            }
        }

        if (bossBar == null) {
            bossBar = BossBar.bossBar(builder.build(), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        } else {
            bossBar.name(builder.build());
        }
    }

    public void start(Plugin plugin) {
        runTaskTimerAsynchronously(plugin, 0, 40);
    }

    public BossBar bossBar() {
        return bossBar;
    }
}
