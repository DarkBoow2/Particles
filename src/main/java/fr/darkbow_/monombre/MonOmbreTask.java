package fr.darkbow_.monombre;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MonOmbreTask extends BukkitRunnable {
    private MonOmbre main;

    public MonOmbreTask(MonOmbre monombre){this.main = monombre;}

    private int un = 0;

    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().size() > 1){
            Player darkbow = Bukkit.getPlayer("DarkBow_");
            if(Bukkit.getOnlinePlayers().contains(darkbow)){
                Point point = new Point(darkbow);
                Bukkit.broadcastMessage("" + un);
                un++;
            }
        }
    }
}
