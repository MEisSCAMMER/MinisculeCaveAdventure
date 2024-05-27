public class Sandwich extends Noun { //BetterNoun™
    public Sandwich(String name, String[] shortName, String desc, boolean isTakeable) {
        super(name, shortName, desc, isTakeable);
    }

    @Override
    public void use() {
        super.use();
        System.out.println("How do you use a sandwich? Don't be silly.");
    }
}
