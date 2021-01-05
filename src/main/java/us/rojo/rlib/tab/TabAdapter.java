package us.rojo.rlib.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.lang.reflect.Field;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityTrackerEntry;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.rojo.rlib.RLib;

public class TabAdapter extends PacketAdapter {

    private static Field playerField;
    private static Field namedEntitySpawnField;

    public TabAdapter() {
        super(RLib.getPlugin(), new PacketType[]{PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN});
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (TabManager.getLayoutProvider() == null || !shouldForbid(event.getPlayer())) {
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            PacketContainer packetContainer = event.getPacket();

            String name = (String) packetContainer.getStrings().read(0);
            boolean isOurs = ((String) packetContainer.getStrings().read(0)).startsWith("$");
            int action = (packetContainer.getIntegers().read(1));
            if (!isOurs) {
                if (action != 4 && shouldCancel(event.getPlayer(), event.getPacket())) {
                    event.setCancelled(true);
                }
            } else {
                packetContainer.getStrings().write(0, name.replace("$", ""));
            }
        }
    }

    private boolean shouldCancel(Player player, PacketContainer packetContainer) {
        UUID tabPacketPlayer;
        if (!TabUtils.is18(player)) {
            return true;
        }

        PacketPlayOutPlayerInfo playerInfoPacket = (PacketPlayOutPlayerInfo) packetContainer.getHandle();
        EntityPlayer recipient = ((CraftPlayer) player).getHandle();

        try {
            tabPacketPlayer = ((GameProfile) playerField.get(playerInfoPacket)).getId();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Player bukkitPlayer = Bukkit.getPlayer(tabPacketPlayer);

        if (bukkitPlayer == null) {
            return true;
        }

        EntityTrackerEntry trackerEntry = (EntityTrackerEntry) (((WorldServer) ((CraftPlayer) bukkitPlayer).getHandle().getWorld()).getTracker()).trackedEntities.get(bukkitPlayer.getEntityId());
        if (trackerEntry == null) {
            return true;
        }

        return !trackerEntry.trackedPlayers.contains(recipient);
    }

    private boolean shouldForbid(Player player) {
        Tab playerTab = (Tab) TabManager.getTabs().get(player.getUniqueId());
        return (playerTab != null && playerTab.isInitiated());
    }

    static {
        try {
            playerField = PacketPlayOutPlayerInfo.class.getDeclaredField("player");
            playerField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            namedEntitySpawnField = PacketPlayOutNamedEntitySpawn.class.getDeclaredField("b");
            namedEntitySpawnField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}