public enum ToolType {
    CHAINSAW("Chainsaw"),
    JACKHAMMER("Jackhammer"),
    LADDER("Ladder");

    private final String displayName;

    ToolType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
