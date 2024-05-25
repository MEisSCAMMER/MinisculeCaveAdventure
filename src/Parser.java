import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Player player;
    private static final String[] VERBS = {
            "take", "get", "use"
    };

    public Parser(Player player) { this.player = player; }

    public boolean parse(String input) {
        ArrayList<String> words = new ArrayList<>(List.of(input.split(" ")));
        words.replaceAll(String::toLowerCase);
        words.removeIf(word -> word.equals("the"));
        if(words.size() == 2) {
            //verb/noun
            if(List.of(VERBS).contains(words.get(0))) {
                //do something with the verb
                for(Noun noun: player.getInventory()) {
                    if(noun.name().equals(words.get(1))) {
                        //verb the noun
                        noun.use();
                    }
                }
            } else {
                //um
                //think of something to do?
                cantUnderstand();
            }
        } else if(words.size() == 1) {
            //single word
            switch (words.get(0)) {
                case "i", "inventory" -> printPlayerInventory();
                case "n", "s", "w", "e" -> player.move(words.get(0));
                case "look" -> {
                    System.out.println(player.getLocation().getDescription());
                    printNouns(player.getLocation().getNouns());
                }
                case "q", "quit" -> {
                    return false;
                }
                default -> cantUnderstand();
            }
        } else cantUnderstand();
        return true;
    }

    public void printPlayerInventory() {
        if(player.getInventory().isEmpty()) {
            System.out.println("You are empty-handed. Even your sense of adventure has left you.");
        } else {
            System.out.println("You have:");
            for (Noun noun: player.getInventory()) {
                System.out.println("- " + noun.name());
            }
        }
    }

    private static <T> String concatenateList(List<T> list) {
        if(list.isEmpty()) return null;
        if(list.size()==1) return list.get(0).toString();
        if(list.size()==2) return list.get(0) + " and " + list.get(1);
        T last = list.remove(list.size()-1);
        StringBuilder sb = new StringBuilder();
        for (T s: list) sb.append(s).append(", ");
        sb.append("and ").append(last);
        return sb.toString();
    }

    public static void printNouns(ArrayList<Noun> nouns) {
        if(!nouns.isEmpty()) System.out.println("There is " + concatenateList(nouns) + " here.");
    }

    public void cantUnderstand() {
        System.out.println("I can't understand that. Try rephrasing?");
    }
}
