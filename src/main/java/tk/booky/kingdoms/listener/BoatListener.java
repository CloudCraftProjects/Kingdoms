package tk.booky.kingdoms.listener;
// Created by booky10 in CraftAttack (15:27 01.03.21)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import tk.booky.kingdoms.manager.KingdomsManager;

public class BoatListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!KingdomsManager.isInSpawn(event.getPlayer().getLocation(), null, 5)) return;
        KingdomsManager.giveBoat(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!KingdomsManager.isInSpawn(event.getRespawnLocation(), null, 5)) return;
        KingdomsManager.giveBoat(event.getPlayer());
    }
}