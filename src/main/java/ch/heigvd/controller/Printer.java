package ch.heigvd.controller;

public class Printer {
    public static void Print(Board board) {
        clearScreen();
        for (char[] aChar : board.getBoard()) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                System.out.print(aChar[j]);
            }
            System.out.println();
        }
    }

    private static void Score(Board board){

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
