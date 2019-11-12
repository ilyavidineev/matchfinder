class WordPosition {
    private Integer lineOffset;
    private Integer charOffset;

    WordPosition(Integer lineOffset, Integer charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    @Override
    public String toString() {
        return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
    }
}