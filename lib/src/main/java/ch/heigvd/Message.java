package ch.heigvd;

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
}
