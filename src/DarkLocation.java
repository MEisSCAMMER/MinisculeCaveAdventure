public class DarkLocation extends Location {
    public DarkLocation(int x, int y, String name, String description, Noun... nouns) {
        super(x, y, name, description, nouns);
    }

    @Override
    public boolean isDark() {
        return true;
    }
}
