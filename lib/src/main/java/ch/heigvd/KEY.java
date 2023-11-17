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
    MENU,
    NONE;

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
                case 'm' -> KEY.MENU;
                default -> KEY.NONE;
            };
            default -> KEY.NONE;
        };
    }
}
