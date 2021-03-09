package fr.darkbow_.animalsarrow;

import fr.darkbow_.animalsarrow.scoreboard.ScoreboardSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnimalsArrow extends JavaPlugin {
    private AnimalsArrow instance;

    private Map<Player, ScoreboardSign> boards;

    public AnimalsArrow getInstance() {
        return this.instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.boards = new HashMap<>();

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