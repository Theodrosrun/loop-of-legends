package ch.heigvd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Server.class);

        logger.debug("Hello from server!");
    }
}