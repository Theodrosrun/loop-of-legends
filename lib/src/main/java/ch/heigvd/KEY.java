package ch.heigvd;

import com.googlecode.lanterna.input.KeyStroke;

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

    public static KEY parseKeyStroke(KeyStroke key) {
        if (key == null) {
            return KEY.NONE;
        }
        return switch (key.getKeyType()) {
            case ArrowUp -> KEY.UP;
            case ArrowDown -> KEY.DOWN;
            case ArrowLeft -> KEY.LEFT;
            case ArrowRight -> KEY.RIGHT;
            case Enter -> KEY.ENTER;
            case Character -> switch (key.getCharacter()) {
                case 'q' -> KEY.QUIT;
                case 'r' -> KEY.READY;
                default -> KEY.NONE;
            };
            default -> KEY.NONE;
        };
    }
}
