import java.util.ArrayList;
import java.util.List;

public class Location {
    private final int x;
    private final int y;
    private final ArrayList<Noun> nouns;
    private final String name;
    private final String description;

    public Location(int x, int y, String name, String description, Noun... nouns) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;
        this.nouns = new ArrayList<>(List.of(nouns));
    }

    //getters/setters
    public int getX()                 { return x; }
    public int getY()                 { return y; }
    public String getDescription()    { return description; }
    public String getName()           { return name; }
    public ArrayList<Noun> getNouns() { return nouns; }

    public void setNouns(ArrayList<Noun> nouns) {
        this.nouns.clear();
        this.nouns.addAll(nouns);
    }
}
