package fr.darkbow_.vaguesdemonstres;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                    main.VaguesdeMonstres = true;
                    VaguesdeMonstres.task = new Taches(main.getInstance()).runTaskTimer(main.getInstance(), 20L, 20L);
                    Bukkit.broadcastMessage("§bVagues de Monstres §6§l§bActivées §b!!");
                }
            }

            if(args[0].equalsIgnoreCase("stop")){
                if(main.VaguesdeMonstres){
                    main.VaguesdeMonstres = false;
                    VaguesdeMonstres.task.cancel();
                    Bukkit.broadcastMessage("§bVagues de Monstres §6§l§bDésactivées §b!!");
                } else {
                    sender.sendMessage("§cLes vagues de Monstres ne sont pas activées.");
                }
            }
        }

        return false;
    }
}