package tk.booky.kingdoms;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.kingdoms.commands.KingdomsRootCommand;
import tk.booky.kingdoms.listener.BlockListener;
import tk.booky.kingdoms.listener.InteractListener;
import tk.booky.kingdoms.listener.MiscListener;
import tk.booky.kingdoms.utils.KingdomsConfig;
import tk.booky.kingdoms.utils.KingdomsManager;

import java.io.File;

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
        manager.loadOverworld();

        Bukkit.getPluginManager().registerEvents(new InteractListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(manager), this);
        Bukkit.getPluginManager().registerEvents(new MiscListener(manager), this);
    }

    @Override
    public void onDisable() {
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
