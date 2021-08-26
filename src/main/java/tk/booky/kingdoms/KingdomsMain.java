package tk.booky.kingdoms;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.kingdoms.commands.KingdomsRootCommand;
import tk.booky.kingdoms.listener.BlockListener;
import tk.booky.kingdoms.listener.InteractListener;
import tk.booky.kingdoms.listener.MiscListener;
import tk.booky.kingdoms.utils.KingdomsManager;

public final class KingdomsMain extends JavaPlugin {

    public static KingdomsMain main;
    private CommandAPICommand command;

    @Override
    public void onLoad() {
        (command = new KingdomsRootCommand()).register();
    }

    @Override
    public void onEnable() {
        main = this;

        saveDefaultConfig();
        KingdomsManager.load();

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new MiscListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {
        KingdomsManager.save(false);
        CommandAPI.unregister(command.getName(), true);
    }
}
