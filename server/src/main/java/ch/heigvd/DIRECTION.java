package ch.heigvd;

public enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT;


    public static DIRECTION parseKey(KEY key){

        switch (key) {
            case UP -> {
                return DIRECTION.UP;
            }
            case DOWN -> {
                return DIRECTION.DOWN;
            }
            case LEFT -> {
                return DIRECTION.LEFT;
            }
            case RIGHT -> {
                return DIRECTION.RIGHT;
            }
            default -> {
                return null;
            }

        }
    }
    public static short[] getCoef(DIRECTION direction){
        return switch (direction) {
            case UP -> new short[]{0, -1};
            case DOWN -> new short[]{0, 1};
            case LEFT -> new short[]{-1, 0};
            case RIGHT -> new short[]{1, 0};
            default -> new short[]{0, 0};
        };
    }
}
