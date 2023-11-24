package ch.heigvd;

public enum Border {
    HORIZONTAL,
    VERTICAL,
    CORNER_TOP_LEFT,
    CORNER_TOP_RIGHT,
    CORNER_BOTTOM_LEFT,
    CORNER_BOTTOM_RIGHT;

    private char[] borderChars;

    static {
        HORIZONTAL.borderChars = new char[]{'═', '─', '━'};
        VERTICAL.borderChars = new char[]{'║', '│', '┃'};
        CORNER_TOP_LEFT.borderChars = new char[]{'╔', '┌', '┏'};
        CORNER_TOP_RIGHT.borderChars = new char[]{'╗', '┐', '┓'};
        CORNER_BOTTOM_LEFT.borderChars = new char[]{'╚', '└', '┗'};
        CORNER_BOTTOM_RIGHT.borderChars = new char[]{'╝', '┘', '┛'};
    }

    public char getBorder(BorderType borderType) {
        return borderChars[borderType.ordinal()];
    }
}

