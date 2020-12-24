package fr.darkbow_.vaguesdemonstres;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class VaguesdeMonstres extends JavaPlugin {
    public static BukkitTask task;
    private VaguesdeMonstres instance;
    public int timer = 0;
    private List<Player> survivants;
    private HashMap<Player, List<EntityType>> monstres;
    private HashSet<Material> bad_blocks;
    public int monstresbasiques = 60; //5 minutes = 300
    public int monstresvener = 180; //20 minutes = 1200
    public boolean VaguesdeMonstres = false;

    public VaguesdeMonstres getInstance() {
        return this.instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.survivants = new ArrayList<>();
        this.monstres = new HashMap<>();

        List<EntityType> darkbow = new ArrayList<>();
        darkbow.add(EntityType.SPIDER);
        monstres.put(Bukkit.getPlayer("DarkBow_"), darkbow);
        survivants.add(Bukkit.getPlayer("DarkBow_"));

        Random r = new Random();

        int entite = 700;
        System.out.println("Test = " + entite);

        this.bad_blocks = new HashSet<>();
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);

        getCommand("vaguesdemonstres").setExecutor(new CommandVaguesdeMonstres(this));
        getServer().getPluginManager().registerEvents(new MonstresEvenement(this), this);

        System.out.println("[VaguesdeMonstres] Plugin Activé !!");
    }

    @Override
    public void onDisable() {
        System.out.println("[VaguesdeMonstres] Plugin Désactivé !");
    }

    public List<Player> getSurvivants() {
        return this.survivants;
    }

    public HashMap<Player, List<EntityType>> getMonstres() {
        return monstres;
    }

    public Location generateEntitySpawnLocation(EntityType entitytype, Location loc, int rayon){
        Random random = new Random();

        double x = random.nextInt(rayon);
        double y = random.nextInt(2);
        double z = random.nextInt(rayon);

        int signe = random.nextInt(2);
        if(signe == 0){
            x = -x;
        }
        x += loc.getX();

        signe = random.nextInt(2);
        if(signe == 0){
            y = -y;
        }
        y += loc.getY();

        signe = random.nextInt(2);
        if(signe == 0){
            z = -z;
        }

        Location randomLocation = new Location(loc.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);

        while(!isLocationSafe(entitytype, randomLocation)){
            generateEntitySpawnLocation(entitytype, loc, rayon);
        }

        return randomLocation;
    }

    public boolean isLocationSafe(EntityType entitytype, Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x,y - 1, z);
        Block above = location.getWorld().getBlockAt(x,y + 1, z);
        Block abovemoins = location.getWorld().getBlockAt(x,y - 3, z);

        boolean bool = false;
        HashSet<Material> listenoire = this.bad_blocks;

        if(entitytype == EntityType.BLAZE){
            listenoire.remove(Material.LAVA);
            listenoire.remove(Material.FIRE);
        }

        if(listenoire.isEmpty()){
            bool = !((block.getType().isSolid()) || (above.getType().isSolid()) || !(abovemoins.getType().isSolid()));
        } else {
            bool = !(bad_blocks.contains(below.getType()) || (bad_blocks.contains(above.getType())) || (block.getType().isSolid()) || (above.getType().isSolid()) || !(abovemoins.getType().isSolid()));
        }

        return bool;
    }
}