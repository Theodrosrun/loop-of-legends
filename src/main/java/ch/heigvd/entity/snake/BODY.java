package ch.heigvd.entity.snake;

import ch.heigvd.controller.DIRECTION;

public enum BODY {
    VERTICAL {
        @Override

        public String toString() {
            return "█";
        }
    },
    HORIZONTAL {
        @Override
        public String toString() {
            return "█";
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
