package fr.darkbow_.headdrops;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class HeadDropsEvent implements Listener {
    private HeadDrops main;

    public HeadDropsEvent(HeadDrops headdrops){main = headdrops;}

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        ItemStack head = null;
        int chance = 0;
        Random r = new Random();

        chance = r.nextInt(100);
        if(chance > 0 && chance < main.getConfig().getInt(event.getEntityType().name())){
            head = new ItemStack(Material.PLAYER_HEAD, 1, (byte)0);
            SkullMeta playerheadmeta = (SkullMeta) head.getItemMeta();
            String skullname;

            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                skullname = player.getName();
            } else {
                skullname = "MHF_" + event.getEntityType().name();
            }

            if(playerheadmeta != null){
                playerheadmeta.setOwner(skullname);
            }

            head.setItemMeta(playerheadmeta);
        }

        if(head != null){
            event.getDrops().add(head);
        }
    }
}