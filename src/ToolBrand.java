public enum ToolBrand {
    DEWALT("DeWalt"),
    RIDGID("Ridgid"),
    STIHL("Stihl"),
    WERNER("Werner");

    private final String displayName;

    ToolBrand(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
