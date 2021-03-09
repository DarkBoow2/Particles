package fr.darkbow_.animalsarrow;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AnimalsArrowEvent implements Listener {
    private AnimalsArrow main;

    public AnimalsArrowEvent(AnimalsArrow vaguesdemonstres){this.main = vaguesdemonstres;}

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
    }
}
