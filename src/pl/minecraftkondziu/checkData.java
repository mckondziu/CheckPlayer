package pl.minecraftkondziu;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class checkData {
	
	    public static Location getLocationFromString(String locationInString){
	        String[] split = locationInString.split("/");
	 
	        return new Location(
	            Bukkit.getServer().getWorld(split[0]),
	            Double.parseDouble(split[1]),
	            Double.parseDouble(split[2]),
	            Double.parseDouble(split[3]),
	            Float.parseFloat(split[4]),
	            Float.parseFloat(split[5]));
	    }
	    public static String locationToString(Location loc)
	    {
	        if(loc == null){
	            return null;
	        }
	       
	        return (new StringBuilder()
	            .append(loc.getWorld().getName()).append("/")
	            .append(loc.getX()).append("/")
	            .append(loc.getY()).append("/")
	            .append(loc.getZ()).append("/")
	            .append(loc.getYaw()).append("/")
	            .append(loc.getPitch()).toString()
	        );
	    }
	    
	    public static String prefix = "§8[§cCheckPlayer§8]§c ";
	    public static String broadcastcheckplayer;
	    public static String broadcastlogoutplayer;
	    public static String sprawdzany;
	    public static int spam;
	    public static String banforcheats;
	    public static String banforcheatstoplayer;
	    public static String clear;
	    
	    public static String colorCodes(String string) {
	        return ChatColor.translateAlternateColorCodes('&', string);
	    }
	    public static final List<String> versions = new ArrayList<String>();
	    
	    public static String version;
	    public static String getVersion() {
	        return version;
	      }
	    
	}
