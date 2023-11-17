package ch.heigvd;

public enum PlayerStatus {
    READY,
    WAITING, LEAVING, CONNECTING, CONNECTED, DISCONNECTED;

    @Override
    public String toString() {
        return switch (this) {
            case READY -> "Ready";
            case WAITING -> "Waiting";
            case LEAVING -> "Leaving";
            case CONNECTING -> "Connecting";
            case CONNECTED -> "Connected";
            case DISCONNECTED -> "Disconnected";
        };
    }
}
