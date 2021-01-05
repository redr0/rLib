# rLib
 ⚖️ This is a fork of qLib with small fixes for better performance
     It depends on Protocollib and the spigot used is the mSpigot



### Tablist Example

```java

public class TablistProvider implements LayoutProvider {

    @Override
    public TabLayout provide(Player player) {
        TabLayout entries = TabLayout.create(player);

        entries.set(0, 0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Test");

        return entries;
    }
}

```

### Tablist onEnable

```java

public void onEnable() {

// Adapter implement
TabManager.setLayoutProvider(new TablistProvider());

}

```

