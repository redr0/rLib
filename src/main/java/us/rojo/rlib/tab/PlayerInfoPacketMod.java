package us.rojo.rlib.tab;

import java.lang.reflect.Field;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerInfoPacketMod {

    private PacketPlayOutPlayerInfo packet;

    public PlayerInfoPacketMod(String name, int ping, GameProfile profile, int action) {
        this.packet = new PacketPlayOutPlayerInfo();

        setField("username", name);
        setField("ping", ping);
        setField("action", action);
        setField("player", profile);
    }

    public void setField(String field, Object value) {
        try {
            Field fieldObject = this.packet.getClass().getDeclaredField(field);
            fieldObject.setAccessible(true);
            fieldObject.set(this.packet, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToPlayer(Player player) {
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(this.packet);
    }
}