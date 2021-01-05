package us.rojo.rlib;

import com.comphenix.protocol.ProtocolLibrary;
import java.util.Random;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.rojo.rlib.scoreboard.ScoreboardManager;

public class RLib extends JavaPlugin {

    @Getter
    private static RLib plugin;

    @Getter
    private static Random random = new Random();

    @Override
    public void onEnable() {
        plugin = this;

        ScoreboardManager.init();
        TabManager.init();

        (new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                    ProtocolLibrary.getProtocolManager().addPacketListener(new TabAdapter());
                }
            }
        }).runTaskLater(this, 1);
    }
}
