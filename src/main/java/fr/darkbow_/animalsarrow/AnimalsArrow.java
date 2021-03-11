package fr.darkbow_.animalsarrow;

import fr.darkbow_.animalsarrow.scoreboard.ScoreboardSign;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnimalsArrow extends JavaPlugin {
    public EntityHider entityHider;

    private static final int TICKS_PER_SECOND = 20;

    private AnimalsArrow instance;

    private Map<Player, ScoreboardSign> boards;

    private Map<Entity, Entity> customprojectiles;
    private Map<String, String> pluginoptions;

    public AnimalsArrow getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        entityHider = new EntityHider(this, EntityHider.Policy.BLACKLIST);

        this.boards = new HashMap<>();
        this.customprojectiles = new HashMap<>();
        this.pluginoptions = new HashMap<>();

        pluginoptions.put("enable", getConfig().getString("enable"));
        ConfigurationSection extrasection = getConfig().getConfigurationSection("extra");
        for(String extra : getConfig().getConfigurationSection("extra").getKeys(false)){
            getPluginoptions().put("extra." + extra, extrasection.getString(extra));
        }

        getPluginoptions().put("automount", getConfig().getString("automount.enable"));

        ConfigurationSection EntitiesSection = getConfig().getConfigurationSection("automount.entities");
        for(String entity : EntitiesSection.getKeys(false)){
            if(entity.equals("horses")){
                getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity + ".enable"));
                System.out.println(EntitiesSection.getString(entity + ".enable"));
                getPluginoptions().put("automount.entities.horses.auto_tame" + entity, EntitiesSection.getString(entity + ".auto_tame"));
                System.out.println(EntitiesSection.getString(entity + ".auto_tame"));
                getPluginoptions().put("automount.entities.horses.auto_saddle" + entity, EntitiesSection.getString(entity + ".auto_saddle"));
                System.out.println(EntitiesSection.getString(entity + ".auto_saddle"));
            } else {
                getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity));
            }
        }

        getServer().getPluginManager().registerEvents(new AnimalsArrowEvent(this), this);

        System.out.println("[AnimalsArrow] Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("[AnimalsArrow] Plugin Disabled!");
    }

    private static final Random RANDOM = new Random();

    public Map<Player, ScoreboardSign> getBoards(){
        return this.boards;
    }

    public void  throwItem(Player player, ItemStack it, Entity e) {
        Entity projectile = null;
        Egg egg = null;
        Snowball snowball = null;
        Location loc = e.getLocation();
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());

        boolean item = true;

        if(it.getType().toString().endsWith("SPAWN_EGG")){
            if(Boolean.parseBoolean(getPluginoptions().get("extra.spawn_eggs"))){
                item = false;

                projectile = e.getWorld().spawnEntity(loc, Objects.requireNonNull(EntityType.fromName(it.getType().toString().replace("_SPAWN_EGG", ""))));
            }
        }

        if(it.getType() == Material.EGG){
            if(Boolean.parseBoolean(getPluginoptions().get("extra.eggs"))){
                item = false;

                egg = (Egg) e.getWorld().spawnEntity(loc, EntityType.EGG);
            }
        }

        if(it.getType() == Material.SNOWBALL){
            if(Boolean.parseBoolean(getPluginoptions().get("extra.snowballs"))){
                item = false;

                snowball = (Snowball) e.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
            }
        }

        if(it.getType().toString().contains("BOAT")){
            if(Boolean.parseBoolean(getPluginoptions().get("extra.boats"))){
                item = false;

                Boat boat = (Boat) e.getWorld().spawnEntity(loc, EntityType.BOAT);
                TreeSpecies treespecies = TreeSpecies.GENERIC;
                if(it.getType() == Material.ACACIA_BOAT){ treespecies = TreeSpecies.ACACIA; }
                if(it.getType() == Material.BIRCH_BOAT){ treespecies = TreeSpecies.BIRCH; }
                if(it.getType() == Material.DARK_OAK_BOAT){ treespecies = TreeSpecies.DARK_OAK; }
                if(it.getType() == Material.JUNGLE_BOAT){ treespecies = TreeSpecies.JUNGLE; }
                if(it.getType() == Material.SPRUCE_BOAT){ treespecies = TreeSpecies.REDWOOD; }
                if(it.getType() == Material.OAK_BOAT){ treespecies = TreeSpecies.GENERIC; }
                boat.setWoodType(treespecies);
                projectile = boat;
            }
        }

        if(item){
            projectile = e.getWorld().dropItem(loc, it);
        }


        if(projectile != null ) {
            projectile.addScoreboardTag("AnimalArrow");
            customprojectiles.put(e, projectile);

            projectile.setVelocity(e.getVelocity());

            if(projectile instanceof Vehicle){
                if(Boolean.parseBoolean(getPluginoptions().get("automount"))){
                    boolean mount = false;

                    if(projectile instanceof Horse && Boolean.parseBoolean(getPluginoptions().get("automount.entities.horses"))){
                        mount = true;
                        if(Boolean.parseBoolean(getPluginoptions().get("automount.entities.horses.auto_tame"))){
                            ((Horse) projectile).setTamed(true);
                            ((Horse) projectile).setLeashHolder(player);
                        }

                        if(Boolean.parseBoolean(getPluginoptions().get("automount.entities.horses.auto_saddle"))){
                            ((Horse) projectile).getInventory().setSaddle(new ItemStack(Material.SADDLE));
                        }
                    }

                    if(projectile instanceof Boat && Boolean.parseBoolean(getPluginoptions().get("automount.entities.boats"))){
                        mount = true;
                    }

                    if(mount){
                        projectile.setPassenger(player);
                    }
                }
            }

            if(!(projectile instanceof Flying) && !(projectile instanceof Chicken)){
                if(projectile instanceof LivingEntity){
                    e.setPassenger(projectile);
                    ((LivingEntity) projectile).setAI(false);
                    ((LivingEntity) projectile).setCollidable(false);
                }
            }
        }

        if(egg != null) {
            egg.setVelocity(e.getVelocity());
            e.setPassenger(egg);
        }

        if(snowball != null) {
            snowball.setVelocity(e.getVelocity());
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

    public Map<Entity, Entity> getCustomprojectiles() {
        return customprojectiles;
    }

    public Map<String, String> getPluginoptions() {
        return pluginoptions;
    }
}