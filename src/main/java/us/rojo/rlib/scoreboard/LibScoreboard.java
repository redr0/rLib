package us.rojo.rlib.scoreboard;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import java.lang.reflect.Field;
import net.minecraft.server.v1_7_R4.Packet;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.com.google.common.base.Preconditions;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import net.minecraft.util.com.google.common.collect.ImmutableSet;
import net.minecraft.util.com.google.common.collect.UnmodifiableIterator;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import us.rojo.rlib.util.LinkedList;

public class LibScoreboard {

    private Player player;
    private Objective objective;
    private Map<String, Integer> displayedScores;
    private Map<String, String> scorePrefixes;
    private Map<String, String> scoreSuffixes;
    private Set<String> sentTeamCreates;
    private StringBuilder separateScoreBuilder;
    private List<String> separateScores;
    private Set<String> recentlyUpdatedScores;
    private Set<String> usedBaseScores;
    private String[] prefixScoreSuffix;
    private ThreadLocal<LinkedList<String>> localList;

    public LibScoreboard(Player player) {
        this.displayedScores = new HashMap<>();
        this.scorePrefixes = new HashMap<>();
        this.scoreSuffixes = new HashMap<>();
        this.sentTeamCreates = new HashSet<>();
        this.separateScoreBuilder = new StringBuilder();
        this.separateScores = new ArrayList<>();
        this.recentlyUpdatedScores = new HashSet<>();
        this.usedBaseScores = new HashSet<>();
        this.prefixScoreSuffix = new String[3];
        this.localList = ThreadLocal.withInitial(LinkedList::new);
        this.player = player;
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        (this.objective = scoreboard.registerNewObjective("sidebar", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void update() {
        String untranslatedTitle = ScoreboardManager.getConfiguration().getTitleGetter().getTitle(player);
        String title = ChatColor.translateAlternateColorCodes('&', untranslatedTitle);
        List<String> lines = localList.get();
        if (!lines.isEmpty()) {
            lines.clear();
        }

        ScoreboardManager.getConfiguration().getScoreGetter().getScores(localList.get(), player);
        recentlyUpdatedScores.clear();
        usedBaseScores.clear();
        int nextValue = lines.size();
        Preconditions.checkArgument(lines.size() < 16, (Object) "Too many lines passed!");
        Preconditions.checkArgument(title.length() < 32, (Object) "Title is too long!");
        if (!objective.getDisplayName().equals(title)) {
            objective.setDisplayName(title);
        }

        for (String line : lines) {
            if (48 <= line.length()) {
                throw new IllegalArgumentException("Line is too long! Offending line: " + line);
            }

            String[] separated = separate(line, usedBaseScores);
            String prefix = separated[0];
            String score = separated[1];
            String suffix = separated[2];

            recentlyUpdatedScores.add(score);
            if (!sentTeamCreates.contains(score)) {
                createAndAddMember(score);
            }

            if (!displayedScores.containsKey(score) || displayedScores.get(score) != nextValue) {
                setScore(score, nextValue);
            }

            if (!scorePrefixes.containsKey(score) || !scorePrefixes.get(score).equals(prefix) || !scoreSuffixes.get(score).equals(suffix)) {
                updateScore(score, prefix, suffix);
            }
            --nextValue;
        }

        for (UnmodifiableIterator unmodifiableIterator = ImmutableSet.copyOf(displayedScores.keySet()).iterator(); unmodifiableIterator.hasNext();) {
            String displayedScore = (String) unmodifiableIterator.next();
            if (recentlyUpdatedScores.contains(displayedScore)) {
                continue;
            }

            removeScore(displayedScore);

        }
    }

    private void setField(Packet packet, String field, Object value) {
        try {
            Field fieldObject = packet.getClass().getDeclaredField(field);
            fieldObject.setAccessible(true);
            fieldObject.set(packet, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndAddMember(String scoreTitle) {
        ScoreboardTeamPacketMod scoreboardTeamAdd = new ScoreboardTeamPacketMod(scoreTitle, "_", "_", (Collection) ImmutableList.of(), 0);
        ScoreboardTeamPacketMod scoreboardTeamAddMember = new ScoreboardTeamPacketMod(scoreTitle, (Collection) ImmutableList.of((Object) scoreTitle), 3);
        scoreboardTeamAdd.sendToPlayer(player);
        scoreboardTeamAddMember.sendToPlayer(player);
        sentTeamCreates.add(scoreTitle);
    }

    private void setScore(String score, int value) {
        PacketPlayOutScoreboardScore scoreboardScorePacket = new PacketPlayOutScoreboardScore();
        setField((Packet) scoreboardScorePacket, "a", score);
        setField((Packet) scoreboardScorePacket, "b", objective.getName());
        setField((Packet) scoreboardScorePacket, "c", value);
        setField((Packet) scoreboardScorePacket, "d", 0);
        displayedScores.put(score, value);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) scoreboardScorePacket);
    }

    private void removeScore(String score) {
        displayedScores.remove(score);
        scorePrefixes.remove(score);
        scoreSuffixes.remove(score);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(score));
    }

    private void updateScore(String score, String prefix, String suffix) {
        scorePrefixes.put(score, prefix);
        scoreSuffixes.put(score, suffix);
        new ScoreboardTeamPacketMod(score, prefix, suffix, null, 2).sendToPlayer(player);
    }

    private String[] separate(String line, Collection<String> usedBaseScores) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String prefix = "";
        String score = "";
        String suffix = "";

        separateScores.clear();
        separateScoreBuilder.setLength(0);
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (c == '*' || (separateScoreBuilder.length() == 16 && separateScores.size() < 3)) {
                separateScores.add(separateScoreBuilder.toString());
                separateScoreBuilder.setLength(0);
                if (c == '*') {
                    continue;
                }
            }
            separateScoreBuilder.append(c);
        }

        separateScores.add(separateScoreBuilder.toString());
        switch (separateScores.size()) {
            case 1: {
                score = separateScores.get(0);
                break;
            }
            case 2: {
                score = separateScores.get(0);
                suffix = separateScores.get(1);
                break;
            }
            case 3: {
                prefix = separateScores.get(0);
                score = separateScores.get(1);
                suffix = separateScores.get(2);
                break;
            }
            default: {
                Bukkit.getLogger().warning("Failed to separate scoreboard line. Input: " + line);
                break;
            }
        }

        if (usedBaseScores.contains(score)) {
            if (score.length() <= 14) {
                for (ChatColor chatColor : ChatColor.values()) {
                    String possibleScore = chatColor + score;
                    if (!usedBaseScores.contains(possibleScore)) {
                        score = possibleScore;
                        break;
                    }
                }

                if (usedBaseScores.contains(score)) {
                    Bukkit.getLogger().warning("Failed to find alternate color code for: " + score);
                }
            } else {
                Bukkit.getLogger().warning("Found a scoreboard base collision to shift: " + score);
            }
        }

        if (prefix.length() > 16) {
            prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }

        if (score.length() > 16) {
            score = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }

        if (suffix.length() > 16) {
            suffix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        usedBaseScores.add(score);
        prefixScoreSuffix[0] = prefix;
        prefixScoreSuffix[1] = score;
        prefixScoreSuffix[2] = suffix;
        return prefixScoreSuffix;
    }
}

