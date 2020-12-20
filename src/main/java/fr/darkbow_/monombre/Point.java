package fr.darkbow_.monombre;

import org.bukkit.entity.Player;

public class Point {
    private MonOmbre main;

    public Point(MonOmbre monombre){this.main = monombre;}

    private Player player;


    Point(Player player){
        this.player = player;
        boolean etat = false;

        for(Joueur joueur : main.getJoueurs()){
            if(joueur.getPlayer() == player){
                etat = true;
                joueur.getPoints().add(this);
            }
        }

        if(!etat){
            Joueur j2 = new Joueur(player);
            main.getJoueurs().add(j2);
            j2.getPoints().add(this);
        }
    }
}