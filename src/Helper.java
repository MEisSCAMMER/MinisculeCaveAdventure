import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Helper {
    private static final String ROOM_DATA_FILE =
            String.format("src%sdata%sroom_data.tsv", File.separator, File.separator);
    public static final String NOUN_DATA_FILE =
            String.format("src%sdata%snoun_data.tsv", File.separator, File.separator);
    public static final String ORIGINAL_NOUN_DATA_FILE =
            String.format("src%sdata%snoun_data_original.tsv", File.separator, File.separator);
    public static final int ACCENT_COLOR = 34;

    /**
     * @param x the x-coordinate of the noun(s).
     * @param y the y-coordinate of the noun(s).
     * @return an ArrayList of the noun(s).
     */
    private static ArrayList<Noun> getNouns(int x, int y) {
        ArrayList<Noun> nouns = new ArrayList<>();
        try(Scanner scanner = new Scanner(new File(NOUN_DATA_FILE))) {
            scanner.nextLine(); //skip the header
            while(scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\t");
                if(Integer.parseInt(data[0])==x && Integer.parseInt(data[1])==y)
                    nouns.add(new Noun(data[2], data[3].split(","), data[4], Boolean.parseBoolean(data[5])));
            }
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }

        return nouns;
    }

    /**
     * @param x the x-coordinate of the location.
     * @param y the y-coordinate of the location.
     * @return a Location object representing the location at the specified coordinates.
     */
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

    /**
     * @param loc an array containing the x and y coordinates of the location.
     * @return a Location object representing the location at the specified coordinates.
     */
    public static Location getLoc(int[] loc) {
        return getLoc(loc[0], loc[1]);
    }

    /**
     * Checks if there is a location at the specified coordinates.
     * @param x the x-coordinate of the location.
     * @param y the y-coordinate of the location.
     * @return true if the location exists, false otherwise.
     */
    public static boolean locExists(int x, int y) {
        try (Scanner scanner = new Scanner(new File(ROOM_DATA_FILE))) {
            scanner.nextLine(); //skip the header
            while(scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split("\t");
                if(Integer.parseInt(data[0])==x && Integer.parseInt(data[1])==y) return true;
            }
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds a noun to NOUN_DATA_FILE at the specified coordinates.
     * @param x the x-coordinate to add the noun to.
     * @param y the y-coordinate to add the noun to.
     * @param noun the noun to add.
     */
    public static void addNoun(int x, int y, Noun noun) {
        //essentially, what we're trying to do here is get a StringBuilder and build it up so it's exactly the same as
        //the contents of NOUN_DATA_FILE, BUT with a new noun at the end. then write that back to NOUN_DATA_FILE
        try (Scanner scanner = new Scanner(new File(NOUN_DATA_FILE))) { //try with resources!
                                                                        //don't have to worry about closing the scanner
            StringBuilder data = new StringBuilder();
            while (scanner.hasNextLine()) data.append(scanner.nextLine()).append("\n");
            //now we've finished writing the entire contents of noun_data.tsv to data, it's time to add that noun

            data.append(x).append("\t") //x
                .append(y).append("\t") //y
                .append(noun.getName()).append("\t"); //name
            for(String shortName: noun.getShortNames()) data.append(shortName).append(","); //shortnames
            data.deleteCharAt(data.length()-1); //delete that last comma
            data.append("\t")
                .append(noun.getDesc()).append("\t") //description
                .append(noun.isTakeable()); //is takeable

            java.nio.file.Files.writeString(java.nio.file.Paths.get(NOUN_DATA_FILE), data.toString());
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
    }

    /**
     * Removes a noun at the specified coordinates from NOUN_DATA_FILE.
     * @param x the x-coordinate of the noun.
     * @param y the y-coordinate of the noun.
     * @param noun the noun to remove.
     */
    public static void removeNoun(int x, int y, Noun noun) {
        //more straightforward than addNoun
        try (Scanner scanner = new Scanner(new File(NOUN_DATA_FILE))) {
            StringBuilder data = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //here, we only append the line IF it's not the noun we want to remove
                if(!line.startsWith(x + "\t" + y + "\t" + noun.getName())) data.append(line).append("\n");
            }

            data.deleteCharAt(data.length()-1); //and delete that last newline

            java.nio.file.Files.writeString(java.nio.file.Paths.get(NOUN_DATA_FILE), data.toString());
        } catch (IOException e) {
            System.out.println("Oops, there's been an error: " + e.getMessage());
        }
    }
}
