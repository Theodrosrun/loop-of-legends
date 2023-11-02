package ch.heigvd;

public class Board {

    private final short horizhontalUnit = 4;
    private final char horizontalBorder = '═';
    private final char verticalBorder = '║';
    private final char cornerBorder = '╔';
    private final char cornerBorder2 = '╗';
    private final char cornerBorder3 = '╚';
    private final char cornerBorder4 = '╝';

    //TODO board should be private
    public char[][] board;

    private Snake[] snakes;

    public Board(int width, int height, short nbSnakes) {
        board = new char[height ][width * horizhontalUnit];
        setBorder();
        Position position = new Position(4,5, DIRECTION.RIGHT, ' ');
        Snake snake = new Snake(position, (short) 5);
        snakes = new Snake[]{snake};
        setSnakes();
    }

    private void setSnakes(){
        for (Snake snake : snakes) {
            for (Position position : snake.getSnake()) {
                board[position.y][position.x*horizhontalUnit] = position.representation;
            }
        }
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


    public Board getBoard() {
        return this;
    }

}
