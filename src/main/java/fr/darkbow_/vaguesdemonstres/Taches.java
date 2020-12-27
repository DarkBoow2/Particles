package fr.darkbow_.vaguesdemonstres;

import fr.darkbow_.vaguesdemonstres.scoreboard.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Taches extends BukkitRunnable {

    private VaguesdeMonstres main;
    public Taches(VaguesdeMonstres vaguesdemonstres) {this.main = vaguesdemonstres;}

    @Override
    public void run() {
        if(main.VaguesdeMonstres && !main.EstEnPause){
            if(Bukkit.getOnlinePlayers().size() > 0){
                Random r = new Random();
                //Toutes les 3 minutes y a un zombie, squelette, creeper, enderman ou araignée en plus

                if(main.timer > 0){
                    if(main.timer%main.monstresbasiques == 0){
                        boolean lightning_creeper;

                        if(Bukkit.getOnlinePlayers().size() > 0){
                            for(Player pls : Bukkit.getOnlinePlayers()){
                                if(!main.getSurvivants().contains(pls)){
                                    main.getSurvivants().add(pls);
                                    List<EntityType> entitieslist = new ArrayList<>();
                                    main.getMonstres().put(pls, entitieslist);
                                }

                                if(main.getSurvivants().contains(pls)){
                                    if(main.monstresbasiques > 5 && main.monstresvener > 10){
                                        int entite = 7;
                                        entite = r.nextInt(6);

                                        EntityType etype = null;

                                        switch (entite){
                                            case 0:
                                                etype = EntityType.ZOMBIE;
                                                break;
                                            case 1:
                                                etype = EntityType.SKELETON;
                                                break;
                                            case 2:
                                                etype = EntityType.SPIDER;
                                                break;
                                            case 3:
                                                etype = EntityType.CAVE_SPIDER;
                                                break;
                                            case 4:
                                                etype = EntityType.ENDERMAN;
                                                break;
                                            case 5:
                                                etype = EntityType.CREEPER;
                                                break;
                                        }

                                        main.getMonstres().get(pls).add(etype);


                                    }

                                    if(!main.getMonstres().get(pls).isEmpty()){
                                        for(EntityType entitytype : main.getMonstres().get(pls)){
                                            Entity entity = VaguesdeMonstres.spawnEntity(entitytype, pls.getLocation(), 5, 3);
                                            if(entitytype == EntityType.CREEPER){
                                                lightning_creeper = r.nextBoolean();
                                                if(lightning_creeper){
                                                    ((Creeper) entity).setPowered(true);
                                                }
                                            }
                                        }
                                    }
                                }

                                pls.sendMessage("§bSpawn de la Horde de Monstres Basiques");
                            }
                        }
                    }

                    if(main.timer%main.monstresvener == 0){
                        for(Player pls : Bukkit.getOnlinePlayers()){
                            if(!main.getSurvivants().contains(pls)){
                                main.getSurvivants().add(pls);
                                List<EntityType> entitieslist = new ArrayList<>();
                                main.getMonstres().put(pls, entitieslist);
                            }

                            int entite = r.nextInt(22);

                            EntityType etype = null;

                            switch (entite){
                                case 0: case 1:
                                    etype = EntityType.BLAZE;
                                    break;
                                case 2: case 3:
                                    etype = EntityType.EVOKER;
                                    break;
                                case 4: case 5:
                                    etype = EntityType.ILLUSIONER;
                                    break;
                                case 6: case 7:
                                    etype = EntityType.PHANTOM;
                                    break;
                                case 8: case 9:
                                    etype = EntityType.PILLAGER;
                                    break;
                                case 10:
                                    etype = EntityType.RAVAGER;
                                    break;
                            }

                            if(etype == null){
                                pls.sendMessage("§cAucun Monstre Terrifiant n'est apparu, c'est Dommage...");
                            } else {
                                Entity entity = VaguesdeMonstres.spawnEntity(etype, pls.getLocation(), 5, 3);
                                if(main.timer >= 3600){
                                    main.getMonstres().get(pls).add(etype);
                                    pls.sendMessage("§cSpawn du Monstre Terrifiant §b§l+ §cAjout du Monstre à la §lHorde de monstres basiques\n§bT'as pas Fini le jeu assez vite, §6§lCHEH !!");
                                } else {
                                    pls.sendMessage("§cSpawn du Monstre Terrifiant");
                                }
                            }
                        }
                    }
                }

                //Toutes les 20 minutes y a 50% de chances d'avoir un ravageur ou un autre monstre super pété



                //Règles (choisir 4 règles au hasard (selon leur niveau de difficulté)
                //1 : 4 zombies toutes les 5 minutes
                //2 : 3 creepers toutes les 10 minutes
                //3 : 2 monstres random toutes les 5 minutes
                //4 : 4 monstres random au début mais pareil après toutes les 30 minutes
                //5 : 1 Boss toutes les heures
                //6 : 1 Enderman invisible toutes les 20 minutes
                //7 : 1 loup toutes les 30 minutes
                //8 : 1 Blaze toutes les 20 minutes

                //Les règles changent toutes les heures vers le temps/2 des autres règles
                //Exemple : 4 zombies toutes les 5 minutes = 4 zombies toutes les 2min30

                for(Map.Entry<Player, ScoreboardSign> boards : main.getBoards().entrySet()){
                    boards.getValue().setLine(0, "§e");
                    boards.getValue().setLine(1, "§6Timer : §r" + main.getTimeFormat(main.timer));
                    boards.getValue().setLine(2, "§b");

                    int monstres = 0;
                    if(main.getMonstres().containsKey(boards.getKey())){
                        if(main.getMonstres().get(boards.getKey()).size() > 0){
                            monstres = main.getMonstres().get(boards.getKey()).size();
                        }
                    }
                    String pluriel = "";
                    if(monstres > 1){
                        pluriel = "s";
                    }

                    boards.getValue().setLine(3, ChatColor.AQUA + "Horde : " + ChatColor.WHITE + monstres + " " + "Monstre" + pluriel);
                    boards.getValue().setLine(4, "§d");

                    int tempsrestant = -1;
                    int restantbasique = -1;
                    int restantvener = -1;

                    restantbasique = main.monstresbasiques - main.timer%main.monstresbasiques;
                    restantvener = main.monstresvener - main.timer%main.monstresvener;
                    if(restantbasique <= restantvener){
                        tempsrestant = restantbasique;
                    } else {
                        tempsrestant = restantvener;
                    }

                    boards.getValue().setLine(5, ChatColor.BLUE + "Prochain Spawn : " + ChatColor.WHITE + main.getTimeFormat(tempsrestant));
                }

                main.timer++;
            }
        } else {
            cancel();
        }
    }
}