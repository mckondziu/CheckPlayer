package pl.minecraftkondziu;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class checkCommands implements CommandExecutor {
    public static HashMap<String, String> sprawdzany = new HashMap<>();
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
            checkplayer.LOGGER.log(Level.INFO, checkData.prefix + "Ta komenda jest tylko dla gracza!");
            return true;
        }
        Player p = (Player) sender;
        if(!(p.hasPermission("checkplayer.help"))){
    		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
    				+ChatColor.GRAY+ "(checkplayer.help)");
    		return false;
    	}
		if(args.length == 0) {
			p.sendMessage("");
            p.sendMessage(ChatColor.YELLOW + "Help [/check /sprawdz /spr /cp]:");
            p.sendMessage(ChatColor.YELLOW + "cheater" + ChatColor.RED + " - " + ChatColor.GREEN + "Zbanuj danego gracza za cheaty");
            p.sendMessage(ChatColor.YELLOW + "player" + ChatColor.RED + " - " + ChatColor.GREEN + "Sprawdz gracza. Teleportuje go do pokoju sprawdzajacego");
            p.sendMessage(ChatColor.YELLOW + "clear" + ChatColor.RED + " - " + ChatColor.GREEN + "Oznacz gracza, jako brak cheatów!");
            p.sendMessage(ChatColor.YELLOW + "cwand" + ChatColor.RED + " - " + ChatColor.GREEN + "Rozdzka do sprawdzania graczy!");
            p.sendMessage(ChatColor.YELLOW + "setspawn" + ChatColor.RED + " - " + ChatColor.GREEN + "Ustaw lokalizacje pokoju sprawdzajacego lub spawnu");
            p.sendMessage("");
            return false;   
        }
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("cheater")) {
				if( !(p.hasPermission("checkplayer.cheater"))){
					p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.cheater)");
					return false;
				}
				p.sendMessage(ChatColor.RED + "Uzycie: /check cheater <gracz>");
			}
            if(args[0].equalsIgnoreCase("player")) {
            	if(!(p.hasPermission("checkplayer.player"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.player)");
            		return false;
            	}
            	p.sendMessage(ChatColor.RED + "Uzycie: /check player <gracz>");
            }
            if(args[0].equalsIgnoreCase("clear")) {
            	if(!(p.hasPermission("checkplayer.clear"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.clear)");
            		return false;
            	}
                p.sendMessage(ChatColor.RED + "Uzycie: /check clear <gracz>");

            }
            if(args[0].equalsIgnoreCase("cwand")) {
            	if(!(p.hasPermission("checkplayer.cwand"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.cwand)");
            		return false;
            	}
            	ItemStack is = new ItemStack(Material.BLAZE_ROD);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.RED + "Kliknij lewym, aby sprawdzic gracza!");
                is.setItemMeta(im);
                p.getInventory().addItem(is);
                p.sendMessage(ChatColor.RED + "Dostales/as rozdzke do sprawdzania graczy!");
            }
            if(args[0].equalsIgnoreCase("setspawn")) {
            	if(!(p.hasPermission("checkplayer.setspawn"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.setspawn)");
            		return false;
            	}
                p.sendMessage(ChatColor.GRAY + "Uzycie: /check setspawn <spawn|checkroom>");
            }
		}
		
		// =================================
		// takie tam odzmaczenie argumentu 2
		// =================================
		
		if (args.length == 2) {
			
			Player target = Bukkit.getPlayer(args[1]);
			String spr = args[1];
			
			if(args[0].equalsIgnoreCase("cheater")) {
				if( !(p.hasPermission("checkplayer.cheater"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.cheater)");
            		return false;
            	}
        		if (target == null){
        			p.sendMessage(checkData.prefix + "Gracz o nicku " + args[1] + " nie ma obecnie na serwerze!");
        			return false;
        		}
        		Bukkit.broadcastMessage(checkData.colorCodes(translate(checkData.banforcheats, p.getName(), target.getName())));
        		String kickmessage = checkData.colorCodes(translate(checkData.banforcheatstoplayer, p.getName(), target.getName()));
        		checkCommands.sprawdzany.remove(target.getName());
        		Config.getConfig("bans").set(target.getName(), kickmessage);
        		target.kickPlayer(kickmessage);
        		Config.saveAll();
        		Location loc = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+spr));
	            target.teleport(loc);
	            Config.getConfig("locations").set("gracz." + spr, null);
	            Config.saveAll();
			}
			if(args[0].equalsIgnoreCase("player")) {
				if(!(p.hasPermission("checkplayer.player"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.player)");
            		return false;
            	}
        		if (target == null) {
        			p.sendMessage(checkData.prefix + "Gracz o nicku " + args[1] + " nie jest obecnie na serwerze!");
        			return true;
        		}

                Location loc = target.getLocation();
                Config.getConfig("locations").set("gracz."+spr, checkData.locationToString(loc));
                Config.saveAll();
        		target.setFireTicks(0);
        		try {
        			target.teleport(checkData.checkroom);
        			p.teleport(checkData.checkroom);
        		}catch (Exception ex) {
        			p.sendMessage("Spawn nie jest ustawiony! Aby ustawic wpisz /check setspawn checkroom");
        		}
        		Bukkit.broadcastMessage(checkData.colorCodes(checkData.broadcastcheckplayer.replace("{PLAYER}", target.getName())));
        		for (int i = 0; i < checkData.spam; i++) {
        			target.sendMessage(checkData.colorCodes(checkData.sprawdzany.replace("{PLAYER}", target.getName())));
        		}
        		sprawdzany.put(target.getName(), p.getName());
			}
    		if(args[0].equalsIgnoreCase("clear")){
    			if(!(p.hasPermission("checkplayer.clear"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.clear)");
            		return false;
            	}
    			if (target == null) {
        			if (Config.getConfig("bans").contains(args[1])) {
        				Config.getConfig("bans").set(args[1], null);
        				Config.saveAll();
        				sender.sendMessage(checkData.prefix + "Odbanowano gracza " + ChatColor.GRAY+ args[1]);
        				return true;
        			}
        			sender.sendMessage(checkData.prefix + "Nie ma takiego gracza na serwerze!");
        			return true;
        		}
    			
        		if (checkCommands.sprawdzany.containsKey(target.getName())) {
        			checkCommands.sprawdzany.remove(target.getName());
        			Bukkit.broadcastMessage(checkData.colorCodes(checkData.clear).replace("{PLAYER}", target.getName()));
        			try {
        				Location loc = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+spr));
        	            target.teleport(loc);
        	            Config.getConfig("locations").set("gracz." + spr, null);
        	            Config.saveAll();
                        if (sender instanceof Player) {
                        	p.teleport(checkData.spawn);
                        }
        			} catch(Exception ex) {
        				sender.sendMessage("Spawn nie jest ustawiony! Aby ustawic wpisz /check setspawn spawn");
        			}
        		} else {
        			sender.sendMessage(checkData.prefix + "Gracz nie jest zbanowany!");
        		}
    		}
    		if(args[0].equalsIgnoreCase("cwand")){
    			if(!(p.hasPermission("checkplayer.cwand"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.cwand)");
            		return false;
            	}
    			ItemStack is = new ItemStack(Material.BLAZE_ROD);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.RED + "Kliknij lewym, aby sprawdzic gracza!");
                is.setItemMeta(im);
                if (target == null) {
                    p.sendMessage("§cGracz " + args[1] + " nie jest online!");
                    return true;
                }
                target.getInventory().addItem(is);
                p.sendMessage(ChatColor.RED + "Dales/as graczu " + args[1] + " rozdzke do sprawdzania graczy!");
                target.sendMessage(ChatColor.RED + "Dostales/as rozdzke do sprawdzania graczy!");
    		}
    		if(args[0].equalsIgnoreCase("setspawn")){
    			if(!(p.hasPermission("checkplayer.setspawn"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.setspawn)");
            		return false;
            	}
    			Location location = p.getLocation();
                if (args[1].equalsIgnoreCase("spawn")) {
                    this.setLocation("spawn", location);
                    checkData.spawn = location;
                    p.sendMessage(checkData.prefix + "Ustawiono spawn.");
                } else if (args[1].equalsIgnoreCase("checkroom")) {
                    this.setLocation("check-room", location);
                    checkData.checkroom = location;
                    p.sendMessage(checkData.prefix + "Ustawiono pokoj sprawdzania.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Argument " + args[1] + " nie zostal rozpoznany.");
                    sender.sendMessage(ChatColor.GRAY + "Uzycie: /check setspawn <spawn|checkroom>");
                }
    		}
				
		}
		
        return true;
    }
	
    public static String translate(String message, String admin, String player) {
        return message
                .replace("{PLAYER}", player)
                .replace("{ADMIN}", admin);
	}
    
    private boolean setLocation(String path, Location location) {
        Config.getConfig("locations").set(path + ".world", location.getWorld().getName());
        Config.getConfig("locations").set(path + ".x", location.getX());
        Config.getConfig("locations").set(path + ".y", location.getY());
        Config.getConfig("locations").set(path + ".z", location.getZ());
        Config.getConfig("locations").set(path + ".yaw", location.getYaw());
        Config.getConfig("locations").set(path + ".pitch", location.getPitch());
        return Config.saveAll();
    }

}
