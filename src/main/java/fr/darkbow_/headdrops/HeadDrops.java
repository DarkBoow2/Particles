package fr.darkbow_.headdrops;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class HeadDrops extends JavaPlugin {

    private HeadDrops instance;

    public HeadDrops getInstance() {
        return instance;
    }

    public static BukkitTask task;
    private Map<Player, Integer> particles;

    @Override
    public void onEnable() {
        instance = this;

        this.particles = new HashMap<>();

        getServer().getPluginManager().registerEvents(new HeadDropsEvent(this), this);

        System.out.println("[HeadDrops] Plugin ON!");
    }

    @Override
    public void onDisable() {
        System.out.println("[HeadDrops] Plugin OFF!");
    }

    public Map<Player, Integer> getParticles() {
        return particles;
    }
}