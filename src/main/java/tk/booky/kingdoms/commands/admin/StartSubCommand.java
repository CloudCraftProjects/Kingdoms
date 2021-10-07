package tk.booky.kingdoms.commands.admin;
// Created by booky10 in Kingdoms (20:18 08.09.21)

import com.github.yannicklamprecht.worldborder.api.IWorldBorder;
import com.github.yannicklamprecht.worldborder.api.Position;
import com.github.yannicklamprecht.worldborder.api.WorldBorderAction;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.title.Title.Times.of;
import static net.kyori.adventure.title.Title.title;
import static net.kyori.adventure.util.Ticks.duration;
import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.GameRule.DO_DAYLIGHT_CYCLE;
import static org.bukkit.GameRule.DO_MOB_SPAWNING;
import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT;
import static org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE;
import static org.bukkit.SoundCategory.AMBIENT;

public class StartSubCommand extends CommandAPICommand implements CommandExecutor {

    private final KingdomsManager manager;

    public StartSubCommand(KingdomsManager manager) {
        super("start");
        this.manager = manager;

        withArguments(
            new IntegerArgument("countdown", 0),
            new TextArgument("project"),
            new TextArgument("owner")
        );

        withPermission("kingdoms.command.admin.start").executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) {
        World world = manager.overworld();
        int timeId = getScheduler().runTaskTimer(manager.plugin(),
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
                        Times countdownTimes = of(duration(5), duration(10), duration(5));
                        Title countdownTitle = title(text(countdown + 1, GOLD, BOLD), empty(), countdownTimes);

                        for (Player player : getOnlinePlayers()) {
                            player.playSound(player.getLocation(), BLOCK_NOTE_BLOCK_HAT, AMBIENT, 314159f, 1f);
                            player.showTitle(countdownTitle);
                        }

                        if (countdown == 0) {
                            broadcast(manager.prefix(text(args[1] + " starts in one second.", GREEN)));
                            break;
                        }
                    case 30:
                    case 15:
                        broadcast(manager.prefix(text(args[1] + " starts in " + (countdown + 1) + " seconds.", GREEN)));
                        break;
                    case 0:
                        Times startTimes = of(duration(10), duration(100), duration(20));
                        Title startTitle = title(text(args[2].toString()), text(args[1].toString(), GREEN), startTimes);

                        Component message = manager.prefix(text(args[1] + " has started!", GOLD, BOLD));
                        getConsoleSender().sendMessage(message);

                        for (Player player : getOnlinePlayers()) {
                            player.playSound(player.getLocation(), UI_TOAST_CHALLENGE_COMPLETE, AMBIENT, 314159f, 1f);
                            player.showTitle(startTitle);
                            player.sendMessage(message);

                            KingdomsTeam team = KingdomsTeam.byMember(player.getUniqueId());
                            if (team != null) {
                                IWorldBorder border = manager.worldBorderApi().getWorldBorder(player);

                                border.center(Position.of(team.treasureLocation()));
                                border.send(player, WorldBorderAction.SET_CENTER);

                                border.size(9);
                                border.send(player, WorldBorderAction.SET_SIZE);

                                border.lerp(9, 509, 10 * 1000);
                                border.send(player, WorldBorderAction.LERP_SIZE);
                            }
                        }

                        getScheduler().runTaskLater(manager.plugin(), () -> {
                            for (Player player : getOnlinePlayers()) {
                                manager.worldBorderApi().resetWorldBorderToGlobal(player);
                            }
                        }, 10 * 20);

                        world.setGameRule(DO_DAYLIGHT_CYCLE, true);
                        world.setGameRule(DO_MOB_SPAWNING, true);
                        manager.config().started(true);

                        getScheduler().cancelTask(timeId);
                        getScheduler().cancelTask(getTaskId());
                    default:
                        break;
                }
            }
        }.runTaskTimer(manager.plugin(), 20, 20);
    }
}
