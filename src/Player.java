import java.util.ArrayList;
import java.util.Map;

public class Player {
    private final String name;
    private int health;
    private Location location;
    private final ArrayList<Noun> inventory;
    private final ArrayList<Map<Integer, Integer>> visited;

    public Player(String name) {
        location = Helper.getLoc(0, 0);
        this.name = name;
        health = 100;
        inventory = new ArrayList<>();
        visited = new ArrayList<>();
        visited.add(Map.of(0, 0));
    }

    public void move(String dir) {
        int[] loc = {location.getX(), location.getY()};
        boolean hasVisited;
        switch (dir.toLowerCase()) {
            case "n", "north" -> loc[1]++;
            case "s", "south" -> loc[1]--;
            case "e", "east"  -> loc[0]++;
            case "w", "west"  -> loc[0]--;
            default -> throw new Error("invalid direction");
        }
        Location newLoc = Helper.getLoc(loc);
        if (newLoc == null) {
            System.out.println("You can't go that way.");
            return;
        }
        hasVisited = visited.contains(Map.of(loc[0], loc[1]));
        if (!hasVisited) visited.add(Map.of(loc[0], loc[1]));
        location = newLoc;
        System.out.println(location.getName() + (hasVisited ? "" : ("\n" + location.getDescription())));
        Parser.printNouns(location.getNouns());
    }

    public void addToInventory(Noun noun) {
        inventory.add(noun);
    }

    public void takeDamage(int health) {
        if(health>0) this.health -= health;
    }

    public void heal(int health) {
        if(health>0) this.health += health;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public ArrayList<Noun> getInventory() { return inventory; }
    public Location getLocation() { return location; }

    public void use(Noun noun) {
        if(inventory.contains(noun) || location.getNouns().contains(noun)) noun.use();
        else System.out.println("You bozo! There is no " + noun.name() + "!");
    }
}
