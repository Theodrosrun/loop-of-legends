# Loop of Legends: The Multi-Snake Challenge

**"Loop of Legends: The Multi-Snake Challenge"** is a networked snake game where players control snakes and try to survive as long as possible. This project implements the game client, which connects to a game server for a multiplayer experience.

## 1. Building the Application

### Prerequisites
- Java JDK 11 or higher.

### Build Instructions

Before you begin with FileEditor, please execute the following commands:

```sh
# Download the dependencies
./mvnw dependency:resolve

# Package the application
./mvnw package
```

## 2. Running the Application

To launch the client, use the following command:
```bash
java -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
java -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

To launch the server, use:
```bash
java -jar server-1.0-SNAPSHOT.jar
```
Example:
```bash
java -jar server-1.0-SNAPSHOT.jar
```

## 3. Application Protocol

The game uses a TCP-based protocol for communication between the client and the server. The protocol defines the following actions:

- **INIT**: The client sends INIT to establish the connection.
- **LOBBY**: The client checks for availability in the lobby.
- **JOIN**: The client joins the lobby.
- **READY**: The client signals that it is ready.
- **START**, **UPDATE**, **END**: The server controls the game flow.
- **DIR**: The client sends the direction for the snake.
