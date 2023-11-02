package ch.heigvd;

import java.util.LinkedList;

public class Snake {

    //https://en.wikipedia.org/wiki/Box-drawing_character

    private final char verticalBody = '│';
    private final char horizontalBody = '─';

    public enum BODY {
        VERTICAL {
            @Override
            public String toString() {
                return "│";
            }
        },
        HORIZONTAL {
            @Override
            public String toString() {
                return "─";
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
    public enum ANGLE {
        UP_RIGHT {
            @Override
            public String toString() {
                return "┐";
            }
        },
        UP_LEFT {
            @Override
            public String toString() {
                return "┌";
            }
        },
        DOWN_RIGHT {
            @Override
            public String toString() {
                return "┘";
            }
        },
        DOWN_LEFT {
            @Override
            public String toString() {
                return "└";
            }
        };

        static char getAngle(DIRECTION direction, DIRECTION previousDirection){
            return switch (direction) {
                case UP -> switch (previousDirection) {
                    case LEFT -> DOWN_LEFT.toString().charAt(0);
                    case RIGHT -> DOWN_RIGHT.toString().charAt(0);
                    default -> ' ';
                };
                case DOWN -> switch (previousDirection) {
                    case LEFT -> UP_LEFT.toString().charAt(0);
                    case RIGHT -> UP_RIGHT.toString().charAt(0);
                    default -> ' ';
                };
                case LEFT -> switch (previousDirection) {
                    case UP -> UP_RIGHT.toString().charAt(0);
                    case DOWN -> DOWN_RIGHT.toString().charAt(0);
                    default -> ' ';
                };
                case RIGHT -> switch (previousDirection) {
                    case UP -> UP_LEFT.toString().charAt(0);
                    case DOWN -> DOWN_LEFT.toString().charAt(0);
                    default -> ' ';
                };
                default -> ' ';
            };
        }
    }

    private LinkedList<Position> body = new LinkedList<Position>();


    public Snake(Position initialPosition, short initialLength) {
        body.add(initialPosition);
        Position previousPosition = null;
        for (int i = 0; i < initialLength; i++) {
            int x = initialPosition.x + i * DIRECTION.getCoef(initialPosition.direction)[0];
            int y = initialPosition.y + i * DIRECTION.getCoef(initialPosition.direction)[1];
            Position position = new Position(x,y, initialPosition.direction, getBodyRepresentation(initialPosition, previousPosition));
            previousPosition = position;
            body.add(position);
        }
    }

    private char getBodyRepresentation(Position position, Position previousPosition) {
        if (previousPosition == null) {
            return HEAD.getHead(position.direction);
        }
        if (position.direction == previousPosition.direction) {
            return BODY.getBody(position.direction);
        } else {
            return ANGLE.getAngle(position.direction, previousPosition.direction);
        }
    }

    public LinkedList<Position> getSnake() {
        return body;
    }

}
