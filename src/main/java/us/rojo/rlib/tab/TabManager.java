package us.rojo.rlib.tab;

import com.google.common.base.Preconditions;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.HttpAuthenticationService;
import net.minecraft.util.com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap;
import net.minecraft.util.com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.rojo.rlib.RLib;

public class TabManager {

    private static boolean initiated = false;

    private static AtomicReference<Object> defaultPropertyMap = new AtomicReference();
    private static LayoutProvider layoutProvider;

    public static PropertyMap getDefaultPropertyMap() {
        Object value = defaultPropertyMap.get();
        if (value == null) {
            synchronized (defaultPropertyMap) {
                value = defaultPropertyMap.get();
                if (value == null) {
                    PropertyMap actualValue = fetchSkin();
                    value = (actualValue == null) ? defaultPropertyMap : actualValue;
                    defaultPropertyMap.set(value);
                }
            }
        }
        return (PropertyMap) ((value == defaultPropertyMap) ? null : value);
    }

    public static LayoutProvider getLayoutProvider() {
        return layoutProvider;
    }

    private static Map<UUID, Tab> tabs = new ConcurrentHashMap();

    public static Map<UUID, Tab> getTabs() {
        return tabs;
    }

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        getDefaultPropertyMap();
        (new TabThread()).start();
        Bukkit.getPluginManager().registerEvents(new TabListener(), RLib.getPlugin());
    }

    public static void setLayoutProvider(LayoutProvider provider) {
        layoutProvider = provider;
    }

    public static void addPlayer(Player player) {
        tabs.put(player.getUniqueId(), new Tab(player));
    }

    public static void updatePlayer(Player player) {
        if (tabs.containsKey(player.getUniqueId())) {
            ((Tab) tabs.get(player.getUniqueId())).update();
        }
    }

    public static void removePlayer(Player player) {
        tabs.remove(player.getUniqueId());
    }

    private static PropertyMap fetchSkin() {
        GameProfile profile = new GameProfile(UUID.fromString("6b22037d-c043-4271-94f2-adb00368bf16"), "bananasquad");

        HttpAuthenticationService authenticationService = (HttpAuthenticationService) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        MinecraftSessionService sessionService = authenticationService.createMinecraftSessionService();
        GameProfile profile2 = sessionService.fillProfileProperties(profile, true);
        return profile2.getProperties();
    }
}

