package ch.heigvd.controller;

public class Printer {
    public static void Print(char[][] chars) {
        clearScreen();
        for (char[] aChar : chars) {
            for (int j = 0; j < chars[0].length; j++) {
                System.out.print(aChar[j]);
            }
            System.out.println();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
