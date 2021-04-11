package fr.darkbow_.headdrops;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HeadDropsEvent implements Listener {
    private HeadDrops main;

    public HeadDropsEvent(HeadDrops headdrops){main = headdrops;}

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() != null){
            if(event.getItem().getType() == Material.END_CRYSTAL && event.getAction() == Action.RIGHT_CLICK_AIR){
                main.getParticles().put(event.getPlayer(), 30);
                if(!Tache.isRunning){
                    HeadDrops.task = new Tache(main.getInstance()).runTaskTimer(main.getInstance(), 2L, 2L);
                }

                event.setCancelled(true);
            }
        }
    }
}