package tk.booky.craftattack.commands.breed;
// Created by booky10 in CraftAttack (12:12 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.CraftAttackMain;
import tk.booky.craftattack.manager.CraftAttackManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BreedLeaderboardSubCommand extends CommandAPICommand implements CommandExecutor {

    private int cachedHash;
    private String cachedLeaderboard;

    public BreedLeaderboardSubCommand() {
        super("leaderboard");

        withPermission("craftattack.command.breed.leaderboard");

        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        Map<UUID, Integer> breeds = CraftAttackManager.getBreeds();

        if (breeds.size() <= 0) {
            CommandAPI.fail("There are currently no breeds!");
        } else if (cachedHash == breeds.hashCode()) {
            sender.sendMessage(cachedLeaderboard);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(CraftAttackMain.main, () -> {
                AtomicInteger place = new AtomicInteger();
                String message = "Current leaderboard:\n" + new ArrayList<>(breeds.entrySet())
                        .parallelStream()
                        .sorted(Map.Entry.comparingByValue())
                        .sorted(Collections.reverseOrder())
                        .limit(7)
                        .map(entry -> place.incrementAndGet() + ". " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + ": " + entry.getValue())
                        .collect(Collectors.joining("\n"));

                cachedHash = breeds.hashCode();
                cachedLeaderboard = message;
                sender.sendMessage(message);
            });

            CommandAPI.fail("Cache is not up to date, it will be calculated...");
        }
    }
}