package tk.booky.craftattack.commands.breed;
// Created by booky10 in CraftAttack (12:12 19.03.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import tk.booky.craftattack.manager.CraftAttackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
            List<Map.Entry<UUID, Integer>> entryList = new ArrayList<>(breeds.entrySet());
            entryList.sort(Map.Entry.comparingByValue());

            List<Map.Entry<UUID, Integer>> sorted = new ArrayList<>();
            int end = Math.min(7, entryList.size());
            for (int i = 0; i != end; i++) sorted.add(entryList.get(i));

            String joined = sorted.stream().map(entry -> (sorted.indexOf(entry) + 1) + ". " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + ": " + entry.getValue()).collect(Collectors.joining("\n"));
            String message = "Current leaderboard:\n" + joined;

            cachedHash = breeds.hashCode();
            cachedLeaderboard = message;

            sender.sendMessage(message);
        }
    }
}