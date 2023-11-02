package ch.heigvd;
import ch.heigvd.DIRECTION;

public class Position {
    int x;
    int y;
    DIRECTION direction;
    char representation;

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
}
