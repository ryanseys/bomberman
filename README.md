bomberman
=========

Bomberman game designed for SYSC 3303.

Collaboration
===
Documents for this project will be stored on a shared
[Google Drive folder](https://drive.google.com/a/ryanseys.com/folderview?id=0B-HAHCc4sghKT3lOaEtNazRuSE0&usp=sharing).


Code
===
We have two Eclipse projects under the following directories:

Client: `code/Client`

Server: `code/Server`

JSON
===
We decided to use JSON as our communication protocol.
In order to use JSON with Java we needed to include a
library, in this case [org.json](http://www.json.org/java/index.html).

The jar file with the included classes for it are in [project]/lib/JSON.jar.
You may need to include the jar file in your build path (right click on it >
  build path > add to build path).

Client -> Server: Message Specification
===
Json Message

|Key|Value|Description|
|---|-----|---|
|"pid"|int *(optional)*|Unique id of the client that sent the message *(Not necessary for register)*|
|"command"|Command|What command the player just entered|
|"button"|Button *(optional)*|What non direction button the client just pressed|
|"direction"| Direction *(optional)*| Direction that the player just moved|
|"bomb"|Bomb *(optional)*| Location that the player just dropped the bomb|
|"client_info"|ClientInfo *(optional)*|Has the connection information for the client so that we can send them messages|


|Command *(String)*|Description|Outcome|
|--------|---|---|
|"join"|Registers client for the game|Returns player ID on success, -1 on failure|
|"move"|Indicates the player just moved, expect "direction" in JSON object as well|Update players position|
|"button"|Indicates the player just pressed button (other than direction)| Calls relevant button handler|



|Button *(String)*|Description|Outcome|
|---|---|---|
|"start"|Starts game| Sets game state to started|
|"end"|Ends the game| Sets game to ended|
|"reset"|Resets the player|Changes the players position|
|"deploy"|Indicates that a bomb was just dropped|Adds bomb to current game state|

|Direction *(String)*|
|---|
|"up"|
|"down"|
|"left"|
|"right"|

|Bomb *(Object)*|key|value|
|---|---|---|
||"x"|int|
||"y"|int|

|ClientInfo *(Object)*|key|value|
|---|---|---|
||"ip"|String|
||"port"|int|


Server -> Client: Message Specification
===
Json Message

|Key|Value|Description|
|---|---|---|
|"type"|String|Indicates the type of message|
|"game" *(optional)*|GameBoard|An object containing the current state of the game board|


|type *(String)*|Description|
|---|---|
|"broadcast"|This message was broadcast to all players, indicting there is also a GameBoard object in the message.

|GameBoard *(object)*|Key|Value|Description|
|---|-----|---|---|
||width|int|width of the board|
||height|int|height of the board|
||board|[(GameObjectType)int][(GameObjectType)int]|A two dimensional integer array of GameObjectType defined in GameObjectType enum|


Notes
===
#####From the project description:
- The sending of the periodic update must proceed concurrently with ongoingprocessing of player controls. The game state must be double-buffered.


####Double Buffered
