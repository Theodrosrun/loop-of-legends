package ch.heigvd.entity.snake;


import ch.heigvd.controller.Board;
import ch.heigvd.controller.DIRECTION;
import ch.heigvd.controller.Position;

import java.util.LinkedList;

public class Snake {
    private static int idCounter;
    private int id;
    private int score = 0;

    boolean alive = true;
    boolean eat = false;
    private LinkedList<Position> body = new LinkedList<Position>();

    public Snake(Position initialPosition, short initialLength) {
        id = idCounter++;
        Position previousPosition = null;
        for (int i = initialLength; i > 0; --i) {
            int x = initialPosition.getX() + i * DIRECTION.getCoef(initialPosition.getDirection())[0];
            int y = initialPosition.getY() + i * DIRECTION.getCoef(initialPosition.getDirection())[1];
            Position position = new Position(x, y, initialPosition.getDirection(), getBodyRepresentation(initialPosition, previousPosition));
            previousPosition = position;
            body.add(position);
        }
    }

    public void setNextDirection(DIRECTION direction) {
        if (!verifyNextDirection(direction)) return;
        body.getFirst().setDirection(direction);
    }

    private boolean verifyNextDirection(DIRECTION direction) {
        return switch (direction) {
            case UP -> getDirection() != DIRECTION.DOWN;
            case DOWN -> getDirection() != DIRECTION.UP;
            case LEFT -> getDirection() != DIRECTION.RIGHT;
            case RIGHT -> getDirection() != DIRECTION.LEFT;
            default -> false;
        };
    }

    public DIRECTION getDirection() {
        return body.getFirst().getDirection();
    }

    public Position getHead() {
        return body.getFirst();
    }

    private char getBodyRepresentation(Position position, Position previousPosition) {
        if (previousPosition == null) {
            return HEAD.getHead(position.getDirection());
        }
        if (position.getDirection() == previousPosition.getDirection()) {
            return BODY.getBody(position.getDirection());
        } else {
            return ANGLE.getAngle(position.getDirection(), previousPosition.getDirection());
        }
    }

    public LinkedList<Position> getPositions() {
        return body;
    }

    private boolean checkAutoCollision() {
        for (int i = 1; i < body.size(); i++) {
            if (body.getFirst().equals(body.get(i))) {
                alive = false;
                break;
            }
        }
        return alive;
    }

    public void step() {
        Position head = body.getFirst();
        if (!checkAutoCollision()) return;
        head.setRepresentation(getBodyRepresentation(head, head));

        Position newHead = new Position(
                head.getX() + DIRECTION.getCoef(head.getDirection())[0],
                head.getY() + DIRECTION.getCoef(head.getDirection())[1],
                head.getDirection(), HEAD.getHead(head.getDirection()));
        body.addFirst(newHead);
        if (!eat){
            body.removeLast();
        } else {
            score++;
            eat = false;
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void grow() {
        eat = true;
    }

    public int getId() {
        return id;
    }

    public String getScore() {
        return "Score: " + score;
    }

    public boolean isAlive() {
        return alive;
    }
}
