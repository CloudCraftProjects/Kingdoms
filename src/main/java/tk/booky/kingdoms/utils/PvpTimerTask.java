package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (19:00 09.09.21)

import org.bukkit.GameRule;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getWorlds;

public class PvpTimerTask extends TimerTask {

    @SuppressWarnings("unchecked")
    private static final GameRule<Boolean> GAME_RULE = (GameRule<Boolean>) GameRule.getByName("allowPvP");
    private static final DecimalFormat FORMAT = new DecimalFormat("00");
    private final Timer timer = new Timer("PvP Timer Thread", true);
    private final KingdomsManager manager;

    public PvpTimerTask(KingdomsManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        if (GAME_RULE == null) {
            throw new IllegalStateException();
        } else {
            int start = manager.config().pvpTimesStart();
            int end = manager.config().pvpTimesEnd();

            if (start != end) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);

                AtomicBoolean newValue = new AtomicBoolean(false);
                int startHour = start % 60, startMinute = start / 60;
                int endHour = end % 60, endMinute = end / 60;

                if (hour >= startHour && minute >= startMinute) {
                    if (hour <= endHour && minute < endMinute) {
                        newValue.set(true);
                    }
                }

                Boolean oldValue = manager.overworld().getGameRuleValue(GAME_RULE);
                if (oldValue != null && oldValue != newValue.get()) {
                    broadcast(manager.prefix(text()
                        .color(GREEN)
                        .append(text("Timed pvp has been "))
                        .append(text(newValue.get() ? "activated" : "deactivated").decorate(BOLD))
                        .append(text(" until " + (newValue.get() ? "" : "tomorrow ") +
                            FORMAT.format(newValue.get() ? endHour : startHour) + ":" +
                            FORMAT.format(newValue.get() ? endMinute : startMinute) + ":" +
                            FORMAT.format(seconds) + "."
                        ).decoration(BOLD, false))
                        .build()
                    ));

                    getScheduler().runTask(manager.plugin(), () -> {
                        for (World world : getWorlds()) {
                            world.setGameRule(GAME_RULE, newValue.get());
                        }
                    });
                }
            }
        }
    }

    public Timer timer() {
        return timer;
    }
}
