# Loop of Legends: The Multi-Snake Challenge

**"Loop of Legends: The Multi-Snake Challenge"** is a networked snake game where players control snakes and try to survive as long as possible. This project implements the game client, which connects to a game server for a multiplayer experience.

---

## 1. Building the Application

Loop of snake has been developed in a Maven repository utilizing the multi-module approach. To launch this game, it is necessary to open two separate terminals.

In the first terminal, you will start the server module. This module is responsible for managing interactions between players and maintaining the overall state of the game. 

In the second terminal, you will launch the client module. The client is the interface through which players interact with the game. This module communicates with the server to send commands and receive updates on the game's state.

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

### 2.1 Launching server
To launch the server, use the following command in the target folder:

#### Windows:
```bash
javaw -jar server-1.0-SNAPSHOT.jar [port]
```
Example:
```bash
javaw -jar server-1.0-SNAPSHOT.jar 40000
```

<br>

#### Linux:
```bash
java -jar server-1.0-SNAPSHOT.jar [port]
```
Example:
```bash
java -jar server-1.0-SNAPSHOT.jar 40000
```

<br>

### 2.2 Launching client
To launch the client, use the following command in the target folder:

#### Windows:
```bash
javaw -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
javaw -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

<br>

#### Linux:
```bash
java -jar client-1.0-SNAPSHOT.jar [server-address] [port]
```
Example:
```bash
java -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

<br>

### 2.3 How to play
Now that you are ready, Follow these simple steps to start playing and enjoy the game:

#### **Enter Your Username**:
   - Start by entering your unique username. This name will represent you in the game and will be visible to other players. Choose a name that is distinctive and memorable!

#### **Declare Ready**:
   - Once you have entered your name and are ready to play, press the 'R' key. This will signal to other players and the game system that you are ready to begin. Wait for other players to also declare themselves ready.

#### **Using the Arrow Keys**:
   - The game begins! Use the arrow keys to control your snake in the game arena.
     - **Up:** Press the up arrow to move your snake upwards.
     - **Right:** Use the right arrow to turn right.
     - **Down:** The down arrow will move your snake downwards.
     - **Left:** And finally, use the left arrow to turn left.

#### **Game Objective**:
   - Your goal is to grow your snake by eating special items that appear in the arena.
   - The more you eat, the longer your snake grows, making the game more challenging and exciting.

#### **Exiting the Game**:
- If you wish to leave the game, simply press the 'Q' key (or the designated exit key). This action will safely exit you from the game session.

---

## 3. Application Protocol

## 3.1 Overview
The Loop of Legend (LOL) protocol is designed specifically for online multiplayer snake games, operating on a client-server architecture. This protocol facilitates real-time interactions and gameplay over a network, providing a seamless gaming experience.

- The main objective of the LOL protocol is to enable players to play the classic snake game in a networked, multiplayer environment.
- Players (clients) connect to a central game server. The server manages game lobbies, player actions, and game state updates.
- To start a game, a client connects to the server and requests to join a game. The game begins when the lobby has sufficient players, and all players indicate they are ready.

## 3.2 Transport protocol
### Protocol and port
- **Protocol**: TCP (Transmission Control Protocol) for reliable, ordered, and error-checked delivery of game data.
- **Port**: The default communication port for the LOL protocol is 20000.

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
- `DIRE <direction>`: Indicating the snake's direction.
    - `0`: Up direction
    - `1`: Down direction
    - `2`: Left direction
    - `3`: Right direction

The server can send the following messages:
- `DONE`: Indicating successful completion of the previous request. See details below.
- `STRT`: Signaling the start of the game.
- `REPT`: Reporting game events or status.
- `UPTE <map>`: Used to send the map. Updating the game state.
- `EROR`: Error message for any issues encountered. See details below.
- `ENDD`: Indicates the end of the game.

Common messages
- `MSGG`: This message is used for general communication within the game. It can be employed for various purposes, such as sending information about current actions. The specific content of the MSGG message would depend on the context in which it is used.
- `QUIT`: This message is sent when exiting the game. Upon receiving a QUIT message, the server typically performs cleanup operations related to the client, such as removing the player from the game lobby or freeing up resources.
- `UNKN`: This message represents an unknown or invalid command. This could happen if there's a typo in the command, the client uses a command not supported by the server or the message format is incorrect.

### Success/Error Codes
- `DONE`: This code indicates that an action requested by the client has been completed successfully. For example, when a player successfully joins a lobby, the server might respond with a DONE message. It's a general acknowledgment sent from the server to confirm that the requested operation was executed without any issues.
- `EROR`: This code signifies that an error occurred while processing the client's request. It is usually followed by an error message that provides more details about the nature of the error. Examples of situations that might generate an EROR message include attempts to join a full game lobby. The accompanying error message helps in understanding the specific reason for the failure.

### Edge-Cases Handling
- **Lost Connection**: If the client loses connection unexpectedly, the server will handle the dropout and update the game state accordingly.

## 3.4 Examples
### Client plays
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/clientPlays.png)
### Clients play
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/clientsPlay.png)
### Client leaves
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/clientLeaves.png)
### Lobby is full
![Example Image](https://github.com/Theodrosrun/loop-of-legends/blob/23-protocol-finalization/docs/lobbyIsFull.png)
