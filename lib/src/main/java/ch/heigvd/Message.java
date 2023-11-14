package ch.heigvd;

import java.io.BufferedReader;
import java.io.IOException;

public enum Message {
    INIT("INIT"),
    DONE("DONE"),
    LOBB("LOBB"),
    JOIN("JOIN"),
    RADY("RADY"),
    STRT("STRT"),
    DIRE("DIRE"),
    UPTE("UPTE"),
    ENDD("ENDD"),
    QUIT("QUIT"),
    UNKN("UNKN");
    private final String message;
    final static char EOT = 0x04;

    Message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    public static Message fromString(String message) {
        for (Message m : Message.values()) {
            if (m.toString().equalsIgnoreCase(message)) {
                return m;
            }
        }
        return UNKN;
    }

    public static String setCommand(Message message, String data) throws IOException {
        if (data == null) {
            return message.toString() + EOT;
        } else {
            return message.toString() + " " + data + EOT;
        }
    }

    public static String setCommand(Message message) throws IOException {
        return setCommand(message, null);
    }

    // TODO verifier si il existe pas une methode dans la lib pour faire ca
    public static String getResponse(BufferedReader reader) throws IOException {
        StringBuilder response = new StringBuilder();
        int c;

        while ((c = reader.read()) != -1) {
            if (c == EOT) {
                break;
            }
            response.append((char) c);
        }

        return response.toString();
    }

    public static String getMessage(String string) throws IOException {
        return string.split(" ", 2)[0];
    }

    public static String getData(String string) throws IOException {
        String[] tab = string.split(" ", 2);
        return tab.length > 1 ? tab[1] : "";
    }
}
