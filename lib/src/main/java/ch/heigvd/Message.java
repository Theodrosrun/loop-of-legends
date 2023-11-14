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
    MSGG("MSGG"),
    EROR("EROR"),
    DIRE("DIRE"),
    UPTE("UPTE"),
    ENDD("ENDD"),
    QUIT("QUIT"),
    UNKN("UNKN");
    final static String SEPARATOR = " ";
    final static char EOT = 0x04;
    private final String message;

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

    public static String setCommand(Message message, String data){
        if (data == null) {
            return message.toString() + EOT;
        } else {
            return message.toString() + SEPARATOR + data + EOT;
        }
    }

    public static String setCommand(Message message) {
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

    public static String getMessage(String string) {
        return string.split(SEPARATOR, 2)[0];
    }

    public static String getData(String string) {
        String[] tab = string.split(SEPARATOR, 2);
        return tab.length > 1 ? tab[1] : "";
    }
}