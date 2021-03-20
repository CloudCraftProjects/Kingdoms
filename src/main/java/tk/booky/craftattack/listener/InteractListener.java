package tk.booky.craftattack.listener;
// Created by booky10 in CraftAttack (15:02 01.03.21)

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tk.booky.craftattack.manager.CraftAttackManager;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (CraftAttackManager.isInSpawn(player)) {
            switch (event.getAction()) {
                case PHYSICAL:
                    break;
                case RIGHT_CLICK_BLOCK:
                    Block clickedBlock = event.getClickedBlock();

                    if (clickedBlock != null) {
                        boolean interactable = clickedBlock.getType().isInteractable();

                        if (interactable) {
                            if (!(clickedBlock.getState() instanceof Container) || player.isSneaking()) {
                                if (!Tag.TRAPDOORS.isTagged(clickedBlock.getType()) && !Tag.CAMPFIRES.isTagged(clickedBlock.getType()) && !Tag.FENCE_GATES.isTagged(clickedBlock.getType())) {
                                    return;
                                }
                            }
                        } else if (Tag.ITEMS_BOATS.isTagged(event.getMaterial())) {
                            return;
                        }
                    }

                    break;
                default:
                    return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (CraftAttackManager.isInSpawn(event.getRightClicked().getLocation(), player)) {
            event.setCancelled(true);
        }
    }
}