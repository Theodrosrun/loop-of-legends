package ch.heigvd;

import ch.heigvd.Food;
import ch.heigvd.snake.Snake;
import ch.qos.logback.core.joran.sanity.Pair;

import java.util.HashMap;

import static java.lang.Math.abs;

public class Board {

    private final short hCoef = 1;
    private final short vCoef = 1;

    private final char emptyChar = ' ';
    private final char horizontalBorder = '═';
    private final char verticalBorder = '║';
    private final char cornerBorder = '╔';
    private final char cornerBorder2 = '╗';
    private final char cornerBorder3 = '╚';
    private final char cornerBorder4 = '╝';

    //TODO board should be private
    private char[][] board;

    private Snake[] snakes;
    private Food foods;

    public Board(int width, int height, short nbSnakes, short foodQuantity, short foodFrequency) {
        board = new char[height * vCoef + 2][width * hCoef + 2];
        setBorder();
        foods = new Food(foodQuantity, foodFrequency);
    }

    private void clearBoard() {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[i][j] = emptyChar;
            }
        }
    }

    private int getRelativeValue(int value, int limit) {

        value = value * hCoef;
        int relativeValue = value % limit;

        if (value % limit < 1) {
            relativeValue = limit - (1 + abs(relativeValue));
        }
        return relativeValue;

    }

    public int getRelativeX(int x) {
        return getRelativeValue(x, board[0].length - 1);
    }

    public int getRelativeY(int y) {
        return getRelativeValue(y, board.length - 1);
    }

    public void deploySnakes() {
        clearBoard();
        for (Snake snake : snakes) {
            if (eat(snake.getHead())){
                snake.grow();
            }
            for (Position position : snake.getPositions()) {
                int y = getRelativeY(position.getY());
                int x = getRelativeX(position.getX());
                board[y][x] = position.getRepresentation();
            }


        }
    }

    public void deployFood() {
          for (Position food : foods.getFood()) {
            int y = getRelativeY(food.getY());
            int x = getRelativeX(food.getX());
            if (board[y][x] == emptyChar) {
                board[y][x] = food.getRepresentation();
            }
        }
    }

    public boolean eat(Position position) {
        int y = getRelativeY(position.getY());
        int x = getRelativeX(position.getX());
        for (Position food : foods.getFood()) {
            int foodY = getRelativeY(food.getY());
            int foodX = getRelativeX(food.getX());
            if (foodX == x && foodY == y) {
                if (foods.isEated(food)) {
                    return false;
                }
                foods.removeFood(food);
                return true;
            }
        }
        return false;
    }

    private void setBorder() {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[i][j] = ' ';
            }
        }

        for (int i = 0; i < board.length; i++) {
            board[i][0] = verticalBorder;
            board[i][board[0].length - 1] = verticalBorder;
        }
        for (int i = 0; i < board[0].length; i++) {
            board[0][i] = horizontalBorder;
            board[board.length - 1][i] = horizontalBorder;
        }
        board[0][0] = cornerBorder;
        board[0][board[0].length - 1] = cornerBorder2;
        board[board.length - 1][0] = cornerBorder3;
        board[board.length - 1][board[0].length - 1] = cornerBorder4;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setSnakes(Snake... snakes) {
        this.snakes = snakes;
    }


}
