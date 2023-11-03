package ch.heigvd.entity.snake;


import ch.heigvd.controller.DIRECTION;
import ch.heigvd.controller.Position;

import java.util.LinkedList;

public class Snake {

    //https://en.wikipedia.org/wiki/Box-drawing_character

    private LinkedList<Position> body = new LinkedList<Position>();

    public Snake(Position initialPosition, short initialLength) {
        Position previousPosition = null;
        for (int i = initialLength; i > 0 ; --i) {
            int x = initialPosition.getX() + i * DIRECTION.getCoef(initialPosition.getDirection())[0];
            int y = initialPosition.getY() + i * DIRECTION.getCoef(initialPosition.getDirection())[1];
            Position position = new Position(x,y, initialPosition.getDirection(), getBodyRepresentation(initialPosition, previousPosition));
            previousPosition = position;
            body.add(position);
        }
    }

    public void setNextDirection(DIRECTION direction){
        body.getFirst().setDirection(direction);
    }

    public DIRECTION getDirection(){
        return body.getFirst().getDirection();
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

    public void step(){
        Position head = body.getFirst();
        Position newHead = new Position(head.getX() + DIRECTION.getCoef(head.getDirection())[0], head.getY() + DIRECTION.getCoef(head.getDirection())[1], head.getDirection(), HEAD.getHead(head.getDirection()));
        body.addFirst(newHead);
        body.removeLast();
    }

}
