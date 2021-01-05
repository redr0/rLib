package us.rojo.rlib.tab;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class TabUtils {

    private static Map<String, GameProfile> cache = new ConcurrentHashMap();

    public static boolean is18(Player player) {
        return ((((CraftPlayer) player).getHandle()).playerConnection.networkManager.getVersion() > 20);
    }

    public static GameProfile getOrCreateProfile(String name, UUID id) {
        GameProfile player = (GameProfile) cache.get(name);
        if (player == null) {
            player = new GameProfile(id, name);
            player.getProperties().putAll(TabManager.getDefaultPropertyMap());
            cache.put(name, player);
        }
        return player;
    }

    public static GameProfile getOrCreateProfile(String name) {
        return getOrCreateProfile(name, new UUID(new Random().nextLong(), new Random().nextLong()));
    }
}

