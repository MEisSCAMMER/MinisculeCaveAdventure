import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Game {
    private final Player player;
    private final Parser parser;

    public Game() {
        //reset noun_data.tsv
        try {
            Files.writeString(Paths.get(Helper.NOUN_DATA_FILE),
                    Files.readString(Paths.get(Helper.ORIGINAL_NOUN_DATA_FILE)));
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
        player = new Player("Steve");
        parser = new Parser(player);
    }

    public void mainloop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Miniscule Cave Adventure!");
        System.out.println(player.getLocation().getName() + "\n" + player.getLocation().getDescription());
        Parser.printNouns(player.getLocation().getNouns());
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine().strip();
            if(!parser.parse(input)) {
                System.out.println("Thank you for playing Miniscule Cave Adventure. " +
                        "Your score is 0 out of a possible 1,000,000 points. You have failed. Goodbye forever.");
                break;
            }
        }
    }
}
