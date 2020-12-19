package fr.darkbow_.monombre;

import org.bukkit.entity.Player;

public class Point {
    private MonOmbre main;

    public Point(MonOmbre monombre){this.main = monombre;}

    Player player;
    Point(Player player){
        this.player = player;
        main.getPoints().put(player, this);
    }

    public Player getPlayer(){
        return this.player;
    }
}