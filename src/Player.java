import java.util.ArrayList;
import java.util.Map;

public class Player {
    private final String name;
    private int health;
    private Location location;
    private final ArrayList<Noun> inventory;
    private final ArrayList<Map<Integer, Integer>> visited; //a map might seem weird, but it's nicer because
                                                            //you're guaranteed two elements in it

    public Player(String name) {
        location = Helper.getLoc(0, 0); //start at (0, 0)
        this.name = name;
        health = 100;
        inventory = new ArrayList<>();
        inventory.add(new Noun("a burning sense of adventure", new String[]{"adventure", "sense", "burning"},
                "It's pretty burning, man.", true));
        visited = new ArrayList<>();
        visited.add(Map.of(0, 0)); //we've already visited the room we start in
    }

    /**
     * Moves the player in the specified direction, then prints information about the new location.
     * @param dir the direction to move in. Accepts N/S/E/W or North/South/East/West. Case-insensitive.
     */
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

        //print the location's name and description if it hasn't been visited
        //otherwise, print only the name
        hasVisited = visited.contains(Map.of(loc[0], loc[1]));
        if (!hasVisited) visited.add(Map.of(loc[0], loc[1]));
        location = newLoc;
        System.out.print(location.getName() + (hasVisited ? "\n" : ("\n" + location.getDescription() + " ")));
        if(!hasVisited) Helper.printExits(location.getX(), location.getY());
        Parser.printNouns(location.getNouns());
    }

    public void addToInventory(Noun noun) {
        inventory.add(noun);
    }

    public void removeFromInventory(Noun noun) {
        inventory.remove(noun);
    }

    public void takeDamage(int health) {
        if(this.health-health > 0) {
            this.health -= health;
            System.out.printf("You have lost %d health. You have %d health remaining.", health, this.health);
        } else {
            System.out.printf("You were about to lose %d health, but you only had %d to begin with.", health,
                    this.health);
            System.out.println("I don't know how to break it to you, but...");
            die();
            this.health = 0;
        }
    }

    public void heal(int health) {
        if(health+this.health<=100) this.health += health;
        else this.health = 100;
    }

    private void die() {
        System.out.println("\n***You have died***");
        System.out.println("Would you like to restart?");
        System.out.println("Whoops, you can't answer that. Because you're dead.");
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public ArrayList<Noun> getInventory() { return inventory; }
    public Location getLocation() { return location; }
}
