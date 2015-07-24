package pl.minecraftkondziu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class listeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
    public void onLeftClickOnPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        if (!(e.getDamager() instanceof Player))
            return;
        Player p = (Player) e.getDamager();
        
        if (!p.hasPermission("checkplayer.wand")) 
            return;
        Player clicked = (Player) e.getEntity();
        ItemStack is = p.getItemInHand();
        ItemMeta im = is.getItemMeta();
            if (is.getType().equals(Material.BLAZE_ROD)) {
                if (im.getDisplayName().equalsIgnoreCase(ChatColor.RED + "Kliknij lewym, aby sprawdzic gracza!")
                        || im.getDisplayName().equalsIgnoreCase(ChatColor.RED + "LPM = ban, PPM = czysty/a na gracza " + clicked.getName())) {
                    e.setCancelled(true);
                    if (!checkCommands.sprawdzany.containsKey(clicked.getName())) {
                    	Location loc = clicked.getLocation();
                        Config.getConfig("locations").set("gracz."+clicked.getName(), checkData.locationToString(loc));
                        Config.saveAll();
                        try {
                        	clicked.teleport(checkData.checkroom);
                            p.teleport(checkData.checkroom);
                        } catch (Exception ex) {
                            p.sendMessage(ChatColor.RED + "Spawn nie jest ustawiony! Aby ustawic wpisz /check setspawn checkroom");
                            return;
                        }
                        im.setDisplayName(ChatColor.RED + "LPM = ban, PPM = czysty/a na gracza " + clicked.getName());
                        is.setItemMeta(im);
                        clicked.setFireTicks(0);
                        clicked.setHealth(20.0);
                        clicked.setFoodLevel(20);
                        Bukkit.broadcastMessage(checkData.colorCodes(checkData.broadcastcheckplayer.replace("{PLAYER}", clicked.getName())));
                       for (int i = 0; i < checkData.spam; i++) {
                           clicked.sendMessage(checkData.colorCodes(checkData.sprawdzany.replace("{PLAYER}", clicked.getName())));    
                       }
                       checkCommands.sprawdzany.put(clicked.getName(), p.getName());
                    } else {
                        Bukkit.broadcastMessage(checkData.colorCodes(checkCommands.translate(checkData.banforcheats, p.getName(), clicked.getName())));
                        String kickmessage = checkData.colorCodes(checkCommands.translate(checkData.banforcheatstoplayer, p.getName(), clicked.getName()));
                        checkCommands.sprawdzany.remove(clicked.getName());
                        Config.getConfig("bans").set(clicked.getName(), kickmessage);
                        clicked.kickPlayer(kickmessage);
                        Config.saveAll();
                        Config.getConfig("locations").set("gracz." + clicked.getName(), null);
                        Config.saveAll();
                        im.setDisplayName(ChatColor.RED + "Kliknij lewym, aby sprawdzic gracza!");
                        is.setItemMeta(im);
                    }    
              }
        }
    }     
    
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event){
        if (!(event.getRightClicked() instanceof Player))
            return;
        Player p = event.getPlayer();
        if (!p.hasPermission("checkplayer.wand") || p.getItemInHand() == null || !p.getItemInHand().getItemMeta().hasDisplayName()) 
            return;
        Player target = (Player) event.getRightClicked();
        ItemStack is = p.getItemInHand();
        ItemMeta im = is.getItemMeta();
        if (!im.getDisplayName().equals(ChatColor.RED + "LPM = ban, PPM = czysty/a na gracza " + target.getName())) 
            return;
        im.setDisplayName(ChatColor.RED + "Kliknij lewym, aby sprawdzic gracza!");
        is.setItemMeta(im);
        checkCommands.sprawdzany.remove(target.getName());
        Bukkit.broadcastMessage(checkData.colorCodes(checkData.clear).replace("{PLAYER}", target.getName()));
        try {
        	Location loc = checkData.getLocationFromString(Config.getConfig("locations").getString("gracz."+target.getName()));
            target.teleport(loc);
            Config.getConfig("locations").set("gracz." + target.getName(), null);
            Config.saveAll();
            p.teleport(checkData.spawn);
        } catch(Exception ex) {
            p.sendMessage(ChatColor.RED + "Spawn nie jest ustawiony! Aby ustawic wpisz /check setspawn spawn");
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
    	Player p = e.getPlayer();
        if (p.isOp())
            PluginUpdater.print(p);
    }
    
    @EventHandler
    void onPlayerLogin(PlayerLoginEvent e) {
    	Player p = e.getPlayer();
        try  {
            if (Config.getConfig("bans").contains(p.getName())) {
                e.disallow(Result.KICK_OTHER, checkData.colorCodes(Config.getConfig("bans").getString(p.getName())));
            }
         } catch(Exception ex) {}

    }
    
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (checkCommands.sprawdzany.containsKey(p.getName())) {
            String message = this.translate(checkData.banforcheatstoplayer, p.getName());
            Config.getConfig("bans").set(p.getName(), message);
            Config.saveAll();
            Config.getConfig("locations").set("gracz." + p.getName(), null);
            Config.saveAll();
            String bcast = checkData.colorCodes(this.translate(checkData.broadcastlogoutplayer, p.getName()));
            
            checkCommands.sprawdzany.remove(p.getName());
            Bukkit.broadcastMessage(bcast);
        }
    }

    private String translate(String message, String player) {
        return message
                    .replace("{PLAYER}", player)    
                    .replace("{ADMIN}", checkCommands.sprawdzany.get(player));
    }
   

}
