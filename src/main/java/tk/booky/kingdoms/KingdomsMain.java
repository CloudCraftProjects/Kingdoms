package tk.booky.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.kingdoms.commands.KingdomsRootCommand;
import tk.booky.kingdoms.listener.BlockListener;
import tk.booky.kingdoms.listener.BoatListener;
import tk.booky.kingdoms.listener.InteractListener;
import tk.booky.kingdoms.listener.MiscListener;
import tk.booky.kingdoms.manager.KingdomsManager;

public final class KingdomsMain extends JavaPlugin {

    public static KingdomsMain main;

    @Override
    public void onEnable() {
        main = this;

        saveDefaultConfig();
        KingdomsManager.load();

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new MiscListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new BoatListener(), this);

        new KingdomsRootCommand().register();
    }

    @Override
    public void onDisable() {
        KingdomsManager.save(false);
    }
}
