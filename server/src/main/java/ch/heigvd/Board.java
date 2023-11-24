package ch.heigvd;

import ch.heigvd.Food;
import ch.heigvd.snake.Snake;
import ch.qos.logback.core.joran.sanity.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Board {

    private final char emptyChar = ' ';

    private BorderType borderType = BorderType.NORMAL;


    //TODO board should be private
    private char[][] board;

    private Food foods;

    public Board(int width, int height, short nbSnakes, short foodQuantity, short foodFrequency) {
        board = new char[height + 2][width + 2];
        setBorder(board);
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

    public void deploySnakes(ArrayList<Snake> snakes) {
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

    public void deployLobby(Lobby lobby) {

        for ( int i = 0; i < lobby.getNbPlayer(); ++i) {
            int maxNameSize = getWidth() - 11;
            Player player = lobby.getPlayers().get(i);
            String ready = player.isReady() ? "READY": "WAIT ";
            StringBuilder sb = new StringBuilder(Border.VERTICAL.getBorder(borderType) + " ");
            sb.append(player.getId()).append(" ");
            sb.append(player.getName(), 0, Math.min(player.getName().length(), maxNameSize));
            if (player.getName().length() - maxNameSize < 0) {
                sb.append(String.valueOf(emptyChar).repeat(maxNameSize - player.getName().length()));
            }
            sb.append(" ");
            sb.append(ready);
            sb.append(Border.VERTICAL.getBorder(borderType));
            board[i + 1] = sb.toString().toCharArray();
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

    private void setBorder(char[][] board) {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[i][j] = ' ';
            }
        }

        for (int i = 0; i < board.length; i++) {
            board[i][0] = Border.VERTICAL.getBorder(borderType);
            board[i][board[0].length - 1] = Border.VERTICAL.getBorder(borderType);
        }
        for (int i = 0; i < board[0].length; i++) {
            board[0][i] = Border.HORIZONTAL.getBorder(borderType);
            board[board.length - 1][i] = Border.HORIZONTAL.getBorder(borderType);
        }
        board[0][0] = Border.CORNER_TOP_LEFT.getBorder(borderType);
        board[0][board[0].length - 1] = Border.CORNER_TOP_RIGHT.getBorder(borderType);;
        board[board.length - 1][0] = Border.CORNER_BOTTOM_LEFT.getBorder(borderType);;
        board[board.length - 1][board[0].length - 1] = Border.CORNER_BOTTOM_RIGHT.getBorder(borderType);;
    }

    public char[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char[] line : board) {
            for (char c : line) {
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getWidth() {
        return board[0].length;
    }

    public int getHeigth(){
        return board.length;
    }
}
