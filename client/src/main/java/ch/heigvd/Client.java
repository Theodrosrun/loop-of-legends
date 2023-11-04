package ch.heigvd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Client.class);

        logger.debug("Hello from client!");
    }
}