# Board Game Application Help


The Board Game Application is a Spring Boot project that provides a platform for playing various board games. This document provides information on game rules, API endpoints, and configuration options.

### Game Rules
#### Mancala
Mancala is a two-player strategy game. 
Players take turns to sow seeds around the board.
The game ends when a player captures all seeds on their side.
For detailed Mancala rules, refer to Mancala Rules.

#### Mancala Game Play
The player who begins with the first move picks up all the stones in any of his own
six pits, and sows the stones on to the right, one in each of the following pits,
including his own big pit. No stones are put in the opponents' big pit. If the player's
last stone lands in his own big pit, he gets another turn. This can be repeated
several times before it's the other player's turn.
#### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little?) pit.
#### The Game Ends
The game is over as soon as one of the sides runs out of stones. The player who
still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.

- For detailed Mancala rules, refer to [Mancala Rules](https://en.wikipedia.org/wiki/Mancala#:~:text=A%20player%20may%20count%20their,most%20seeds%20in%20the%20bank.).

## API Endpoints
### Create a Game
- **Endpoint**: POST /mancala/start
- **Request Body**: JSON with initial game setup details
- **Request Body Example**
```json
{
  "playerNames" : ["Hana" ,
    "Mona"
  ]
}
```
- **Response**: JSON with the created game details
- **Response Example**
```json
{
  "id": 2,
  "name": "Mancala",
  "status": "PLAYING",
  "gameMatrix": [
    [6, 6, 6, 6, 6, 6, 0],
    [6, 6, 6, 6, 6, 6, 0]
  ],
  "activePlayerIndex": 0,
  "players": [
    {"name": "Hana"},
    {"name": "Mona"}
  ]
}
```
In the Mancala game matrix, the number of rows corresponds to the number of players. 
Each row represents the pits associated with a specific player.
The first row signifies the pits of the first player, the second row is associated with the second player, and so forth. 
Within each row, the last item represents the player's big pit. Initially, at the start of the game, the value in the last position of each row is set to zero.

```
Player 1: [pit, pit, pit, pit, pit, pit, bigPit]
Player 2: [pit, pit, pit, pit, pit, pit, bigPit]
...
Player N: [pit, pit, pit, pit, pit, pit, bigPit]

```

### Play a Move
- **Endpoint**: PUT /mancala/play
- **Request Body**: JSON with game ID, player name, and selected pit index
- **Request Body Example**
```json
{
  "gameId": 5,
  "playerName": "Hana",
  "selectedPit": 3
}
```
- **Response**: JSON with the updated game details after the move
- **Response Example**:
```json
{
  "id": 5,
  "name": "Mancala",
  "status": "PLAYING",
  "gameMatrix": [
    [6, 6, 6, 0, 7, 7, 1],
    [6, 6, 6, 7, 7, 7, 0]
  ],
  "activePlayerIndex": 1,
  "players": [
    {"name": "Hana"},
    {"name": "Mona"}
  ]
}
```
in the above response:
- "id": 3: Indicates the unique identifier of the game.
- "name": "Mancala": Specifies the name of the game as "Mancala."
- "status": "PLAYING": Indicates that the game is currently in progress.
- "gameMatrix": Represents the current state of the game board with two rows, each corresponding to a player's pits. The last element in each row represents the Mancala (large pit) for each player.
- "activePlayerIndex": 1: Specifies that it is currently player with index 1 (Mona)'s turn to play.
- "players": Contains an array of player objects, each with a "name" property. In this case, there are two players - "Hana" and "Mona."


#### Finishing game
If a player successfully completes the game by emptying all the stones in all pits,
the result will include a property called 'winnerPlayerIndex,' indicating the index of the winning player.
The status will be set to FINISHED.
response example : 
```json
{
    "id": 1,
    "name": "Mancala",
    "status": "FINISHED",
  "gameMatrix": [
    [0, 0, 0, 0, 0, 0, 20],
    [0, 0, 0, 0, 0, 0, 52]
  ],
    "winnerPlayerIndex": 1,
    "players": [
        {"name": "Hana"},
        {"name": "Mona"}
    ]
}
```
- "id": 1: Indicates the unique identifier of the game.
- "name": "Mancala": Specifies the name of the game as "Mancala."
- "status": "FINISHED": Indicates that the game has finished.
- "gameMatrix": Represents the state of the game board with two rows, each corresponding to a player's pits. The last element in each row represents the Mancala (large pit) for each player.
- "winnerPlayerIndex": 1: Specifies that the player with index 1 (Mona) is the winner.
- "players": Contains an array of player objects, each with a "name" property. In this case, there are two players - "Hana" and "Mona."

### Cancel a Game

- **Endpoint**: DELETE /mancala/{gameId}
- **Example**: DELETE /mancala/5
- **Response**: Success message if the game is canceled


## Configuration

### Database Configuration
The application uses MySQL as the database. 
Configure database connection details in the application.
properties file.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/game?useSSL=false
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=gameuser
spring.datasource.password=nK9RxfZ%

```

### Mancala Rules Configuration
Mancala rules and settings can be configured in the application.properties file, including the default number of player pits,
stones per pit, and other game-specific configurations.