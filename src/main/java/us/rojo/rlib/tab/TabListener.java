package us.rojo.rlib.tab;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.rojo.rlib.RLib;

public class TabListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        (new BukkitRunnable() {
            @Override
            public void run() {
                TabManager.addPlayer(event.getPlayer());
            }
        }).runTaskLater(RLib.getPlugin(), 10L);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TabManager.removePlayer(event.getPlayer());
        TabLayout.remove(event.getPlayer());
    }
}

