package fr.darkbow_.monombre;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MonOmbreTask extends BukkitRunnable {
    private MonOmbre main;

    public MonOmbreTask(MonOmbre monombre){this.main = monombre;}

    private int un = 5;

    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().size() > 1){
            Player darkbow = Bukkit.getPlayer("DarkBow_");
            if(Bukkit.getOnlinePlayers().contains(darkbow)){
                if(main.getJoueur(darkbow) != null && main.getJoueur(darkbow).getPoints().size() == un){
                    Vector v = new Vector();
                    NPC npc = main.getMonOmbre();
                    v.setX(darkbow.getLocation().getX() - main.getJoueur(darkbow).getPoints().get(un-1).getLocation().getX());
                    v.setZ(darkbow.getLocation().getY() - main.getJoueur(darkbow).getPoints().get(un-1).getLocation().getY());
                    v.setZ(darkbow.getLocation().getZ() - main.getJoueur(darkbow).getPoints().get(un-1).getLocation().getZ());

                    if(darkbow.getPose() == Pose.DYING){
                        ((Player) npc.getEntity()).setSwimming(true);
                    }
                }
                Bukkit.broadcastMessage("" + un);
                un++;
            }
        }
    }
}