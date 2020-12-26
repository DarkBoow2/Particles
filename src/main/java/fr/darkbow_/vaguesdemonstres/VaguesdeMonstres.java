package fr.darkbow_.vaguesdemonstres;

import fr.darkbow_.vaguesdemonstres.scoreboard.ScoreboardSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class VaguesdeMonstres extends JavaPlugin {
    public static BukkitTask task;
    private VaguesdeMonstres instance;
    public int timer = 0;
    private List<Player> survivants;
    private HashMap<Player, List<EntityType>> monstres;
    private HashMap<Player, Boolean> veulentvoirinfos;
    private HashSet<Material> bad_blocks;
    public int monstresbasiques = 10; //5 minutes = 300
    public int monstresbasiquesinitial = 10;
    public int monstresvener = 15; //20 minutes = 1200
    public int monstresvenerinitial = 15;
    public boolean VaguesdeMonstres = false;
    public boolean EstEnPause = false;

    private Map<Player, ScoreboardSign> boards;

    public VaguesdeMonstres getInstance() {
        return this.instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.boards = new HashMap<>();

        this.survivants = new ArrayList<>();
        this.monstres = new HashMap<>();
        this.veulentvoirinfos = new HashMap<>();

        Random r = new Random();

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

    public Map<Player, ScoreboardSign> getBoards(){
        return this.boards;
    }

    public String getTimeFormat(long seconds) {
        int jour = (int) TimeUnit.SECONDS.toDays(seconds);
        long heures = TimeUnit.SECONDS.toHours(seconds) - (jour * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long seconde = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        String textejour = "";
        if(seconds >= 86400){
            textejour = jour + "j:";
        }

        String texteheures = "";
        if(seconds >= 3600){
            texteheures = heures + "h";
        }

        String texteminutes = "";
        if(seconds >= 60){
            texteminutes = minute + "m";
        }

        return textejour + texteheures + texteminutes + seconde + "s";
    }

    public Map<Player, Boolean> VeulentVoirInfos(){
        return this.veulentvoirinfos;
    }

    public boolean VeutVoirInfos(Player player){
        if(!this.VeulentVoirInfos().containsKey(player)){
            VeulentVoirInfos().put(player, true);
        }

        return VeulentVoirInfos().get(player);
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
}