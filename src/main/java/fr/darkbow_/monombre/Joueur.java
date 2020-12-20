package fr.darkbow_.monombre;

import org.bukkit.entity.Player;

import java.util.List;

public class Joueur {
    private MonOmbre main;
    int test = 0;

    public Joueur(MonOmbre monombre){this.main = monombre;}
    private List<Point> playerpoints;

    Player player;
    Joueur(Player player){
        this.player = player;
    }

    public List<Point> getPoints(){
        return this.playerpoints;
    }

    public Player getPlayer(){
        return this.player;
    }
}