package ch.heigvd.snake;

import ch.heigvd.DIRECTION;

public enum BODY {
    VERTICAL {
        @Override

        public String toString() {
            return "o";
        }
    },
    HORIZONTAL {
        @Override
        public String toString() {
            return "o";
        }
    };
    static char getBody(DIRECTION direction){
        return switch (direction) {
            case UP -> VERTICAL.toString().charAt(0);
            case DOWN -> VERTICAL.toString().charAt(0);
            case LEFT -> HORIZONTAL.toString().charAt(0);
            case RIGHT -> HORIZONTAL.toString().charAt(0);
            default -> ' ';
        };
    }
}
