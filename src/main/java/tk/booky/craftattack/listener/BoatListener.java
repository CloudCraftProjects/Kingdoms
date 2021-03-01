package tk.booky.craftattack.listener;
// Created by booky10 in CraftAttack (15:27 01.03.21)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import tk.booky.craftattack.manager.CraftAttackManager;

public class BoatListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!CraftAttackManager.isInSpawn(event.getPlayer().getLocation(), null, 5)) return;
        CraftAttackManager.giveBoat(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!CraftAttackManager.isInSpawn(event.getRespawnLocation(), null, 5)) return;
        CraftAttackManager.giveBoat(event.getPlayer());
    }
}