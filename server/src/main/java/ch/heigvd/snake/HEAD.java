package ch.heigvd.snake;

import ch.heigvd.controller.DIRECTION;

public enum HEAD {
    UP {
        @Override
        public String toString() {
            return "0";
        }
    },
    DOWN {
        @Override
        public String toString() {
            return "0";
        }
    },
    LEFT {
        @Override
        public String toString() {
            return "0";
        }
    },
    RIGHT {
        @Override
        public String toString() {
            return "0";
        }
    };

    static char getHead(DIRECTION direction){
        return switch (direction) {
            case UP -> UP.toString().charAt(0);
            case DOWN -> DOWN.toString().charAt(0);
            case LEFT -> LEFT.toString().charAt(0);
            case RIGHT -> RIGHT.toString().charAt(0);
            default -> ' ';
        };
    }
}
