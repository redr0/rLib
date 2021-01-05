# rLib
 ⚖️ This is a fork of qLib with small fixes for better performance




# Example Tablist
public class TablistProvider implements LayoutProvider {

    @Override
    public TabLayout provide(Player player) {
        TabLayout entries = TabLayout.create(player);

        entries.set(0, 0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Test");

        return entries;
    }
}

