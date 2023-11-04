package ch.heigvd;

import ch.heigvd.Position;

import java.util.ArrayList;

public class Food {

    private ArrayList<Position> food;
    private ArrayList<Position> eatedFood = new ArrayList<>();
    private final char representation = '⭑';
    private final char emptyChar = ' ';
    private final int frequency;
    private final int quantity;
    private int counter = 0;

    public Food(int quantity, int frequency) {
        food = new ArrayList<>(quantity);
        for (int i = 0; i < quantity; i++) {
            food.add(new Position(0, 0, representation));
        }
        this.frequency = frequency;
        this.quantity = quantity;
    }
    private void generateFood(){
        eatedFood.clear();
        if (counter++ % frequency == 0) {
            for (int i = 0; i < quantity; i++) {
                food.set(i, getRandPosition());
            }
        }
    }

    private Position getRandPosition(){
        int x = (int) (Math.random() * 20);
        int y = (int) (Math.random() * 20);
        return new Position(x, y, representation);
    }
    public ArrayList<Position> getFood(){
        generateFood();
        return food;
    }

    public void removeFood(Position position){
        for (Position f : food) {
            if (f.equals(position)) {
                f.setRepresentation(emptyChar);
                eatedFood.add(f);
                return;
            }
        }
    }

    public boolean isEated(Position position){
        for (Position f : eatedFood) {
            if (f.equals(position)) {
                return true;
            }
        }
        return false;
    }

    public char getRepresentation() {
        return representation;
    }
}
