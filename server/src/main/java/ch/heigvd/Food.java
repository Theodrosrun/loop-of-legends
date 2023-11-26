package ch.heigvd;
import java.util.ArrayList;

public class Food {

    /**
     * the list of food present on the map
     */
    private final ArrayList<Position> food;

    /**
     * the list of food that has been eated
     */
    private final ArrayList<Position> eatedFood = new ArrayList<>();

    /**
     * the representation of the food
     */
    private final char representation = '⭑';

    /**
     * the representation of an empty space
     */
    private final char emptyChar = ' ';

    /**
     * the frequency of the food generation
     */
    private final int frequency;

    /**
     * the quantity of food generated each time
     */
    private final int quantity;

    /**
     * the time counter that generate the food
     */
    private int counter = 0;
    
    /**
     * Constructor
     * @param quantity the quantity of food generated each time
     * @param frequency the frequency of the food generation
     */
    public Food(int quantity, int frequency) {
        food = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            food.add(new Position(0, 0, representation));
        }
        this.frequency = frequency;
        this.quantity = quantity;
    }

    /**
     * get the list of food
     * @return the list of food
     */
    public ArrayList<Position> getFood(){
        generateFood();
        return food;
    }

    /**
     * remove the food at the given position
     * @param position the position of the food to remove
     */
    public void removeFood(Position position){
        for (Position f : food) {
            if (f.equals(position)) {
                f.setRepresentation(emptyChar);
                eatedFood.add(f);
                return;
            }
        }
    }

    /**
     * check if the food at the given position has been eated
     * @param position the position of the food to check
     * @return true if the food has been eated, false otherwise
     */
    public boolean isEated(Position position){
        for (Position f : eatedFood) {
            if (f.equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * generate the food
     */
    private void generateFood(){
        eatedFood.clear();
        if (counter++ % frequency == 0) {
            for (int i = 0; i < quantity; i++) {
                food.set(i, getRandPosition());
            }
        }
    }

    /**
     * get a random position
     * @return a random position
     */
    private Position getRandPosition(){
        int x = (int) (Math.random() * Math.random());
        int y = (int) (Math.random() * Math.random());
        return new Position(x, y, representation);
    }
}
