package ch.heigvd.controller;

import ch.heigvd.entity.snake.Snake;

import static java.lang.Math.abs;

public class Board {

    private final short hCoef = 1;
    private final short vCoef = 1;
    private final char horizontalBorder = '═';
    private final char verticalBorder = '║';
    private final char cornerBorder = '╔';
    private final char cornerBorder2 = '╗';
    private final char cornerBorder3 = '╚';
    private final char cornerBorder4 = '╝';

    //TODO board should be private
    private char[][] board;

    private Snake[] snakes;

    public Board(int width, int height, short nbSnakes) {
        board = new char[height * vCoef + 2][width * hCoef + 2];
        setBorder();
    }

    private void clearBoard() {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[i][j] = ' ';
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

    public int getRelativX(int x) {
        return getRelativeValue(x, board[0].length - 1);
    }

    public int getRelativY(int y) {
        return getRelativeValue(y, board.length - 1);
    }

    public void deploySnakes() {
        clearBoard();
        for (Snake snake : snakes) {
            for (Position position : snake.getPositions()) {
                int y = getRelativY(position.getY());
                int x = getRelativX(position.getX());
                board[y][x] = position.getRepresentation();
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

    public char[][] getBoard() {
        return board;
    }

    public void setSnakes(Snake... snakes) {
        this.snakes = snakes;
    }

}
