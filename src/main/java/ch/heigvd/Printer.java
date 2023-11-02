package ch.heigvd;

public class Printer {
    public static void Print(Board board) {
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                System.out.print(board.board[i][j]);
            }
            System.out.println();
        }
    }
}
