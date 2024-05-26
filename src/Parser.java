import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Player player;
    private static final String[] VERBS = {
            "take", "get", "use"
    };

    private static final String[] UNIMPORTANT_WORDS = {
            "the", "a", "an", "I", "my", "brass"
    };

    public Parser(Player player) { this.player = player; }

    public boolean parse(String input) {
        ArrayList<String> words = new ArrayList<>(List.of(input.split(" ")));
        words.replaceAll(String::toLowerCase);
        words.removeIf(word -> List.of(UNIMPORTANT_WORDS).contains(word));
        if(words.size() == 2) {
            //verb/noun
            if(List.of(VERBS).contains(words.get(0))) {
                //do something with the verb
                ArrayList<Noun> allNouns = player.getLocation().getNouns();
                allNouns.addAll(player.getInventory());
                for(Noun noun: allNouns) {
                    if(noun.getShortName().equals(words.get(1))) {
                        //verb the noun
                        if(words.get(0).equals("use")) {
                            noun.use();
                            return true;
                        }
                    }
                }
                System.out.println("You bozo! There is no " + words.get(1) + "!"); //we haven't found the noun
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
                case "h", "help" -> printHelp(Helper.ACCENT_COLOR);
                case "l", "look" -> {
                    System.out.println(player.getLocation().getDescription());
                    printNouns(player.getLocation().getNouns());
                }
                case "q", "quit" -> {
                    return false;
                }
                default -> cantUnderstand();
            }
        } else return parse(words.get(0) + " " + words.get(1)); //just the first two non-important words
        return true;
    }

    public void printPlayerInventory() {
        if(player.getInventory().isEmpty()) {
            System.out.println("You are empty-handed. Even your sense of adventure has left you.");
        } else {
            System.out.println("You have:");
            for (Noun noun: player.getInventory()) {
                System.out.println("- " + noun.getName());
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

    private void cantUnderstand() {
        System.out.println("I can't understand that. Try rephrasing?");
    }

    public static void printHelp(int color) {
        System.out.printf("""
                Miniscule Cave Adventure uses a text parser. Here are some commands you can use:
                * \u001b[%dmN/S/E/W\u001b[0m to move
                * \u001b[%dmL\u001b[0m or \u001b[%dmLOOK\u001b[0m to print a description of your current location
                * \u001b[%dmI\u001b[0m or \u001b[%dmINVENTORY\u001b[0m to print the current contents of your inventory
                * \u001b[%dmTAKE/GET [object]\u001b[0m to do nothing because I haven't implemented that yet
                * \u001b[%dmUSE [object]\u001b[0m to use an object. It doesn't do much yet but it works, technically
                * \u001b[%dmQ\u001b[0m or \u001b[%dmQUIT\u001b[0m to... GEE I DON'T KNOW... quit
                * \u001b[%dmH\u001b[0m or \u001b[%dmHELP\u001b[0m to print this message again%n""",
                color, color, color, color, color, color, color, color, color, color, color
        );
    }
}
