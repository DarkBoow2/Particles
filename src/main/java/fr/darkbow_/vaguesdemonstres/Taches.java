package fr.darkbow_.vaguesdemonstres;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Taches extends BukkitRunnable {

    private VaguesdeMonstres main;
    public Taches(VaguesdeMonstres vaguesdemonstres) {this.main = vaguesdemonstres;}

    @Override
    public void run() {
        Random r = new Random();
        //Toutes les 3 minutes y a un zombie, squelette, creeper, enderman ou araignée en plus

        if((main.timer/60) % 10 == 0){
            main.monstresbasiques = Math.round(main.monstresbasiques/2);
            main.monstresvener = (int) Math.round(main.monstresvener/1.5);

            Bukkit.broadcastMessage("Les monstres apparaissent de plus en plus souvent maintenant !");
        }


        if((main.timer) % main.monstresbasiques == 0){
            boolean lightning_creeper = false;

            for(Player pls : Bukkit.getOnlinePlayers()){
                if(main.getSurvivants().contains(pls)){
                    int entite = 1;
                    if(!main.getMonstres().isEmpty()){
                        entite = r.nextInt(main.getMonstres().get(pls).size());
                    }

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

                    if(etype == EntityType.CREEPER){
                        lightning_creeper = r.nextBoolean();
                    }

                    for(EntityType entitytype : main.getMonstres().get(pls)){
                        Entity entity = pls.getWorld().spawnEntity(pls.getLocation(), entitytype);
                        if(entitytype == EntityType.CREEPER){
                            lightning_creeper = r.nextBoolean();
                            if(lightning_creeper){
                                ((Creeper) entity).setPowered(true);
                            }
                        }
                    }
                }
            }
        }

        if((main.timer) % main.monstresvener == 0){
            for(Player pls : Bukkit.getOnlinePlayers()){
                int entite = 15;

                EntityType etype = null;
                entite = r.nextInt(22);

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

                if(etype != null){
                    pls.getWorld().spawnEntity(pls.getLocation(), etype);
                    main.getMonstres().get(pls).add(etype);
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

        main.timer++;
    }
}