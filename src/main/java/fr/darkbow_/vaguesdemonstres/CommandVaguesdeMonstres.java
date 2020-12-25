package fr.darkbow_.vaguesdemonstres;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVaguesdeMonstres implements CommandExecutor {
    private VaguesdeMonstres main;
    
    public CommandVaguesdeMonstres(VaguesdeMonstres vaguesdemonstres){this.main = vaguesdemonstres;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("start")){
                if(main.VaguesdeMonstres){
                    sender.sendMessage("§cLes vagues de Monstres sont déjà activées.");
                } else {
                    main.monstresbasiques = main.monstresbasiquesinitial;
                    main.monstresvener = main.monstresvenerinitial;
                    main.VaguesdeMonstres = true;
                    VaguesdeMonstres.task = new Taches(main.getInstance()).runTaskTimer(main.getInstance(), 20L, 20L);
                    Bukkit.broadcastMessage("§bVagues de Monstres §6§l§bActivées §b!!");
                }
            }

            if(args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("resume")){
                if(main.VaguesdeMonstres){
                    Bukkit.broadcastMessage("§cLes vagues de Monstres ne sont pas activées.");
                } else {
                    main.VaguesdeMonstres = true;
                    main.EstEnPause = false;
                    VaguesdeMonstres.task = new Taches(main.getInstance()).runTaskTimer(main.getInstance(), 20L, 20L);
                    sender.sendMessage("§cVagues de Monstres Réactivées.");
                }
            }

            if(args[0].equalsIgnoreCase("pause")){
                if(main.VaguesdeMonstres){
                    if(main.EstEnPause){
                        Bukkit.broadcastMessage("§bT'as trouvé le trick, §6§lGG");
                    } else {
                        Bukkit.broadcastMessage("§bTu crois vraiment que tu peux faire pause aussi facilement ?? §6§lLOL");
                    }
                } else {
                    sender.sendMessage("§cLes vagues de Monstres ne sont pas activées.");
                }
            }

            if(args[0].equalsIgnoreCase("esuap")){
                if(main.VaguesdeMonstres){
                    if(main.EstEnPause){
                        Bukkit.broadcastMessage("§bLes Vagues de Monstres sont déjà en Pause !");
                    } else {
                        main.EstEnPause = true;
                        VaguesdeMonstres.task.cancel();
                        Bukkit.broadcastMessage("§bVagues de Monstres mises en pause.");
                    }
                } else {
                    sender.sendMessage("§cLes vagues de Monstres ne sont pas activées.");
                }
            }

            if(args[0].equalsIgnoreCase("stop")){
                if(main.VaguesdeMonstres){
                    main.VaguesdeMonstres = false;
                    main.EstEnPause = false;
                    VaguesdeMonstres.task.cancel();
                    main.monstresbasiques = main.monstresbasiquesinitial;
                    main.monstresvener = main.monstresvenerinitial;
                    if(!main.getSurvivants().isEmpty()){
                        main.getSurvivants().clear();
                        main.getMonstres().clear();
                    }

                    main.timer = 0;

                    Bukkit.broadcastMessage("§bVagues de Monstres §6§l§bDésactivées §b!!");
                } else {
                    sender.sendMessage("§cLes Vagues de Monstres ne sont pas Activées.");
                }
            }
        }

        return false;
    }
}