package fr.darkbow_.headdrops;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;

public class Tache extends BukkitRunnable {

    private HeadDrops main;
    public Tache(HeadDrops headdrops) {this.main = headdrops;}

    public static boolean isRunning = false;

    @Override
    public void run() {
        if(!isRunning){
            isRunning = true;
        }

        for(Map.Entry<Player, Integer> particlesmap : main.getParticles().entrySet()){
            if(particlesmap.getValue() <= 0){
                main.getParticles().remove(particlesmap.getKey());
            } else {
                for(int degree = 0; degree < 360; degree+=3){

                    double radians = Math.toRadians(degree);
                    double cos = Math.cos(radians) * 5;
                    double sin = Math.sin(radians) * 5;
                    Location loc = particlesmap.getKey().getLocation();

                    loc.add(cos, 0, sin);
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.fromRGB(0, 1, 1), 1));
                    loc.subtract(cos, 0, sin);
                }

                for(Player pls : Bukkit.getOnlinePlayers()){
                    if(pls != particlesmap.getKey()) {
                        if(particlesmap.getKey().getLocation().getY() - pls.getLocation().getY() < 5 && particlesmap.getKey().getLocation().getY() - pls.getLocation().getY() > -5){
                            Location plsloc = pls.getLocation();
                            plsloc.setY(particlesmap.getKey().getLocation().getY());

                            double distance = particlesmap.getKey().getLocation().distance(plsloc);
                            if (distance < 5) {
                                Bukkit.broadcastMessage("§b§l" + pls.getName() + " §rest dans le cercle de §6§l" + particlesmap.getKey().getName() + " §r!");
                            }
                        }
                    }
                }

                particlesmap.setValue(particlesmap.getValue() - 1);
            }
        }

        if(main.getParticles().isEmpty()){
            isRunning = false;
            cancel();
        }
    }
}