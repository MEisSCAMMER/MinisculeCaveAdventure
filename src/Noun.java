public class Noun {
    private final String name;
    private final String shortName;
    private final String desc;
    private final boolean isTakeable;
    private boolean isUsed = false; //eventually there may be functionality depending on whether an item has been used

    public Noun(String name, String shortName, String desc, boolean isTakeable) {
        this.name = name;
        this.shortName = shortName;
        this.desc = desc;
        this.isTakeable = isTakeable;
    }

    public void use() {
        isUsed = true;
        System.out.println("You use the " + shortName + ".");
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isTakeable() {
        return isTakeable;
    }
}
