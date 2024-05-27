import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Player player;
    private static final String[] VERBS = {"take", "get", "use", "drop", "examine", "x"};

    private static final String[] UNIMPORTANT_WORDS = {"the", "a", "an", "i", "my"};

    public Parser(Player player) { this.player = player; }

    /**
     * Parses the input string and performs the appropriate action.
     * @param input the input string to parse.
     * @return false if the player has quit, true otherwise.
     */
    public boolean parse(String input) {
        ArrayList<String> words = new ArrayList<>(List.of(input.split(" ")));
        words.replaceAll(String::toLowerCase);
        //remove punctuation
        words.replaceAll(word -> word.replaceAll("[^a-zA-Z0-9]", ""));
        words.removeIf(word -> List.of(UNIMPORTANT_WORDS).contains(word));
        if(words.size() == 2) parseTwoWords(words.get(0), words.get(1));
        else if(words.size() == 1) return parseSingleWord(words.get(0));
        else return parse(words.get(0) + " " + words.get(1)); //just the first two non-important words
        return true;
    }

    /**
     * Prints the current contents of the player's inventory.
     */
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
                You are playing Miniscule Cave Adventure (MCA) v0.0.1.
                MCA uses a text parser. Here are some commands you can use:
                * \u001b[%dmN/S/E/W\u001b[0m to move
                * \u001b[%dmL\u001b[0m or \u001b[%dmLOOK\u001b[0m to print a description of your current location
                * \u001b[%dmI\u001b[0m or \u001b[%dmINVENTORY\u001b[0m to print the current contents of your inventory
                * \u001b[%dmTAKE/GET [object]\u001b[0m to take an object
                * \u001b[%dmDROP [object]\u001b[0m to drop an object
                * \u001b[%dmUSE [object]\u001b[0m to use an object. It doesn't do much yet but it works, technically
                * \u001b[%dmX/EXAMINE [object]\u001b[0m to examine an object
                * \u001b[%dmQ\u001b[0m or \u001b[%dmQUIT\u001b[0m to... GEE I DON'T KNOW... quit the game
                * \u001b[%dmH\u001b[0m or \u001b[%dmHELP\u001b[0m to print this message again%n""",
                color, color, color, color, color, color, color, color, color, color, color, color, color
        );
    }

    /**
     * Parses a single-word command.
     * @param word the word to parse.
     * @return false if the player has quit, true otherwise.
     */
    private boolean parseSingleWord(String word) {
        switch (word) {
            case "i", "inventory" -> printPlayerInventory();
            case "n", "s", "w", "e" -> player.move(word);
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
        return true;
    }

    /**
     * Parses a two-word command.
     * @param word1 the first word.
     * @param word2 the second word.
     */
    private void parseTwoWords(String word1, String word2) {
        //we're assuming the player entered something in the form of VERB NOUN
        if(List.of(VERBS).contains(word1)) {
            //assemble a list of everything the player could be talking about
            ArrayList<Noun> allNouns = new ArrayList<>();
            allNouns.addAll(player.getLocation().getNouns());
            allNouns.addAll(player.getInventory());
            for(Noun noun: allNouns) for(String name: noun.getShortNames()) {
                if(name.equals(word2)) switch (word1) {
                    case "use" -> { //probably make this do something more useful at some point?
                        noun.use();
                        return;
                    } case "get", "take" -> {
                        if (player.getLocation().getNouns().contains(noun)) {
                            if(noun.isTakeable()) {
                                //we only get this far if the noun is in the room and it's takeable
                                player.addToInventory(noun);
                                player.getLocation().removeNoun(noun);
                                Helper.removeNoun(player.getLocation().getX(), player.getLocation().getY(), noun);
                                System.out.println("You take the " + noun.getShortName() + ".");
                            } else System.out.println("You can't take that. It's " + noun.getName() + ", for" +
                                    " God's sake."); //it's in the room but not takeable
                        } else System.out.println("You already have the " + noun.getShortName() + ".");
                               //i.e., it's in the player's inventory but NOT the room
                        return;
                    } case "drop" -> {
                        if (player.getInventory().contains(noun)) {
                            player.removeFromInventory(noun);
                            player.getLocation().getNouns().add(noun);
                            Helper.addNoun(player.getLocation().getX(), player.getLocation().getY(), noun);
                            System.out.println("You drop the " + noun.getShortName() + ".");
                        } else System.out.println("You don't have the " + noun.getShortName() + ".");
                        return;
                    } case "examine", "x" -> { //this one's easy
                        System.out.println(noun.getDesc());
                        return;
                    } default -> {}
                }
            }
            //at this point, the noun isn't in either the player's inventory or the current room
            //we may now insult the player
            System.out.println("You bozo! There is no " + word2 + "!");
        } else cantUnderstand();
    }
}
