package ch.heigvd;

public enum KEY {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    QUIT,
    ENTER,
    READY,
    NONE;

    public static KEY getKey(int keyCode) {
        switch (keyCode) {
            case 57419:
                return LEFT;
            case 57416:
                return UP;
            case 57421:
                return RIGHT;
            case 57424:
                return DOWN;
            case 16:
                return QUIT;
            case 28:
                return ENTER;
            case 19:
                return READY;
            default:
                return NONE;
        }
    }
}
