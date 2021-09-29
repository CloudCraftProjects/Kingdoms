package tk.booky.kingdoms.commands.teleport;
// Created by booky10 in Kingdoms (15:36 28.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static org.bukkit.Bukkit.getScheduler;

public class TeamSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    @SuppressWarnings("unchecked")
    private static final GameRule<Boolean> ALLOW_PVP = (GameRule<Boolean>) GameRule.getByName("allowPvP");
    private final Set<UUID> currentlyTeleporting = new HashSet<>();
    private final KingdomsManager manager;

    public TeamSubCommand(KingdomsManager manager) {
        super("team");
        this.manager = manager;

        withPermission("kingdoms.command.teleport.team").executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        if (ALLOW_PVP != null) {
            Boolean pvp = sender.getWorld().getGameRuleValue(ALLOW_PVP);
            if (pvp != null && pvp) {
                manager.fail("Teleporting is deactivated while pvp is enabled.");
            }
        }

        KingdomsTeam team = KingdomsTeam.byMember(sender.getUniqueId());
        if (team == null) {
            manager.fail("You are currently not in a team.");
        } else if (team.treasureLocation() == null) {
            manager.fail("Your team currently doesn't have a treasure location.");
        } else if (currentlyTeleporting.add(sender.getUniqueId())) {
            manager.message(sender, "Please don't move, you will get teleported in five seconds.");
            Location oldLocation = sender.getLocation().toBlockLocation();

            getScheduler().runTaskLater(manager.plugin(), () -> {
                currentlyTeleporting.remove(sender.getUniqueId());

                if (sender.isOnline()) {
                    if (oldLocation.equals(sender.getLocation().toBlockLocation())) {
                        sender.teleportAsync(team.treasureLocation().toCenterLocation(), TeleportCause.COMMAND);
                        manager.message(sender, "You have been brought to your team!");
                    } else {
                        manager.message(sender, text("You have moved.", RED));
                    }
                }
            }, sender.getGameMode() == GameMode.SPECTATOR ? 0 : 5 * 20);
        } else {
            manager.fail("You are already teleporting.");
        }
    }
}
