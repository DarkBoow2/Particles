package fr.darkbow_.monombre;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MonOmbreTask extends BukkitRunnable {
    private MonOmbre main;

    public MonOmbreTask(MonOmbre monombre){this.main = monombre;}

    private Vector move;
    private int un = 0;

    @Override
    public void run() {
        //Bukkit.broadcastMessage("" + un);
        //un++;
        if(Bukkit.getOnlinePlayers().size() > 1){
            Player darkbow = Bukkit.getPlayer("DarkBow_");
            if(Bukkit.getOnlinePlayers().contains(darkbow)){
                move = darkbow.getVelocity();
                //Bukkit.broadcastMessage(move.toString());
                for(Player pls : Bukkit.getOnlinePlayers()){
                    if(!pls.getName().equals("DarkBow_")){
                        pls.setVelocity(move);
                    }
                }
            }
        }
    }
}
