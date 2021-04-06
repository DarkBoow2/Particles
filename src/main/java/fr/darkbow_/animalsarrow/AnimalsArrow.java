package fr.darkbow_.animalsarrow;

import fr.darkbow_.animalsarrow.commands.CommandAnimalsArrow;
import fr.darkbow_.animalsarrow.scoreboard.ScoreboardSign;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class AnimalsArrow extends JavaPlugin {
    public EntityHider entityHider;

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

        String status = null;
        pluginoptions.put("enable", getConfig().getString("enable"));
        if(!Boolean.parseBoolean(pluginoptions.get("enable"))){
            status = " But Not Activated Yet!\nChange it in the config and restart your server or execute the /aa toggle command when the server will be completely started.";
        }
        pluginoptions.put("projectile-rides-arrow", getConfig().getString("projectile-rides-arrow"));
        pluginoptions.put("player-launch", getConfig().getString("player-launch"));
        ConfigurationSection extrasection = getConfig().getConfigurationSection("extra");
        if(extrasection != null){
            for(String extra : Objects.requireNonNull(getConfig().getConfigurationSection("extra")).getKeys(false)){
                getPluginoptions().put("extra." + extra, extrasection.getString(extra));
            }
        }

        getPluginoptions().put("automount", getConfig().getString("automount.enable"));

        ConfigurationSection EntitiesSection = getConfig().getConfigurationSection("automount.entities");
        if(EntitiesSection != null){
            for(String entity : EntitiesSection.getKeys(false)){
                if(entity.equals("horses")){
                    getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity + ".enable"));
                    getPluginoptions().put("automount.entities.horses.auto_tame" + entity, EntitiesSection.getString(entity + ".auto_tame"));
                    getPluginoptions().put("automount.entities.horses.auto_saddle" + entity, EntitiesSection.getString(entity + ".auto_saddle"));
                } else {
                    getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity));
                }
            }
        }

        getServer().getPluginManager().registerEvents(new AnimalsArrowEvent(this), this);
        Objects.requireNonNull(getCommand("animalsarrow")).setExecutor(new CommandAnimalsArrow(this));

        System.out.println("[AnimalsArrow] Plugin ON!" + status);
    }

    @Override
    public void onDisable() {
        System.out.println("[AnimalsArrow] Plugin OFF!");
    }

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

        if(it == null || it.getType() == Material.AIR){
            item = false;
            if(Boolean.parseBoolean(getPluginoptions().get("player-launch"))){
                projectile = player;
            }
        } else {
            it.setAmount(1);
            if(it.getType().toString().endsWith("SPAWN_EGG")){
                if(Boolean.parseBoolean(getPluginoptions().get("extra.spawn_eggs"))){
                    item = false;

                    projectile = e.getWorld().spawnEntity(loc, Objects.requireNonNull(EntityType.fromName(it.getType().toString().replace("_SPAWN_EGG", ""))));
                }
            }

            if(it.getType() == Material.EGG){
                if(Boolean.parseBoolean(getPluginoptions().get("extra.eggs"))){
                    item = false;

                    if(Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                        egg = (Egg) e.getWorld().spawnEntity(loc, EntityType.EGG);
                    } else {
                        egg = player.launchProjectile(Egg.class);
                    }
                }
            }

            if(it.getType() == Material.SNOWBALL){
                if(Boolean.parseBoolean(getPluginoptions().get("extra.snowballs"))){
                    item = false;

                    if(getPluginoptions().get("launch-mode").equalsIgnoreCase("riding")){
                        snowball = (Snowball) e.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
                    } else {
                        snowball = player.launchProjectile(Snowball.class);
                    }
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
        }

        if(item){
            projectile = e.getWorld().dropItem(loc, it);
        }

        if(projectile != null) {
            projectile.setVelocity(e.getVelocity());

            if(projectile instanceof Player){
                if(!Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                    projectile.addScoreboardTag("AnimalArrow");
                }
            } else {
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

                if(Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                    if(projectile instanceof LivingEntity){
                        ((LivingEntity) projectile).setAI(false);
                        ((LivingEntity) projectile).setCollidable(false);
                        projectile.addScoreboardTag("AnimalArrow");
                    }
                } else {
                    if(!(projectile instanceof Flying) && !(projectile instanceof Chicken) && !(projectile instanceof Cat)){
                        if(projectile instanceof LivingEntity){
                            projectile.addScoreboardTag("AnimalArrow");
                        }
                    }
                }
            }

            if(Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                e.setPassenger(projectile);
            }

            customprojectiles.put(e, projectile);
        }

        if(egg != null) {
            customprojectiles.put(e, egg);
            egg.setVelocity(e.getVelocity());
            if(Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                e.setPassenger(egg);
            } else {
                egg.addScoreboardTag("AnimalArrow");
            }
        }

        if(snowball != null) {
            customprojectiles.put(e, snowball);
            snowball.setVelocity(e.getVelocity());
            if(Boolean.parseBoolean(getPluginoptions().get("projectile-rides-arrow"))){
                e.setPassenger(snowball);
            } else {
                snowball.addScoreboardTag("AnimalArrow");
            }
        }
    }

    public String bool(boolean b){
        String bool = null;
        if(b){
            bool = ChatColor.GREEN + "§a§lEnabled";
        } else {
            bool = ChatColor.RED + "§c§lDisabled";
        }

        return bool;
    }

    public boolean consumeItem(Player player, int count, Material mat) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }

    public Map<Entity, Entity> getCustomprojectiles() {
        return customprojectiles;
    }

    public Map<String, String> getPluginoptions() {
        return pluginoptions;
    }
}