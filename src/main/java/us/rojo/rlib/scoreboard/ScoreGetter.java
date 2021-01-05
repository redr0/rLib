package us.rojo.rlib.scoreboard;

import org.bukkit.entity.Player;
import us.rojo.rlib.util.LinkedList;

public interface ScoreGetter {

    void getScores(LinkedList<String> lines, Player player);
}
