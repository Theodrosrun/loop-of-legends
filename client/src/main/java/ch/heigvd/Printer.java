package ch.heigvd;

public class Printer {

    private String content;
    public void Print() {
        clearScreen();
        System.out.println(content);
//        for (char[] aChar : board.getBoard()) {
//            for (int j = 0; j < board.getBoard()[0].length; j++) {
//                System.out.print(aChar[j]);
//            }
//            System.out.println();
//        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
