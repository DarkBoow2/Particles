package fr.darkbow_.monombre;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MonOmbreListeners implements Listener {

    private MonOmbre main;

    public MonOmbreListeners(MonOmbre monombre){this.main = monombre;}

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().getName().equals("DarkBow_")){
            main.getMonOmbre().getNavigator().setTarget(event.getPlayer(), false);
        }
    }
}