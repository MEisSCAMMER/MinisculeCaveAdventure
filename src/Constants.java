import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Constants {
    private static final String ROOM_DATA_FILE =
            String.format("src%sdata%sroom_data.tsv", File.separator, File.separator);
    private static final String NOUN_DATA_FILE =
            String.format("src%sdata%snoun_data.tsv", File.separator, File.separator);

    public static Location getLoc(int x, int y) {
        try (Scanner scanner = new Scanner(new File(ROOM_DATA_FILE))) {
            scanner.nextLine(); //skip the header
            Location l = null;
            while(scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\t");
                if(Integer.parseInt(data[0])==x && Integer.parseInt(data[1])==y) {
                    l = new Location(x, y, data[2], data[3]);
                    break;
                }
            }
            if(l == null) return null;
            ArrayList<Noun> nouns = getNouns(x, y);
            if(!nouns.isEmpty()) l.setNouns(nouns);
            return l;
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
        return null;
    }

    private static ArrayList<Noun> getNouns(int x, int y) {
        ArrayList<Noun> nouns = new ArrayList<>();
        try(Scanner scanner = new Scanner(new File(NOUN_DATA_FILE))) {
            scanner.nextLine(); //skip the header
            while(scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\t");
                if(Integer.parseInt(data[0])== x && Integer.parseInt(data[1])== y)
                    nouns.add(new Noun(data[2], data[3], data[4], Boolean.parseBoolean(data[5])));
            }
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }

        return nouns;
    }

    public static Location getLoc(int[] loc) {
        return getLoc(loc[0], loc[1]);
    }
}
