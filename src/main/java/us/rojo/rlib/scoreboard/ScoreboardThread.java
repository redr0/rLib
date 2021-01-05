package us.rojo.rlib.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardThread extends Thread {

    public ScoreboardThread() {
        super("Lib - Scoreboard Thread");
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                try {
                    ScoreboardManager.updateScoreboard(other);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                Thread.sleep(ScoreboardManager.getUpdateInterval() * 50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
