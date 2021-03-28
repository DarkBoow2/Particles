package fr.darkbow_.animalsarrow;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnimalsArrowEvent implements Listener {
    private AnimalsArrow main;

    public AnimalsArrowEvent(AnimalsArrow vaguesdemonstres){main = vaguesdemonstres;}

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                main.entityHider.toggleEntity(player, event.getProjectile());

                if(Boolean.parseBoolean(main.getPluginoptions().get("projectile-rides-arrow"))){
                    event.setConsumeItem(false);
                }

                if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE){
                    if(!Boolean.parseBoolean(main.getPluginoptions().get("projectile-rides-arrow"))){
                        org.bukkit.inventory.meta.Damageable bowdurability = (org.bukkit.inventory.meta.Damageable) event.getBow().getItemMeta();
                        bowdurability.setDamage(bowdurability.getDamage() + 1);
                        if(bowdurability.getDamage() == 0){
                            event.getBow().setAmount(event.getBow().getAmount() - 1);
                        } else if(bowdurability.getDamage() > 0){
                            event.getBow().setItemMeta((ItemMeta) bowdurability);
                        }
                    }
                }

                ItemStack otherhand = null;
                if(event.getBow() != null){
                    if(event.getBow().isSimilar(player.getInventory().getItemInMainHand())){
                        otherhand = player.getInventory().getItemInOffHand();
                        if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE){
                            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                        }
                    } else if(event.getBow().isSimilar(player.getInventory().getItemInOffHand())){
                        otherhand = player.getInventory().getItemInMainHand();
                        if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE){
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        }
                    }

                    main.throwItem(player, otherhand, event.getProjectile());
                    if(!Boolean.parseBoolean(main.getPluginoptions().get("projectile-rides-arrow"))){
                        event.setCancelled(true);
                    }
                }

                if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE){
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(event.getEntity().getScoreboardTags().contains("AnimalArrow")){
                event.setCancelled(true);
                if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                    event.getEntity().removeScoreboardTag("AnimalArrow");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(event.getEntity().getScoreboardTags().contains("AnimalArrow")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnProjectileHit(ProjectileHitEvent event){
        if(event.getEntity() instanceof Arrow){
            if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
                if(main.getCustomprojectiles().containsKey(event.getEntity())){
                    if(main.getCustomprojectiles().get(event.getEntity()).getScoreboardTags().contains("AnimalArrow")){
                        main.getCustomprojectiles().get(event.getEntity()).removeScoreboardTag("AnimalArrow");
                        if(main.getCustomprojectiles().get(event.getEntity()) instanceof LivingEntity){
                            ((LivingEntity) main.getCustomprojectiles().get(event.getEntity())).setCollidable(true);
                            ((LivingEntity) main.getCustomprojectiles().get(event.getEntity())).setAI(true);
                            main.getCustomprojectiles().get(event.getEntity()).leaveVehicle();
                            main.getCustomprojectiles().remove(event.getEntity());
                            event.getEntity().remove();
                        } else if(main.getCustomprojectiles().get(event.getEntity()).getType() == EntityType.EGG || main.getCustomprojectiles().get(event.getEntity()).getType() == EntityType.SNOWBALL){
                            main.getCustomprojectiles().get(event.getEntity()).leaveVehicle();
                            main.getCustomprojectiles().remove(event.getEntity());
                        }
                    }

                    event.getEntity().remove();
                }
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(main.getCustomprojectiles().containsValue(event.getVehicle())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(main.getCustomprojectiles().containsValue(event.getVehicle())){
                if(!event.getVehicle().isOnGround()){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHorseEjectYou(HorseJumpEvent event){
        if(Boolean.parseBoolean(main.getPluginoptions().get("enable"))){
            if(main.getCustomprojectiles().containsValue(event.getEntity())){
                event.setCancelled(true);
            }
        }
    }
}