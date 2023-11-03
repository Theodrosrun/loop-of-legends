package ch.heigvd.controller;

public class Position {
    private int x;
    private int y;
    private DIRECTION direction;
    private char representation;

    public Position(int[] xy, DIRECTION direction, char representation) {
        this.x = xy[0];
        this.y = xy[1];
        this.direction = direction;
        this.representation = representation;
    }
    public Position(int x, int y, DIRECTION direction, char representation) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.representation = representation;
    }

    public int[] getXY() {
        return new int[]{x, y};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public char getRepresentation() {
        return representation;
    }

    public void setDirection(DIRECTION direction){
        this.direction = direction;
    }

}
