package ch.heigvd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Player implements java.io.Serializable {

    private final char MASTER_LOGO = '♔';
    private static int idCnt = 0;
    private int id;
    private boolean master;
    private String name;
private PlayerStatus status = PlayerStatus.DISCONNECTED;

    public Player(String name) {
        this.id = ++idCnt;
        this.name = name;
        status = PlayerStatus.CONNECTED;
    }

    public char getMasterLogo() {
        if (master) {
            return MASTER_LOGO;
        }
        return ' ';
    }

    public String getName() {
        return name;
    }

    public void setReady() {
       status = PlayerStatus.READY;
    }

    public boolean isReady() {
        return status == PlayerStatus.READY;
    }

    public void setMaster() {
        this.master = true;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player player){
            return this.id == player.id;
        }
        return false;
    }

    public void removeMaster() {
        this.master = false;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public boolean isMaster() {
        return master;
    }
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

    public static Player deserialize(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            Player p = (Player) ois.readObject();
            return p;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
