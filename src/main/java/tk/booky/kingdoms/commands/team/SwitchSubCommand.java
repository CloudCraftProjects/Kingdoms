package tk.booky.kingdoms.commands.team;
// Created by booky10 in Kingdoms (14:35 18.09.21)

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import tk.booky.kingdoms.team.KingdomsTeam;
import tk.booky.kingdoms.utils.KingdomsManager;

import static org.bukkit.Bukkit.broadcast;
import static org.bukkit.Statistic.PLAY_ONE_MINUTE;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class SwitchSubCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private final KingdomsManager manager;

    public SwitchSubCommand(KingdomsManager manager) {
        super("switch");
        this.manager = manager;

        withRequirement(sender -> sender instanceof Player && ((Player) sender).getStatistic(PLAY_ONE_MINUTE) <= 20 * 60 * 60 * 2);
        withArguments(new MultiLiteralArgument("yellow", "green", "blue", "red"));

        withPermission("kingdoms.command.team.switch").executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        KingdomsTeam newTeam = KingdomsTeam.valueOf(((String) args[0]).toUpperCase());
        if (newTeam.coins() <= 0) {
            manager.fail("This team has already died, please choose a different one.");
        } else {
            KingdomsTeam oldTeam = KingdomsTeam.byMember(sender.getUniqueId());
            if (oldTeam != null) {
                oldTeam.members().remove(sender.getUniqueId());
                broadcast(manager.prefix("Player " + sender.getName() + " has switched the team from " + oldTeam.name().toLowerCase() + " to " + newTeam.name().toLowerCase() + "."));
                Location location = (newTeam.treasureLocation() == null ? manager.overworld().getSpawnLocation() : newTeam.treasureLocation()).toCenterLocation();

                AttributeInstance maxHealth = sender.getAttribute(GENERIC_MAX_HEALTH);
                sender.setHealth(maxHealth == null ? 20 : maxHealth.getValue());
                for (PotionEffect effect : sender.getActivePotionEffects()) {
                    sender.removePotionEffect(effect.getType());
                }

                sender.getInventory().clear();
                sender.setTotalExperience(0);
                sender.setFoodLevel(20);
                sender.setLevel(0);
                sender.setExp(0);

                sender.getEquipment().setHelmet(newTeam.coloredHelmet(), true);
                newTeam.members().add(sender.getUniqueId());
                sender.teleportAsync(location);
            } else {
                manager.fail("An internal error occurred while trying to resolve the specified team.");
            }
        }
    }
}
