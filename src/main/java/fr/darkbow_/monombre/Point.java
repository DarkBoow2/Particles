package fr.darkbow_.monombre;

import org.bukkit.entity.Player;

public class Point {
    private MonOmbre main;

    public Point(MonOmbre monombre){this.main = monombre;}

    private Player player;


    Point(Player player){
        this.player = player;
        if(!main.getJoueurs().contains(new Joueur(player))){
            main.getJoueurs().add(new Joueur(player));
        }

        for(Joueur joueur : main.getJoueurs()){
            if(joueur.getPlayer() == player){
                joueur.getPoints().add(this);
            }
        }
    }
}