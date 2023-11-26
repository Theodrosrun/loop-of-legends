package ch.heigvd;
import ch.heigvd.snake.Snake;
import java.util.ArrayList;


import static java.lang.Math.abs;
/**
 * The class that represent the board of the game
 */
public class Board {

    /**
     * the representation of the border
     */
    private final char emptyChar = ' ';

    /**
     * the representation of the border
     */
    private final BorderType borderType = BorderType.NORMAL;

    /**
     * the board
     */
    private final char[][] board;

    /**
     * the food on the board
     */
    private final Food foods;

    /**
     * Constructor
     * @param width the width of the board
     * @param height the height of the board
     * @param foodQuantity the quantity of food generated each time
     * @param foodFrequency the frequency of the food generation
     */
    public Board(int width, int height, int foodQuantity, int foodFrequency) {
        board = new char[height + 2][width + 2];
        setBorder(board);
        foods = new Food(foodQuantity, foodFrequency);
    }

    /**
     * get the relative x position of the board
     * @param x the x position
     * @return the relative x position of the board
     */
    public int getRelativeX(int x) {
        return getRelativeValue(x, board[0].length - 1);
    }

    /**
     * get the relative y position of the board
     * @param y the y position
     * @return the relative y position of the board
     */
    public int getRelativeY(int y) {
        return getRelativeValue(y, board.length - 1);
    }

    /**
     * deploy the snakes on the board
     * @param snakes the list of snakes to deploy
     */
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

    /**
     * deploy the lobby on the board
     * @param lobby the lobby to deploy
     */
    public void deployLobby(Lobby lobby) {
        setBorder(board);
        for ( int i = 0; i < lobby.getNbPlayer(); ++i) {
            StringBuilder sb = new StringBuilder(Border.VERTICAL.getBorder(borderType) + " ");
            int maxNameSize = getWidth() - 11;
            Player player = lobby.getPlayers().get(i);
            String ready = player.isReady() ? "READY": "WAIT ";

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

    /**
     * deploy the food on the board
     */
    public void deployFood() {
          for (Position food : foods.getFood()) {
            int y = getRelativeY(food.getY());
            int x = getRelativeX(food.getX());
            if (board[y][x] == emptyChar) {
                board[y][x] = food.getRepresentation();
            }
        }
    }

    /**
     * check if the snake at the given position eat a food
     * @param position the position of the snake
     * @return true if the snake eat a food, false otherwise
     */
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

    /**
     * get the width of the board
     * @return the width of the board
     */
    public int getWidth() {
        return board[0].length;
    }

    /**
     * get the height of the board
     * @return the height of the board
     */
    public int getHeight(){
        return board.length;
    }

    /**
     * clear the board
     */
    private void clearBoard() {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[i][j] = emptyChar;
            }
        }
    }

    /**
     * get the relative value of the given value
     * @param value the value to get the relative value
     * @param limit the limit of the value
     * @return the relative value of the given value
     */
    private int getRelativeValue(int value, int limit) {

        int relativeValue = value % limit;

        if (value % limit < 1) {
            relativeValue = limit - (1 + abs(relativeValue));
        }
        return relativeValue;

    }

    /**
     * set the border of the board
     * @param board the board to set the border
     */
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
        board[0][board[0].length - 1] = Border.CORNER_TOP_RIGHT.getBorder(borderType);
        board[board.length - 1][0] = Border.CORNER_BOTTOM_LEFT.getBorder(borderType);
        board[board.length - 1][board[0].length - 1] = Border.CORNER_BOTTOM_RIGHT.getBorder(borderType);
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
}
