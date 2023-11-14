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
    UNKN("UNKN");
    private final String message;
    private final char EOT = 4;

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
            return message.toString() + message.EOT;
        } else {
            return message.toString() + " " + data + message.EOT;
        }
    }

    public static String setCommand(Message message) throws IOException {
        return setCommand(message, null);
    }

    public static String[] getResponse(BufferedReader reader) throws IOException {
        StringBuilder response = new StringBuilder();
        int c;

        while ((c = reader.read()) != -1) {
            if (c == 4) { // Le caractère 4 correspond à EOT (End Of Transmission)
                break;  // Sortir de la boucle une fois que EOT est trouvé
            }
            response.append((char) c);
        }

        return response.toString().split(" ");
    }

    public static String getMessage(BufferedReader reader) throws IOException {
        return getResponse(reader)[0];
    }

    public static String getData(BufferedReader reader) throws IOException {
        String[] response = getResponse(reader);
        return response.length > 1 ? response[1] : "";
    }
}
