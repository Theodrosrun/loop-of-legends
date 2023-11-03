package ch.heigvd.entity.snake;

import ch.heigvd.controller.DIRECTION;

public enum HEAD {
    UP {
        @Override
        public String toString() {
            return "▲";
        }
    },
    DOWN {
        @Override
        public String toString() {
            return "▼";
        }
    },
    LEFT {
        @Override
        public String toString() {
            return "◄";
        }
    },
    RIGHT {
        @Override
        public String toString() {
            return "►";
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
