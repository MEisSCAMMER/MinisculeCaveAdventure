import java.util.Scanner;

public class Noun {
    private final String name;
    private final String[] shortNames;
    private String desc;
    private final boolean isTakeable;
    private boolean isUsed = false;

    public Noun(String name, String[] shortNames, String desc, boolean isTakeable) {
        this.name = name;
        this.shortNames = shortNames;
        this.desc = desc;
        this.isTakeable = isTakeable;
    }

    public void use(Player player, boolean beingUsedBy) {
        if(beingUsedBy) { //the player seems to be using something on this noun
            if(name.equals("a door")) {
                System.out.println("It opens! You can enter the door now.");
                desc = "It's open.";
                isUsed = true;
            } else System.out.println("You can't use it on the " + shortNames[0] + ".");
            return;
        }

        //the player is trying to use this noun directly
        switch(name) {
            case "a sandwich" -> System.out.println("How do you use a sandwich? Don't be silly.");
            case "a brass lantern" -> {
                System.out.println("You turn the lantern o" + (isUsed ? "ff" : "n") + ".");
                if(desc.contains("off, but")) desc = desc.replace("off, but", "on, and");
                else desc = desc.replace("on, and", "off, but");
                isUsed = !isUsed;
            } case "a shiny key" -> {
                if(!isUsed) {
                    Parser.promptForUse(player, new Scanner(System.in));
                    isUsed = false;
                } else {
                    System.out.println("You already used the key. It's not going to unlock anything else.");
                }
            } case "a door" -> System.out.println("It's locked. Of course it is. " +
                    "Nothing's ever EASY around here, is it?");
            default -> System.out.println("You can't use the " + shortNames[0] + ".");
        }
    }

    public boolean isUsed() {
        return isUsed;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String[] getShortNames() {
        return shortNames;
    }

    public String getShortName() {
        return shortNames[0];
    }

    public String getDesc() {
        return desc;
    }

    public boolean isTakeable() {
        return isTakeable;
    }
}
