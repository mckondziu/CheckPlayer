package pl.minecraftkondziu;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class checkplayer extends JavaPlugin {
    public static checkplayer instance;
    
    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().log(Level.INFO, "# " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " on #");
        this.getLogger().log(Level.INFO, "Author: filippop1 ");
        this.getLogger().log(Level.INFO, "Kontynuacja: MinecraftKondziu");
        this.getCommand("check").setExecutor(new checkCommands());
        this.getServer().getPluginManager().registerEvents(new listeners(), this);
        Config.registerConfig("bans", "bans.yml", this);
        Config.registerConfig("options", "options.yml", this);
        Config.registerConfig("locations", "locations.yml", this);
        Config.loadAll();
        this.loadConfig();
        
        //metrics
        try {
            org.mcstats.Metrics metrics = new org.mcstats.Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        this.getServer().getScheduler().runTaskLaterAsynchronously(this, new PluginUpdater(), 20L);
        checkData.version = getDescription().getVersion();
    }
    
    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "# " + this.getDescription().getName() + " v"+this.getDescription().getVersion() + " off #");
        this.getLogger().log(Level.INFO, "Author: filippop1 ");
        this.getLogger().log(Level.INFO, "Kontynuacja: MinecraftKondziu");
    }
    
    public static checkplayer getInst() {
        return instance;
    }
    
    private void loadConfig() {
        try {
        	checkData.clear = Config.getConfig("options").getString("gracz-jest-czysty");
        	checkData.banforcheatstoplayer = Config.getConfig("options").getString("wiadomosc-bana-za-cheaty-do-gracza");
        	checkData.broadcastcheckplayer = Config.getConfig("options").getString("ogloszenie-o-sprawdzaniu-gracza");
        	checkData.broadcastlogoutplayer = Config.getConfig("options").getString("ogloszenie-o-wylogowaniu-sie-podczas-sprawdzania");
        	checkData.sprawdzany = Config.getConfig("options").getString("wiadomosc-do-podejrzanego");
        	checkData.spam = Config.getConfig("options").getInt("spam-wiadomosc-do-podejrzanego");
        	checkData.banforcheats = Config.getConfig("options").getString("ogloszenie-bana-za-cheaty");
            
        } catch (Exception ex) {
            this.getLogger().log(Level.INFO, "Wystapil blad podczas ladowania configu!");
        }
    }
    public static final Logger LOGGER = Bukkit.getLogger();
    public PluginDescriptionFile pdf;

    public static void notifyAdmins(String message) {
        LOGGER.log(Level.WARNING, message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("xvsx.notify")) {
                player.sendMessage(ChatColor.RED + "[CheckPlayer] " + ChatColor.GRAY + message);
            }
        }
    }
}

