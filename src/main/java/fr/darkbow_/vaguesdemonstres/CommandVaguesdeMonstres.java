package fr.darkbow_.vaguesdemonstres;

import fr.darkbow_.vaguesdemonstres.scoreboard.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandVaguesdeMonstres implements CommandExecutor {
    private VaguesdeMonstres main;
    
    public CommandVaguesdeMonstres(VaguesdeMonstres vaguesdemonstres){this.main = vaguesdemonstres;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(args.length == 1){
            if(sender.hasPermission("vaguesdemonstres.admin")){
                if(args[0].equalsIgnoreCase("start")){
                    if(main.VaguesdeMonstres){
                        sender.sendMessage("§cLes vagues de Monstres sont déjà activées.");
                    } else {
                        main.monstresbasiques = main.monstresbasiquesinitial;
                        main.monstresvener = main.monstresvenerinitial;
                        main.VaguesdeMonstres = true;
                        main.EstEnPause = false;
                        VaguesdeMonstres.task = new Taches(main.getInstance()).runTaskTimer(main.getInstance(), 20L, 20L);
                        Bukkit.broadcastMessage("§b§lVagues de Monstres §6§l§bActivées §b!!");
                        for(Player pls : Bukkit.getOnlinePlayers()){
                            if(main.VeutVoirInfos(pls) && !main.getBoards().containsKey(pls)) {
                                ScoreboardSign sb = new ScoreboardSign(pls, "§c§lVagues de Monstres");
                                sb.create();
                                main.getBoards().put(pls, sb);
                            }
                        }

                        if(main.VeulentVoirInfos().containsValue(true)){
                            for(Map.Entry<Player, ScoreboardSign> boards : main.getBoards().entrySet()){
                                boards.getValue().setLine(0, "§e");
                                boards.getValue().setLine(1, ChatColor.GOLD + "Timer : " + ChatColor.WHITE + "0s");
                                boards.getValue().setLine(2, "§b");
                                boards.getValue().setLine(3, ChatColor.AQUA + "Horde : " + ChatColor.WHITE + "0 " + "Monstre");

                                boards.getValue().setLine(4, "§d");

                                int tempsrestant = -1;
                                if(main.timer%main.monstresbasiques >= main.timer%main.monstresvener){
                                    tempsrestant = main.timer%main.monstresbasiques;
                                } else {
                                    tempsrestant = main.timer%main.monstresvener;
                                }

                                boards.getValue().setLine(5, ChatColor.BLUE + "Prochain Spawn : " + ChatColor.WHITE + main.getTimeFormat(tempsrestant));
                            }
                        }
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

                        if(main.getBoards().isEmpty()){
                            for(Player pls : main.getBoards().keySet()){
                                main.getBoards().get(pls).destroy();
                                main.getBoards().remove(pls);
                            }
                        }

                        main.timer = 0;

                        Bukkit.broadcastMessage("§bVagues de Monstres §6§l§bDésactivées §b!!");
                    } else {
                        sender.sendMessage("§cLes Vagues de Monstres ne sont pas Activées.");
                    }
                }
            }

            if(args[0].equalsIgnoreCase("tab") || args[0].equalsIgnoreCase("infos") || args[0].equalsIgnoreCase("scoreboard")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    main.VeulentVoirInfos().put(player, !main.VeutVoirInfos(player));
                    player.sendMessage(ChatColor.BLUE + "Scoreboard Personnel " + main.bool(main.VeutVoirInfos(player)));

                    if(main.VeutVoirInfos(player)){
                        ScoreboardSign sb = new ScoreboardSign(player, "§c§lVagues de Monstres");
                        sb.create();
                        main.getBoards().put(player, sb);
                    } else {
                        if(main.getBoards().containsKey(player)){
                            main.getBoards().get(player).destroy();
                            main.getBoards().remove(player);
                        }
                    }
                } else {
                    sender.sendMessage("§cSeuls les Joueurs peuvent exécuter cette commande.");
                }
            }
        }

        return false;
    }
}