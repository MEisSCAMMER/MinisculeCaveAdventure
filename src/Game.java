import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Game {
    private final Player player;
    private final Parser parser;

    public Game() {
        player = new Player("Steve");
        parser = new Parser(player, this);
    }

    public void mainloop() {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Miniscule Cave Adventure!");
            System.out.print(player.getLocation().getName() + "\n" + player.getLocation().getDescription() + " ");
            Helper.printExits(player.getLocation().getX(), player.getLocation().getY());
            Parser.printNouns(player.getLocation().getNouns());
            System.out.println("\nWhat do you do? (For help, simply type \"help\".)");
            while(true) {
                System.out.print("> ");
                String input = scanner.nextLine().strip();
                if(!parser.parse(input)) {
                    System.out.println("Thank you for playing Miniscule Cave Adventure. " +
                            "Your score is 0 out of a possible 1,000,000 points. You have failed. Goodbye forever.");
                    quit();
                    break;
                }
            }
        }
    }

    public void quit() {
        //reset noun_data.tsv
        try {
            Files.writeString(
                    Paths.get(Helper.NOUN_DATA_FILE),
                    Files.readString(Paths.get(Helper.ORIGINAL_NOUN_DATA_FILE))
            );
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
    }

    public void winSequence(int score) {
        if(score == 1000000) {
            System.out.println("Angelic voices sing as you enter, to a chorus of rapturous " +
                    "melody. You listen carefully. Is that...? Are they singing...? You can just " +
                    "make out the words: ");
            System.out.printf("\u001b[%dmNEVER GONNA GIVE YOU UP\u001b[0m\n", Helper.ACCENT_COLOR);
            System.out.printf("\u001b[%dmNEVER GONNA LET YOU DOWN\u001b[0m\n", Helper.ACCENT_COLOR);
            System.out.printf("\u001b[%dmNEVER GONNA RUN AROUND AND DESERT YOU\u001b[0m\n",
                    Helper.ACCENT_COLOR);
            System.out.println("...");
            System.out.println("Oh for God's sake.");
            System.out.println("Look. Ignore that. The point is:");
            System.out.println();
            System.out.printf("***\u001b[%dmYou have won\u001b[0m***\n", Helper.ACCENT_COLOR);
            System.out.println("Thank you for playing Miniscule Cave Adventure. Your score is 1,000,000 out of a " +
                    "possible 1,000,000 points. You have succeeded. Good job. Have a cookie to celebrate.");
        } else {
            System.out.printf("***\u001b[%dmYou have won\u001b[0m***\n", Helper.ACCENT_COLOR);
            System.out.println("Thank you for playing Miniscule Cave Adventure. Your score is 1 out of a " +
                    "possible 1,000,000 points. You have technically succeeded. Coward. You know what? I'm NOT " +
                    "thanking you. What kind of person types \"win\" into a prompt? Seriously? That's just lazy.");
        }
        quit();
        System.exit(0);
    }
}
