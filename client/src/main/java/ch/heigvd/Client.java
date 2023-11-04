package ch.heigvd;

import ch.heigvd.asyncInputManager.AsyncInputManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Client.class);

        AsyncInputManager.testShowKeys();


        logger.debug("Hello from client!");
    }

    public void Sender() {

    }

    public void Receiver() {

    }
}