public class Noun {
    private final String name;
    private final String[] shortNames;
    private final String desc;
    private final boolean isTakeable;
    private boolean isUsed = false; //eventually there may be functionality depending on whether an item has been used

    public Noun(String name, String[] shortNames, String desc, boolean isTakeable) {
        this.name = name;
        this.shortNames = shortNames;
        this.desc = desc;
        this.isTakeable = isTakeable;
    }

    public void use() {
        isUsed = true;
        System.out.println("You use the " + shortNames[0] + ".");
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
