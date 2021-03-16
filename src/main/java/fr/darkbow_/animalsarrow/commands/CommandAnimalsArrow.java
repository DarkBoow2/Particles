package fr.darkbow_.animalsarrow.commands;

import fr.darkbow_.animalsarrow.AnimalsArrow;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class CommandAnimalsArrow implements CommandExecutor {
    private AnimalsArrow main;

    public CommandAnimalsArrow(AnimalsArrow animalsArrow){this.main = animalsArrow;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        String wrongcommand = "§cWrong command.\n§7Execute §8/aa help §7to see the list of commands.";
        if(args.length == 0){
            sender.sendMessage("§7Execute §8/aa help §7to see the list of commands.");
        }

        if(args.length == 1){
            if(args[0].equals("toggle")){
                main.getPluginoptions().put("enable", String.valueOf(!Boolean.parseBoolean(main.getPluginoptions().get("enable"))));
                main.getConfig().set("enable", Boolean.parseBoolean(main.getPluginoptions().get("enable")));
                main.saveConfig();
                sender.sendMessage("§7The Plugin is now " + main.bool(Boolean.parseBoolean(main.getPluginoptions().get("enable"))) + "§7.");
            } else if(args[0].equalsIgnoreCase("reload")) {
                main.getConfig().options().copyDefaults(true);
                main.saveConfig();

                main.getPluginoptions().put("projectile-rides-arrow", main.getConfig().getString("projectile-rides-arrow"));
                main.getPluginoptions().put("player-launch", main.getConfig().getString("player-launch"));
                ConfigurationSection extrasection = main.getConfig().getConfigurationSection("extra");
                for(String extra : main.getConfig().getConfigurationSection("extra").getKeys(false)){
                    main.getPluginoptions().put("extra." + extra, extrasection.getString(extra));
                }

                main.getPluginoptions().put("automount", main.getConfig().getString("automount.enable"));

                ConfigurationSection EntitiesSection = main.getConfig().getConfigurationSection("automount.entities");
                for(String entity : EntitiesSection.getKeys(false)){
                    if(entity.equals("horses")){
                        main.getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity + ".enable"));
                        System.out.println(EntitiesSection.getString(entity + ".enable"));
                        main.getPluginoptions().put("automount.entities.horses.auto_tame" + entity, EntitiesSection.getString(entity + ".auto_tame"));
                        System.out.println(EntitiesSection.getString(entity + ".auto_tame"));
                        main.getPluginoptions().put("automount.entities.horses.auto_saddle" + entity, EntitiesSection.getString(entity + ".auto_saddle"));
                        System.out.println(EntitiesSection.getString(entity + ".auto_saddle"));
                    } else {
                        main.getPluginoptions().put("automount.entities." + entity, EntitiesSection.getString(entity));
                    }
                }

                sender.sendMessage("§aConfiguration Reloaded Successfully!");
            } else if(args[0].equalsIgnoreCase("help")) {
                sender.sendMessage("§7§l---> §6§lAnimals Arrow §b§lHelp §7§l<---" +
                        "\n§a/aa help §7-> §3Get this help message." +
                        "\n§a/aa toggle §7-> §3Toggle the plugin status to §aON §7or §cOFF." +
                        "\n§a/aa reload §7-> §3Reload the Configuration File.");
            } else {
                sender.sendMessage(wrongcommand);
            }
        }

        if(args.length > 1){
            sender.sendMessage(wrongcommand);
        }

        return false;
    }
}
