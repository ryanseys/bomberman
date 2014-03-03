List of testcases to test server functionality:
===
---

##All integer representations on game are as follows:


|int|Type|
|---|---|
|0|EMPTY|
|1|PLAYER_1|
|2|PLAYER_2|
|3|PLAYER_3|
|4|PLAYER_4|
|5|ENEMY|
|6|BOMB|
|7|DOOR|
|8|BOX|
|9|POWERUP|
*Defined in GameObjectType.java*

##Joining a game
###Test1: Single client joining:
Input:

```json
{"command":"join", "type":"player"}
```

Expected Response:
```json
{"pid":1,"type":"player_join","resp":"Success"}
```

###Test2: Two clients joining:

```json
{"command":"join", "type":"player"}
{"command":"join", "type":"player"}

```

Expected Response:
```json
{"pid":1,"type":"player_join","resp":"Success"}
```
```json
{"pid":2,"type":"player_join","resp":"Success"}
```

###Test3: Three clients joining (More than the limit):
Input:
```json
{"command":"join", "type":"player"}
{"command":"join", "type":"player"}
```

Expected Response:
```json
{"pid":1,"type":"player_join","resp":"Success"}
```
```json
{"pid":2,"type":"player_join","resp":"Success"}
```
```json
{"type":"player_join","resp":"Failure"}
```

###Test4: Spectators joining the game, interlaced with players, last player fails because there are already two:
```json
{"command":"join", "type":"spectator"}
{"command":"join", "type":"player"}
{"command":"join", "type":"spectator"}
{"command":"join", "type":"player"}
{"command":"join", "type":"spectator"}
{"command":"join", "type":"player"}
```

Expected Response:
```json
{"type":"spectator_join","resp":"Success"}
```
```json
{"pid":1,"type":"player_join","resp":"Success"}
```
```json
{"type":"spectator_join","resp":"Success"}
```
```json
{"pid":2,"type":"player_join","resp":"Success"}
```
```json
{"type":"spectator_join","resp":"Success"}
```
```json
{"type":"player_join","resp":"Failure"}
```

##Start a game
###Test1: Start game while game has not been started, one player:
Input:
```json
{"command":"join", "type":"player"}
{"command":"button", "pid": 1, "button":"start"}
```
Expected Response (broadcast of the new game state):
```json
{"pid":1,"type":"player_join","resp":"Success"}
{"game":{"height":10,"width":10,
 "board":[[0,8,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,7,0,0,0,0,0,0,0,0],
          [0,0,0,8,0,0,0,0,0,0],
          [8,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,8,8,0],
          [0,9,0,0,0,0,0,0,0,0],
          [0,0,1,0,0,0,0,0,0,0]]},"type":"broadcast"}
```
###Test1: Start game while game has not been started, two players:
Input:
```json
{"command":"join", "type":"player"}
{"command":"join", "type":"player"}
{"command":"button", "pid": 1, "button":"start"}
```

Server Response:
```json
Client Started
{"pid":1,"type":"player_join","resp":"Success"}
{"pid":1,"type":"player_join","resp":"Success"}
{"game":{"height":10,"width":10,
 "board":[[0,8,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,7,0,0,0,0,0,0,0,0],
          [0,0,0,8,0,0,0,0,0,0],
          [8,0,0,0,0,2,0,0,0,0],
          [0,0,0,0,0,0,0,8,8,0],
          [0,9,0,0,0,0,0,0,0,0],
          [0,0,1,0,0,0,0,0,0,0]]},"type":"broadcast"}

```


##Load a board
###Test1: Pass in a board from Client to Server

```json
{"command":"join", "type":"player"}
{"game":{"height":10,"width":10,"board":[[9,0,0,0,0,0,0,0,1,0],[0,0,0,0,0,0,0,0,0,0],[8,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,8,0,0],[0,0,0,0,0,0,0,0,8,8],[0,0,0,0,0,8,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,7],[0,0,0,0,0,0,0,0,0,0]]},"command":"load"}
{"command":"button", "pid": 1, "button":"start"}
```

Server Response:
```
Client Started
{"pid":1,"type":"player_join","resp":"Success"}
{"type":"response","resp":"Success"}
{"game":{"height":10,"width":10,
 "board":[[9,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [8,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,0],
          [0,0,0,0,0,0,0,8,0,0],
          [0,0,0,0,0,0,0,0,8,8],
          [0,0,0,0,0,8,0,0,0,0],
          [0,0,0,0,1,0,0,0,0,0],
          [0,0,0,0,0,0,0,0,0,7],
          [0,0,0,0,0,0,0,0,0,0]]},"type":"broadcast"}

```

##Moving a player
###Test1: Move up
```json
{"command":"join", "type":"player"}
{"command":"button", "pid": 1, "button":"start"}
{"command":"move", "pid": 1, "direction":"up"}
{"command":"move", "pid": 1, "direction":"down"}
{"command":"move", "pid": 1, "direction":"left"}
{"command":"move", "pid": 1, "direction":"right"}
```
Resuling board from start:
```
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][9][0][8][8][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][8][0][0][8][0][0][0]
[0][1][0][0][0][0][0][0][7][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][8][0][0][0][0]
```
Board after move up:
```
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][9][0][8][8][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][8][0][0][8][0][0][0]
[0][0][1][0][0][0][0][0][7][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][8][0][0][0][0]
```
Board after move down:
```
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][9][0][8][8][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][8][0][0][8][0][0][0]
[0][1][0][0][0][0][0][0][7][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][8][0][0][0][0]
```
Board after move left:
```
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][9][0][8][8][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][1][0][8][0][0][8][0][0][0]
[0][0][0][0][0][0][0][0][7][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][8][0][0][0][0]
```
Board after move right:
```
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][9][0][8][8][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][8][0][0][8][0][0][0]
[0][1][0][0][0][0][0][0][7][0]
[0][0][0][0][0][0][0][0][0][0]
[0][0][0][0][0][8][0][0][0][0]
```
