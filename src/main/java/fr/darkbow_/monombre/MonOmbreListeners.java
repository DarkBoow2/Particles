package fr.darkbow_.monombre;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MonOmbreListeners implements Listener {

    private MonOmbre main;

    public MonOmbreListeners(MonOmbre monombre){this.main = monombre;}

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(Bukkit.getOnlinePlayers().size() > 1){
            if(event.getPlayer().getName().equals("DarkBow_")){
                Player darkbow = Bukkit.getPlayer("DarkBow_");
                Location darkloc = darkbow.getLocation();
                darkloc.setX(darkloc.getX() + 3);
                for(Player pls : Bukkit.getOnlinePlayers()){
                    if(!pls.getName().equals("DarkBow_") && Bukkit.getOnlinePlayers().contains(darkbow)){
                        pls.teleport(darkloc);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().getName().equals("DarkBow_")){
            main.getMonOmbre().getNavigator().setTarget(event.getPlayer(), false);
        }
    }
}