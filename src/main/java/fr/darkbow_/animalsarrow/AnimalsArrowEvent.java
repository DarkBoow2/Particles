package fr.darkbow_.animalsarrow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AnimalsArrowEvent implements Listener {
    private AnimalsArrow main;

    public AnimalsArrowEvent(AnimalsArrow vaguesdemonstres){this.main = vaguesdemonstres;}

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();

            org.bukkit.inventory.meta.Damageable bowdurability = (org.bukkit.inventory.meta.Damageable) event.getBow().getItemMeta();
            player.sendMessage("Durabilité : " + bowdurability.getDamage());
            /*bowdurability.setDamage(bowdurability.getDamage()-1);
            if(bowdurability.getDamage() == 0){
                event.getBow().setAmount(event.getBow().getAmount()-1);
                player.updateInventory();
            } else if(bowdurability.getDamage() > 0){
                event.getBow().setItemMeta((ItemMeta) bowdurability);
            }*/

            ItemStack otherhand = null;
            if(event.getBow() != null){
                if(event.getBow().isSimilar(player.getInventory().getItemInMainHand())){
                    otherhand = player.getInventory().getItemInOffHand();
                } else if(event.getBow().isSimilar(player.getInventory().getItemInOffHand())){
                    otherhand = player.getInventory().getItemInMainHand();
                }

                if(otherhand != null && otherhand.getType() != Material.AIR){
                    /*if(event.getProjectile() instanceof Arrow){
                        ((Arrow) event.getProjectile()).setBounce(false);
                    }*/

                    /*event.setCancelled(true);*/
                    main.throwItem(player, otherhand, event.getProjectile());

                    player.updateInventory();
                    Vector vector = player.getLocation().getDirection();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity().getScoreboardTags().contains("NoFallDamage")){
            event.setCancelled(true);
            Bukkit.broadcastMessage("Cause : " + event.getCause().name());
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                Bukkit.broadcastMessage("yooo");
                event.getEntity().removeScoreboardTag("NoFallDamage");
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getEntity().getScoreboardTags().contains("NoFallDamage")){
            event.setCancelled(true);
        }
    }
}