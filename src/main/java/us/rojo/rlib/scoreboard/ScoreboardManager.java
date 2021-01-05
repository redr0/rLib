package us.rojo.rlib.scoreboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import net.minecraft.util.com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import us.rojo.rlib.RLib;

public final class ScoreboardManager {

    private static Map<String, LibScoreboard> boards = new ConcurrentHashMap<>();
    private static ScoreboardConfiguration configuration = null;
    private static boolean initiated = false;
    private static int updateInterval = 2;

    public static void init() {
        Preconditions.checkState(!ScoreboardManager.initiated);
        ScoreboardManager.initiated = true;
        new ScoreboardThread().start();
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(), RLib.getPlugin());
    }

    protected static void create(Player player) {
        if (ScoreboardManager.configuration != null) {
            ScoreboardManager.boards.put(player.getName(), new LibScoreboard(player));
        }
    }

    protected static void updateScoreboard(Player player) {
        LibScoreboard board = ScoreboardManager.boards.get(player.getName());
        if (board != null) {
            board.update();
        }
    }

    protected static void remove(Player player) {
        ScoreboardManager.boards.remove(player.getName());
    }

    public static ScoreboardConfiguration getConfiguration() {
        return ScoreboardManager.configuration;
    }

    public static void setConfiguration(ScoreboardConfiguration configuration) {
        ScoreboardManager.configuration = configuration;
    }

    public static int getUpdateInterval() {
        return ScoreboardManager.updateInterval;
    }

    public static void setUpdateInterval(final int updateInterval) {
        ScoreboardManager.updateInterval = updateInterval;
    }
}
