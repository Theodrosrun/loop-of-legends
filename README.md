# Loop of Legends: The Multi-Snake Challenge

**"Loop of Legends: The Multi-Snake Challenge"** is a networked snake game where players control snakes and try to survive as long as possible. This project implements the game client, which connects to a game server for a multiplayer experience.

---

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

---

## 2. Running the Application

### 2.1 Client
To launch the client, use the following command:
```bash
java -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
java -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

### 2.2 Server
To launch the server, use:
```bash
java -jar server-1.0-SNAPSHOT.jar
```
Example:
```bash
java -jar server-1.0-SNAPSHOT.jar
```

---

## 3. Application Protocol

The game uses a TCP-based protocol for communication between the client and the server. The protocol defines the following actions:

### Port and Protocol Specification
- **Protocol**: TCP (Transmission Control Protocol).
- **Port**: 20000.

### Connection Initiation
- The connection is initiated by the client.
- The client sends an **INIT** message to the server to establish a connection.
- The server, upon receiving the **INIT** message, responds with **DONE** if the connection is successfully established.

### Messages/Actions

The client can send the following messages:
- **INIT**: Start the connection.
- **LOBB**: Check lobby availability.
- **JOIN**: Join the game lobby.
- **RADY**: Indicating readiness for the game.
- **REPT**: Reporting game events or statuses.
- **DIRE  <dir>**: Indicating the snake's direction.

The server can send the following messages:
- **STRT**: Signaling the start of the game.
- **DONE**: Indicating successful completion of the previous request.
- **UPTE <map>**: Updating the game state.
- **ENDD**: Indicates the end of the game.

Common messages
- **MSGG**: General messaging within the game.
- **QUIT**: Exit the game.
- **EROR**: Error message for any issues encountered.
- **UNKN**: Represents an unknown or invalid message.

### Success/Error Codes
- **DONE**: Action completed successfully.
- **EROR**: An error occurred, followed by an error message.

### Edge-Cases Handling
- **Lost Connection**: If the client loses connection unexpectedly, the server will handle the dropout and update the game state accordingly.
- **Invalid Message (UNKN)**: If the server receives an unrecognized message, it responds with an **EROR** message.
- **Late Joining**: If a client attempts to join a game in progress, the server responds with an **EROR** message.

### Protocol Diagram
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/uml1.png)
