package pl.minecraftkondziu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginUpdater implements Runnable {

public void run()
{
  try {
    URL url = new URL("http://minecraftkondziu.cba.pl/PluginUpdaterCP.txt");
    @SuppressWarnings("resource")
	Scanner scanner = new Scanner(url.openStream());
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      checkData.versions.add(line.toLowerCase());
    }

    print(Bukkit.getConsoleSender());
    for (Player player : Bukkit.getOnlinePlayers())
      if (player.isOp())
        print(player);
  }
  catch (MalformedURLException ex)
  {
    Logger.getLogger(PluginUpdater.class.getName()).log(Level.SEVERE, null, ex);
  } catch (IOException ex) {
    Logger.getLogger(PluginUpdater.class.getName()).log(Level.SEVERE, null, ex);
  }
}

public static List<String> getVersions() {
  return checkData.versions;
}

public static boolean hasVersion(String version) {
  if (version.toLowerCase().equals("unknown")) {
    return true;
  }
  return checkData.versions.contains(version.toLowerCase());
}
	public static void print(CommandSender sender)
	{
	  String version = checkData.getVersion();
	  if (!hasVersion(version)) {
	    String label = ChatColor.YELLOW + "+-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
	    String text = ChatColor.BLUE + "Aktualizacja pluginu " + ChatColor.GRAY + "Check" +ChatColor.BOLD + "Player" + ChatColor.BLUE + ".";

	    String ver = ChatColor.BLUE + " Twoja wersja " + ChatColor.RED + version + ChatColor.BLUE + " - najnowsza " + ChatColor.GREEN + (String)getVersions().get(0);
	    
	    String dl = ChatColor.GRAY + "Mozesz pobrac ze strony " + ChatColor.WHITE + "http://minecraftkondziu.wc.lt/pl/CheckPlayer/";

	    sender.sendMessage(label + "\n" + text + ver + "\n" + dl + "\n" + label);
	  }
	}

}
