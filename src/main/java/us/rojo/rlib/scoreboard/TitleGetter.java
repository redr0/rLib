package us.rojo.rlib.scoreboard;

import org.bukkit.entity.Player;
import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;

public class TitleGetter {

    private String defaultTitle;

    @Deprecated
    public TitleGetter(String defaultTitle) {
        this.defaultTitle = ChatColor.translateAlternateColorCodes('&', defaultTitle);
    }

    public TitleGetter() {
    }

    public static TitleGetter forStaticString(String string) {
        Preconditions.checkNotNull((Object) string);
        return new TitleGetter() {
            @Override
            public String getTitle(Player player) {
                return string;
            }
        };
    }

    public String getTitle(Player player) {
        return this.defaultTitle;
    }
}

