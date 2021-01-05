package us.rojo.rlib.tab;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.rojo.rlib.RLib;

public class TabThread extends Thread {

    public TabThread() {
        this.protocolLib = Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib");

        setName("Lib - Tab Thread");
        setDaemon(true);
    }

    private Plugin protocolLib;

    public void run() {
        while (RLib.getPlugin().isEnabled() && this.protocolLib != null && this.protocolLib.isEnabled()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                try {
                    TabManager.updatePlayer(online);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(250L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
