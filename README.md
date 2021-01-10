# rLib
 ⚖️ This is a fork of qLib with small fixes for better performance



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

### Scoreboard Example

```java
  
  public class ServerScoreGetter implements ScoreGetter {

    @Override
    public void getScores(LinkedList<String> scores, Player player) {

         scores.add("Test");
    }
}

```
### Need that Clase

```java

public class ServerScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(new TitleGetter("Hola"));
        configuration.setScoreGetter(new HubScoreGetter());

        return (configuration);
    }

}

```

### Scoreboard onEnable

```java

ScoreboardManager.setLayoutProvider(new ScoreboardLayoutProvider());

```
It depends on ProtocolLib and the spigot used is the mSpigot
