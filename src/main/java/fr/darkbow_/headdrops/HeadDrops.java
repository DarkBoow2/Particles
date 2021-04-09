package fr.darkbow_.headdrops;

import org.bukkit.plugin.java.JavaPlugin;

public class HeadDrops extends JavaPlugin {

    private HeadDrops instance;

    public HeadDrops getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new HeadDropsEvent(this), this);

        System.out.println("[HeadDrops] Plugin ON!");
    }

    @Override
    public void onDisable() {
        System.out.println("[HeadDrops] Plugin OFF!");
    }
}