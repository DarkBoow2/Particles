package fr.darkbow_.animalsarrow;

import fr.darkbow_.animalsarrow.scoreboard.ScoreboardSign;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnimalsArrow extends JavaPlugin {
    private AnimalsArrow instance;

    private Map<Player, ScoreboardSign> boards;

    private Map<Arrow, Entity> customprojectiles;

    public AnimalsArrow getInstance() {
        return this.instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.boards = new HashMap<>();
        this.customprojectiles = new HashMap<>();

        getServer().getPluginManager().registerEvents(new AnimalsArrowEvent(this), this);

        System.out.println("[VaguesdeMonstres] Plugin Activé !!");
    }

    @Override
    public void onDisable() {
        System.out.println("[VaguesdeMonstres] Plugin Désactivé !");
    }

    private static final Random RANDOM = new Random();

    public Map<Player, ScoreboardSign> getBoards(){
        return this.boards;
    }

    public void  throwItem(Player player, ItemStack it, Entity e) {
        Entity projectile = null;
        Egg egg = null;
        Snowball snowball = null;
/*        Bukkit.broadcastMessage("§e§lType Item : §b" + it.getType().toString());*/
        if(it.getType().toString().endsWith("SPAWN_EGG")) {
/*            Bukkit.broadcastMessage("§6Egg Meta : §a" + it.getType().toString().replace("_SPAWN_EGG", ""));*/
            projectile = e.getWorld().spawnEntity(e.getLocation(), Objects.requireNonNull(EntityType.fromName(it.getType().toString().replace("_SPAWN_EGG", ""))));
/*            if(((SpawnEggMeta) it.getItemMeta()).getSpawnedType() == null) {
                projectile = e.getWorld().dropItem(e.getLocation(), it);
            } else {
                projectile = e.getWorld().spawnEntity(e.getLocation(), EntityType.fromName(it.getType().toString().replace("SPAWN_EGG", "")));
            }*/
        } else if(it.getType() == Material.EGG || it.getType() == Material.SNOWBALL) {
            if(it.getType() == Material.EGG) {
                /*egg = player.launchProjectile(Egg.class);*/
                egg = (Egg) e.getWorld().spawnEntity(e.getLocation(), EntityType.EGG);
            } else if(it.getType() == Material.SNOWBALL) {
                /*snowball = player.launchProjectile(Snowball.class);*/
                snowball = (Snowball) e.getWorld().spawnEntity(e.getLocation(), EntityType.SNOWBALL);
            }
        } else if(it.getType().toString().contains("BOAT")) {
            Boat boat = (Boat) e.getWorld().spawnEntity(e.getLocation(), EntityType.BOAT);
            TreeSpecies treespecies = TreeSpecies.GENERIC;
            if(it.getType() == Material.ACACIA_BOAT){ treespecies = TreeSpecies.ACACIA; }
            if(it.getType() == Material.BIRCH_BOAT){ treespecies = TreeSpecies.BIRCH; }
            if(it.getType() == Material.DARK_OAK_BOAT){ treespecies = TreeSpecies.DARK_OAK; }
            if(it.getType() == Material.JUNGLE_BOAT){ treespecies = TreeSpecies.JUNGLE; }
            if(it.getType() == Material.SPRUCE_BOAT){ treespecies = TreeSpecies.REDWOOD; }
            if(it.getType() == Material.OAK_BOAT){ treespecies = TreeSpecies.GENERIC; }
            boat.setWoodType(treespecies);
            projectile = boat;
        } else {
            projectile = e.getWorld().dropItem(e.getLocation(), it);
        }


        if(projectile != null ) {
            projectile.setVelocity(e.getVelocity());
            projectile.addScoreboardTag("NoFallDamage");
            e.setPassenger(projectile);
            ((LivingEntity) projectile).setCollidable(false);
        }

        if(egg != null) {
            egg.setVelocity(e.getVelocity());
            e.setPassenger(egg);
            ((LivingEntity) egg).setCollidable(false);
        }

        if(snowball != null) {
            snowball.setVelocity(e.getVelocity());
            ((LivingEntity) snowball).setCollidable(false);
            e.setPassenger(snowball);
        }
    }

    public String bool(boolean b){
        String bool = null;
        if(b){
            bool = ChatColor.GREEN + "§lAffiché";
        } else {
            bool = ChatColor.RED + "§lCaché";
        }

        return bool;
    }

    public Map<Arrow, Entity> getCustomprojectiles() {
        return customprojectiles;
    }
}