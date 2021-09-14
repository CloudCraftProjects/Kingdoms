package tk.booky.kingdoms;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.kingdoms.commands.KingdomsRootCommand;
import tk.booky.kingdoms.listener.BlockListener;
import tk.booky.kingdoms.listener.InteractListener;
import tk.booky.kingdoms.listener.MiscListener;
import tk.booky.kingdoms.listener.SelectionListener;
import tk.booky.kingdoms.listener.SitListener;
import tk.booky.kingdoms.listener.TeamListener;
import tk.booky.kingdoms.utils.KingdomsConfig;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class KingdomsMain extends JavaPlugin {

    private KingdomsConfig configuration;
    private CommandAPICommand command;
    private KingdomsManager manager;

    @Override
    public void onLoad() {
        configuration = new KingdomsConfig(new File(getDataFolder(), "config.yml"));
        manager = new KingdomsManager(this, configuration);
        (command = new KingdomsRootCommand(manager)).register();
    }

    @Override
    public void onEnable() {
        manager
            .loadCoinsObjective()
            .loadOverworld();

        configuration
            .reloadConfiguration()
            .saveConfiguration();

        Bukkit.getPluginManager().registerEvents(new SelectionListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new TeamListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new MiscListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new SitListener(manager), this);

        if (manager.isRunningCloudPlane()) {
            manager.task().timer().scheduleAtFixedRate(manager.task(), 0, TimeUnit.MINUTES.toMillis(1));
        } else {
            getLogger().warning("The pvp times feature is not available, because cloudplane is not used.");
        }
    }

    @Override
    public void onDisable() {
        if (manager.isRunningCloudPlane()) {
            manager.task().timer().cancel();
        }

        configuration.saveConfiguration();
        CommandAPI.unregister(command.getName(), true);
    }

    public KingdomsConfig configuration() {
        return configuration;
    }

    public CommandAPICommand command() {
        return command;
    }

    public KingdomsManager manager() {
        return manager;
    }
}
