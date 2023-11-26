package ch.heigvd;

/**
 * The class that represent the position on the map
 */
public class Position {

    /**
     * The x and y coordinates of the position
     */
    private final int x;

    /**
     *  The x and y coordinates of the position
     */
    private final int y;

    /**
     * The direction of the position
     */
    private DIRECTION direction;

    /**
     * The representation of the position on board
     */
    private char representation;

    /**
     * Constructor
     * @param x The x coordinate of the position
     * @param y The y coordinate of the position
     * @param representation The representation of the position on board
     */
    public Position(int x, int y, char representation) {
        this.x = x;
        this.y = y;
        this.direction = null;
        this.representation = representation;
    }

    /**
     * Constructor
     * @param x The x coordinate of the position
     * @param y The y coordinate of the position
     * @param direction The direction of the position
     * @param representation The representation of the position on board
     */
    public Position(int x, int y, DIRECTION direction, char representation) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.representation = representation;
    }

    /**
     * get the x coordinate of the position
     * @return The x coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * get the y coordinate of the position
     * @return The y coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * get the direction of the position
     * @return The direction of the position
     */
    public DIRECTION getDirection() {
        return direction;
    }

    /**
     * set the direction of the position
     * @param direction The direction of the position
     */
    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    /**
     * get the representation of the position on board
     * @return The representation of the position on board
     */
    public char getRepresentation() {
        return representation;
    }

    /**
     * set the representation of the position on board
     * @param representation The representation of the position on board
     */
    public void setRepresentation(char representation) {
        this.representation = representation;
    }

    /**
     * Compare two positions with their coordinates
     * @param obj The position to compare
     * @return true if the positions are equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position position) {
            return position.getX() == this.getX() && position.getY() == this.getY();
        }
        return false;
    }

}
