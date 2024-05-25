public record Noun(String name, String shortName, String desc, boolean isTakeable) {
    public void use() {}

    @Override
    public String toString() {
        return name;
    }
}
