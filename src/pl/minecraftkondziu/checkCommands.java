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
            p.sendMessage(ChatColor.YELLOW + "cheater <powod>" + ChatColor.RED + " - " + ChatColor.GREEN + "Zbanuj danego gracza za cheaty z mozliwoscia powodu");
            p.sendMessage(ChatColor.YELLOW + "player" + ChatColor.RED + " - " + ChatColor.GREEN + "Sprawdz gracza. Teleportuje go do pokoju sprawdzajacego");
            p.sendMessage(ChatColor.YELLOW + "clear" + ChatColor.RED + " - " + ChatColor.GREEN + "Oznacz gracza, jako brak cheatów!");
            p.sendMessage(ChatColor.YELLOW + "cwand" + ChatColor.RED + " - " + ChatColor.GREEN + "Rozdzka do sprawdzania graczy!");
            p.sendMessage(ChatColor.YELLOW + "setspawn" + ChatColor.RED + " - " + ChatColor.GREEN + "Ustaw lokalizacje pokoju sprawdzajacego");
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
    		if(args[0].equalsIgnoreCase("setspawn")){
    			if(!(p.hasPermission("checkplayer.setspawn"))){
            		p.sendMessage(ChatColor.RED + "Nie masz uprawnien do tej komendy "
            				+ChatColor.GRAY+ "(checkplayer.setspawn)");
            		return false;
            	}
    			Location loca = p.getLocation();
    			Config.getConfig("locations").set("check-room", checkData.locationToString(loca));
                Config.saveAll();
                p.sendMessage(checkData.prefix + "Ustawiono pokoj sprawdzania.");
    		}
		}
		
		// =================================
		// takie tam odzmaczenie argumentu 2
		// =================================
		
		if (args.length == 2) {
			
	        double ded = 0;
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
        		target.setHealth(ded);
        		target.kickPlayer(kickmessage);
        		if(!(Config.getConfig("locations").getString("gracz."+p.getName()) != null)) {
        			Config.getConfig("locations").set("gracz." + spr, null);
        		}else{
        			Location loca = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+p.getName()));
                	p.teleport(loca);
        			Config.getConfig("locations").set("gracz." + p.getName(), null);
        			Config.getConfig("locations").set("gracz." + spr, null);
        		}
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
                Location loca = p.getLocation();
                Config.getConfig("locations").set("gracz."+spr, checkData.locationToString(loc));
                Config.getConfig("locations").set("gracz."+p.getName(), checkData.locationToString(loca));
                Config.saveAll();
        		target.setFireTicks(0);
        		try {
        			Location checkroom = checkData.getLocationFromString(Config.getConfig("locations").getString("check-room"));
        			target.teleport(checkroom);
        			p.teleport(checkroom);
        		}catch (Exception ex) {
        			p.sendMessage(ChatColor.RED+"CheckRoom nie jest ustawiony! Aby ustawic wpisz "+ChatColor.GRAY+"/check setspawn");
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
        			sender.sendMessage(checkData.prefix + "Ten gracz nie jest zbanowany!");
        			return true;
        		}
    			
        		if (checkCommands.sprawdzany.containsKey(target.getName())) {
        			checkCommands.sprawdzany.remove(target.getName());
        			Bukkit.broadcastMessage(checkData.colorCodes(checkData.clear).replace("{PLAYER}", target.getName()));
        			Location loc = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+spr));
        			Location loca = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+p.getName()));
        	        target.teleport(loc);
        	        p.teleport(loca);
        	        Config.getConfig("locations").set("gracz." + spr, null);
        	        Config.getConfig("locations").set("gracz." + p.getName(), null);
        	        Config.saveAll();
        		} else {
        			sender.sendMessage(checkData.prefix + "Gracz nie jest sprawdzany");
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
				
		}
		
		// =================================
		// takie tam odzmaczenie argumentu 3
		// =================================
				
		if (args.length >= 3) {
			
	        double ded = 0;
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
				String powod = "";
	            
	            for(int i = 2; i < args.length ; i++){
	                powod += args[i] + " ";
	            }
	            Bukkit.broadcastMessage(checkData.colorCodes(translate(checkData.banforcheats + ChatColor.DARK_GRAY + " Powód: " + ChatColor.GRAY+ powod, p.getName(), target.getName())));
	            String kickmessage = checkData.colorCodes(translate(checkData.banforcheatstoplayer + ChatColor.DARK_GRAY + " Powód: " + ChatColor.GRAY+ powod, p.getName(), target.getName()));
        		checkCommands.sprawdzany.remove(target.getName());
        		Config.getConfig("bans").set(target.getName(), kickmessage);
        		target.setHealth(ded);
        		target.kickPlayer(kickmessage);
        		if(!(Config.getConfig("locations").getString("gracz."+p.getName()) != null)) {
        			Config.getConfig("locations").set("gracz." + spr, null);
        		}else{
        			Location loca = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+p.getName()));
                	p.teleport(loca);
        			Config.getConfig("locations").set("gracz." + p.getName(), null);
        			Config.getConfig("locations").set("gracz." + spr, null);
        		}
	            Config.saveAll();
	            
			}
		}
		
        return true;
    }
	
    public static String translate(String message, String admin, String player) {
        return message
                .replace("{PLAYER}", player)
                .replace("{ADMIN}", admin);
	}

}
