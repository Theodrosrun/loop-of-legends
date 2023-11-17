package ch.heigvd;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Setting implements java.io.Serializable {
    private boolean wallEnabled;
    private int speed;
    private int HorizontalSize;
    private int VerticalSize;

    public Setting(boolean wallEnabled, int speed, int HorizontalSize, int VerticalSize) {
        this.wallEnabled = wallEnabled;
        this.speed = speed;
        this.HorizontalSize = HorizontalSize;
        this.VerticalSize = VerticalSize;
    };

    public byte[] serialize() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Setting deserialize(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Setting) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
