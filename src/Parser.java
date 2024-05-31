import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private final Player player;
    private final Game game;
    private static final String[] VERBS = {"take", "get", "use", "drop", "examine", "x", "go", "enter"};

    private static final String[] UNIMPORTANT_WORDS = {"the", "a", "an", "i", "my", "on"};

    public Parser(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    /**
     * Parses the input string and performs the appropriate action.
     * @param input the input string to parse.
     * @return false if the player has quit, true otherwise.
     */
    public boolean parse(String input) {
        //to handle multicommand commands
        if(input.contains(";")) {
            for(String command: input.split(";")) if(!parse(command.strip())) return false;
            return true;
        }

        //next: single-command commands
        ArrayList<String> words = new ArrayList<>(List.of(input.split(" ")));
        words.replaceAll(String::toLowerCase);
        if(words.size() == 1) return parseSingleWord(words.get(0).replaceAll("[^a-z0-9]", ""));
        words.replaceAll(word -> word.replaceAll("[^a-z0-9]", "")); //remove punctuation
        words.removeIf(word -> List.of(UNIMPORTANT_WORDS).contains(word)); //remove unimportant words
        if(words.isEmpty() || words.size() == 1) cantUnderstand(); //the player's being unintelligible
        else if(words.size() == 2) parseTwoWords(words.get(0), words.get(1));
        else return parse(words.get(0) + " " + words.get(1)); //just the first two unimportant words
        return true;
    }

    /**
     * Parses a single-word command.
     * @param word the word to parse.
     * @return false if the player has quit, true otherwise.
     */
    private boolean parseSingleWord(String word) {
        switch (word) {
            case "i", "inventory" -> printPlayerInventory();
            case "n", "s", "w", "e",
                 "north", "south", "west", "east" -> player.move(word);
            case "h", "help" -> printHelp();
            case "l", "look" -> {
                System.out.println(player.getLocation().getDescription());
                printNouns(player.getLocation().getNouns());
            }
            case "q", "quit" -> {
                return false;
            }
            case "win" -> game.winSequence(1);
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
            //if the user wants to move (e.g., "go north") we can do that pretty easily here
            if(word1.equals("go")) {
                parseSingleWord(word2);
                return;
            }

            //assemble a list of everything the player could be talking about
            ArrayList<Noun> allNouns = new ArrayList<>();
            allNouns.addAll(player.getLocation().getNouns());
            allNouns.addAll(player.getInventory());
            for(Noun noun: allNouns) for(String name: noun.getShortNames()) {
                if(name.equals(word2)) switch (word1) {
                    case "use" -> {
                        noun.use(player, false);
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
                    } case "enter" -> {
                        if(noun.getName().equals("a door")) {
                            if(noun.isUsed()) {
                                game.winSequence(1000000);
                                return;
                            }
                        } else System.out.println("You can't enter that.");
                        return;
                    } default -> {}
                }
            }
            //at this point, the noun isn't in either the player's inventory or the current room
            //we may now insult the player
            System.out.println("You bozo! There is no " + word2 + "!");
        } else cantUnderstand();
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

    public static void printNouns(ArrayList<Noun> nouns) {
        if(!nouns.isEmpty()) System.out.println("There is " + Helper.concatenateList(nouns) + " here.");
    }

    private void cantUnderstand() {
        System.out.println("I can't understand that. Try rephrasing?");
    }

    public static void printHelp() {
        int color = Helper.ACCENT_COLOR; //I'm not typing out "Helper.ACCENT_COLOR" 18 times
        System.out.printf("""
                You are playing Miniscule Cave Adventure (MCA) v%s.
                MCA uses a text parser, which is non-case-sensitive. Here are some commands you can use:
                * \u001b[%dmn\u001b[0m/\u001b[%dms\u001b[0m/\u001b[%dme\u001b[0m/\u001b[%dmw\u001b[0m to move
                * \u001b[%dml\u001b[0m or \u001b[%dmlook\u001b[0m to print a description of your current location
                * \u001b[%dmi\u001b[0m or \u001b[%dminventory\u001b[0m to print the current contents of your inventory
                * \u001b[%dmtake\u001b[0m/\u001b[%dmget [object]\u001b[0m to take an object
                * \u001b[%dmdrop [object]\u001b[0m to drop an object
                * \u001b[%dmuse [object]\u001b[0m to use an object. It doesn't do much yet but it works, technically
                * \u001b[%dmx\u001b[0m/\u001b[%dmexamine [object]\u001b[0m to examine an object
                * \u001b[%dmq\u001b[0m or \u001b[%dmquit\u001b[0m to... GEE I DON'T KNOW... quit the game
                * \u001b[%dmh\u001b[0m or \u001b[%dmhelp\u001b[0m to print this message again.
                If you want to enter multiple commands in one line, simply separate them with a semicolon.
                """,
                Helper.VERSION, color, color, color, color, color, color, color, color, color, color, color, color,
                color, color, color, color, color, color
        );
    }

    public static void promptForUse(Player player, Scanner scanner) { //java kept crashing with the scanner inside the
                                                                      //method for some reason
        System.out.print("On what?\n> ");
        String line = scanner.nextLine();
        String[] parsedWords = line.strip().toLowerCase().replaceAll("[^a-z0-9 ]", "").split(" ");
        String parsed = Arrays.stream(parsedWords).filter(word -> !List.of(UNIMPORTANT_WORDS).contains(word)).findFirst().orElse("");
        String steve = Arrays.stream(parsedWords).filter(word -> !List.of(UNIMPORTANT_WORDS).contains(word))
                .collect(Collectors.joining(" "));
        ArrayList<Noun> allNouns = new ArrayList<>();
        allNouns.addAll(player.getLocation().getNouns());
        allNouns.addAll(player.getInventory());
        for(Noun noun: allNouns) for(String name: noun.getShortNames()) {
            if(name.equals(parsed)) {
                noun.use(player, true);
                return;
            }
        }
        System.out.printf("I'm not sure what a \"%s\" is.\n", steve);
    }
}
