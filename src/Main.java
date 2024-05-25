import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Steve");
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser(player);
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
