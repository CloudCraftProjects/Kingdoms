package tk.booky.kingdoms.commands.admin;
// Created by booky10 in Kingdoms (20:18 08.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static tk.booky.kingdoms.utils.KingdomsUtilities.overworld;

public class StartSubCommand extends CommandAPICommand implements CommandExecutor {

    private final Plugin plugin;

    public StartSubCommand(Plugin plugin) {
        super("start");
        this.plugin = plugin;

        withArguments(
            new IntegerArgument("countdown", 0), // 30
            new TextArgument("project"),
            new TextArgument("owner"),
            new LongArgument("worldborder_start_time", 0), // 100
            new DoubleArgument("worldborder_start", 1), // 2000
            new LongArgument("worldborder_end_time", 0), // 2000
            new DoubleArgument("worldborder_end", 1) // 59999968
        );

        withPermission("kingdoms.command.admin.start").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        World world = overworld();
        int timeId = Bukkit.getScheduler().runTaskTimer(plugin,
            () -> world.setTime(world.getTime() + 20), 20, 1).getTaskId();

        new BukkitRunnable() {
            private int countdown = (int) args[0];

            @Override
            public void run() {
                switch (countdown--) {
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        Title.Times countdownTimes = Title.Times.of(Ticks.duration(5), Ticks.duration(10), Ticks.duration(5));
                        Title countdownTitle = Title.title(Component.text(countdown + 1, NamedTextColor.GOLD, TextDecoration.BOLD), Component.empty(), countdownTimes);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.AMBIENT, 100000f, 1f);
                            player.showTitle(countdownTitle);
                        }

                        if (countdown == 0) {
                            Bukkit.broadcast(Component.text(args[1] + " startet in 1 Sekunde!", NamedTextColor.GREEN));
                            break;
                        }
                    case 30:
                    case 15:
                        Bukkit.broadcast(Component.text(args[1] + " startet in " + (countdown + 1) + " Sekunden!", NamedTextColor.GREEN));
                        break;
                    case 0:
                        Title.Times startTimes = Title.Times.of(Ticks.duration(10), Ticks.duration(100), Ticks.duration(20));
                        Title startTitle = Title.title(Component.text(args[2].toString()), Component.text(args[1].toString(), NamedTextColor.GREEN), startTimes);

                        Component message = Component.text(args[1] + " hat begonnen!", NamedTextColor.GREEN);
                        Bukkit.getConsoleSender().sendMessage(message);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.AMBIENT, 100000f, 1f);
                            player.sendMessage(message);
                            player.showTitle(startTitle);
                        }

                        Bukkit.getScheduler().runTaskLater(plugin,
                            () -> overworld().getWorldBorder().setSize((double) args[6]), ((long) args[5]) * 20);
                        world.getWorldBorder().setSize((double) args[4], (long) args[3]);

                        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                        world.setGameRule(GameRule.DO_MOB_SPAWNING, true);

                        Bukkit.getScheduler().cancelTask(timeId);
                        Bukkit.getScheduler().cancelTask(getTaskId());
                    default:
                        break;
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
