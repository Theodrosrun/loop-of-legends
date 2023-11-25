# Loop of Legends: The Multi-Snake Challenge

**"Loop of Legends: The Multi-Snake Challenge"** is a networked snake game where players control snakes and try to survive as long as possible. This project implements the game client, which connects to a game server for a multiplayer experience.

---

## 1. Building the Application

Loop of snake has been developed in a Maven repository utilizing the multi-module approach. To launch this game, it is necessary to open two separate terminals.

In the first terminal, you will start the server module. This module is responsible for managing interactions between players and maintaining the overall state of the game. 

In the second terminal, you will launch the client module. The client is the interface through which players interact with the game. This module communicates with the server to send commands and receive updates on the game's state.

### Prerequisites
- Java JDK 11 or higher.

### Build Instructions

Before you begin with Loop of Legends, please execute the following commands:

```sh
# Download the dependencies
./mvnw dependency:resolve

# Package the application
./mvnw package
```

---

## 2. Running the Application

### 2.1 Server
To launch the server, use the following command in the target folder:

#### Windows:
```bash
javaw -jar server-1.0-SNAPSHOT.jar
```
Example:
```bash
javaw -jar server-1.0-SNAPSHOT.jar
```

#### Linux:
```bash
java -jar server-1.0-SNAPSHOT.jar
```
Example:
```bash
java -jar server-1.0-SNAPSHOT.jar
```

### 2.2 Client
To launch the client, use the following command in the target folder:

#### Windows:
```bash
javaw -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
javaw -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

#### Linux:
```bash
java -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
java -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```


## 3. Application Protocol

## 3.1 Overview
- The LOL protocol is meant to play snake over the network.
- The LOL protocol is a client-server protocol.
- The client connects to a server and requests a game. If the lobby is not full and everyone is ready, the game starts.

## 3.2 Transport protocol
### Protocol and port
- **Protocol**: TCP (Transmission Control Protocol).
- **Port**: 20000.

### Connection Initiation
- The connection is initiated by the client.
- The client sends an **INIT** message to the server to establish a connection.
- The server, upon receiving the **INIT** message, responds with **DONE** if the connection is successfully established.

## 3.3 Messages/Actions
The client can send the following messages:
- `INIT`: Start the connection.
- `LOBB`: Check lobby availability.
- `JOIN`: Join the game lobby.
- `RADY`: Indicating readiness for the game.
- `REPT`: Reporting game events or status.
- `DIRE <direction>`: Indicating the snake's direction.
    - `0`: Up direction
    - `1`: Down direction
    - `2`: Left direction
    - `3`: Right direction

The server can send the following messages:
- `STRT`: Signaling the start of the game.
- `DONE`: Indicating successful completion of the previous request.
- `UPTE <map>`: Used to send the map. Updating the game state.
- `ENDD`: Indicates the end of the game.

Common messages
- `MSGG`: General messaging within the game.
- `QUIT`: Exit the game.
- `EROR`: Error message for any issues encountered.
- `UNKN`: Represents an unknown or invalid message.

### Success/Error Codes
- `DONE`: Action completed successfully.
- `EROR`: An error occurred, followed by an error message.

### Edge-Cases Handling
- **Lost Connection**: If the client loses connection unexpectedly, the server will handle the dropout and update the game state accordingly.
- **Invalid Message (UNKN)**: If the server receives an unrecognized message, it responds with an **EROR** message.
- **Late Joining**: If a client attempts to join a game in progress, the server responds with an **EROR** message.

## 3.4 Examples
### Client plays
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/clientPlays.png)
### Client leaves
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/clientLeaves.png)
### Lobby is full
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/lobbyIsFull.png)
