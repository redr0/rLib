package us.rojo.rlib.scoreboard;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.lang.reflect.Field;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;

public class ScoreboardTeamPacketMod {

    private PacketPlayOutScoreboardTeam packet;
    private static Field aField;
    private static Field bField;
    private static Field cField;
    private static Field dField;
    private static Field eField;
    private static Field fField;
    private static Field gField;

    public ScoreboardTeamPacketMod(String name, String prefix, String suffix, Collection players, int i) {
        packet = new PacketPlayOutScoreboardTeam();
        try {
            ScoreboardTeamPacketMod.aField.set(packet, name);
            ScoreboardTeamPacketMod.fField.set(packet, i);
            if (i == 0 || i == 2) {
                ScoreboardTeamPacketMod.bField.set(packet, name);
                ScoreboardTeamPacketMod.cField.set(packet, prefix);
                ScoreboardTeamPacketMod.dField.set(packet, suffix);
                ScoreboardTeamPacketMod.gField.set(packet, 3);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (i == 0) {
            addAll(players);
        }
    }

    public ScoreboardTeamPacketMod(String name, Collection players, int paramInt) {
        packet = new PacketPlayOutScoreboardTeam();
        try {
            ScoreboardTeamPacketMod.gField.set(packet, 3);
            ScoreboardTeamPacketMod.aField.set(packet, name);
            ScoreboardTeamPacketMod.fField.set(packet, paramInt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        addAll(players);
    }

    public void sendToPlayer(Player bukkitPlayer) {
        ((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(packet);
    }

    private void addAll(Collection collection) {
        if (collection == null) {
            return;
        }

        try {
            ((Collection) ScoreboardTeamPacketMod.eField.get(packet)).addAll(collection);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        try {
            ScoreboardTeamPacketMod.aField = PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
            ScoreboardTeamPacketMod.bField = PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
            ScoreboardTeamPacketMod.cField = PacketPlayOutScoreboardTeam.class.getDeclaredField("c");
            ScoreboardTeamPacketMod.dField = PacketPlayOutScoreboardTeam.class.getDeclaredField("d");
            ScoreboardTeamPacketMod.eField = PacketPlayOutScoreboardTeam.class.getDeclaredField("e");
            ScoreboardTeamPacketMod.fField = PacketPlayOutScoreboardTeam.class.getDeclaredField("f");
            ScoreboardTeamPacketMod.gField = PacketPlayOutScoreboardTeam.class.getDeclaredField("g");
            ScoreboardTeamPacketMod.aField.setAccessible(true);
            ScoreboardTeamPacketMod.bField.setAccessible(true);
            ScoreboardTeamPacketMod.cField.setAccessible(true);
            ScoreboardTeamPacketMod.dField.setAccessible(true);
            ScoreboardTeamPacketMod.eField.setAccessible(true);
            ScoreboardTeamPacketMod.fField.setAccessible(true);
            ScoreboardTeamPacketMod.gField.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

