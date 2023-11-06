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
            case 16://q
                return QUIT;
            case 28:// Enter
                return ENTER;
            case 19://r
                return READY;
            default:
                return NONE;
        }
    }

//    public static DIRECTION keyToDirection(KEY key) {
//        return switch (key) {
//            case UP -> DIRECTION.UP;
//            case DOWN -> DIRECTION.DOWN;
//            case LEFT -> DIRECTION.LEFT;
//            case RIGHT -> DIRECTION.RIGHT;
//            default -> null;
//        };
//    }
}
