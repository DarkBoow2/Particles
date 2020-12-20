package fr.darkbow_.monombre;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private MonOmbre main;

    public Joueur(MonOmbre monombre){this.main = monombre;}
    private List<Player> playerpoints = new ArrayList<Player>();

    Player player;
    Joueur(Player player){
        this.player = player;
    }

    public List<Player> getPoints(){
        return this.playerpoints;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void addPoint(Player player){
        playerpoints.add(player);
    }
}