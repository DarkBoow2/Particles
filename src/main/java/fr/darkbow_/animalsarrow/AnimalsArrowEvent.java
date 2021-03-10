package fr.darkbow_.animalsarrow;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

public class AnimalsArrowEvent implements Listener {
    private AnimalsArrow main;

    public AnimalsArrowEvent(AnimalsArrow vaguesdemonstres){this.main = vaguesdemonstres;}

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            /*for(Player pls : Bukkit.getOnlinePlayers()){
                main.entityHider.toggleEntity(pls, event.getProjectile());
            }*/

            Player player = (Player) event.getEntity();
            /*main.entityHider.toggleEntity(player, event.getProjectile());*/

            org.bukkit.inventory.meta.Damageable bowdurability = (org.bukkit.inventory.meta.Damageable) event.getBow().getItemMeta();
            /*player.sendMessage("Durabilité : " + bowdurability.getDamage());*/
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
                    /*event.setCancelled(true);*/
                    /*if(event.getProjectile() instanceof Arrow){
                        ((Arrow) event.getProjectile()).setBounce(false);
                    }*/

                    /*event.setCancelled(true);*/
                    main.throwItem(player, otherhand, event.getProjectile());

                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity().getScoreboardTags().contains("AnimalArrow")){
            event.setCancelled(true);
            /*Bukkit.broadcastMessage("Cause : " + event.getCause().name());*/
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                event.getEntity().removeScoreboardTag("AnimalArrow");
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getEntity().getScoreboardTags().contains("AnimalArrow")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnProjectileHit(ProjectileHitEvent event){
        if(main.getCustomprojectiles().containsKey(event.getEntity())){
            if(main.getCustomprojectiles().get(event.getEntity()).getScoreboardTags().contains("AnimalArrow")){
                main.getCustomprojectiles().get(event.getEntity()).removeScoreboardTag("AnimalArrow");
                ((LivingEntity) main.getCustomprojectiles().get(event.getEntity())).setCollidable(true);
                ((LivingEntity) main.getCustomprojectiles().get(event.getEntity())).setAI(true);
                Objects.requireNonNull(event.getHitBlock()).setType(Material.GREEN_WOOL);
                main.getCustomprojectiles().get(event.getEntity()).leaveVehicle();
                main.getCustomprojectiles().remove(event.getEntity());
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event){
        if(main.getCustomprojectiles().containsValue(event.getVehicle())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event){
        if(main.getCustomprojectiles().containsValue(event.getVehicle())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHorseEjectYou(HorseJumpEvent event){
        if(main.getCustomprojectiles().containsValue(event.getEntity())){
            event.setCancelled(true);
        }
    }
}