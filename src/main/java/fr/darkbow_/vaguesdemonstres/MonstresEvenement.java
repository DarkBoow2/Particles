package fr.darkbow_.vaguesdemonstres;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class MonstresEvenement implements Listener {
    private VaguesdeMonstres main;

    public MonstresEvenement(VaguesdeMonstres vaguesdemonstres){this.main = vaguesdemonstres;}

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(main.VaguesdeMonstres && !main.EstEnPause){
            if(VaguesdeMonstres.task.isCancelled()){
                VaguesdeMonstres.task = new Taches(main.getInstance()).runTaskTimer(main.getInstance(), 20L, 20L);
            }

            Player player = event.getPlayer();
            if(!main.getSurvivants().contains(player)){
                main.getSurvivants().add(player);
            }

            if(!main.getMonstres().containsKey(player)){
                List<EntityType> entitytype = new ArrayList<>();
                entitytype.add(EntityType.SKELETON);
                main.getMonstres().put(player, entitytype);
            }
        }
    }

    @EventHandler
    public void onSpawn(PlayerEggThrowEvent event){
        event.getEgg().setBounce(true);
    }
}
