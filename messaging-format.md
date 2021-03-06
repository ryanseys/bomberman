## Messaging Format (JSON)

We decided to use JSON as our server/client data-interchange notation.
In order to use JSON with Java we needed to include a
library, in this case [org.json](http://www.json.org/java/index.html).

The jar file with the included classes for it are included in both projects under [project]/lib/JSON.jar. You will need to include the jar file in your build path. In Eclipse, expand the lib/ directory, right click on the jar file > Build Path > Add to Build Path.

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
|"game" *(optional)*|GameBoard|An object containing the state of a board to be loaded|
|"type" *(optional)*|String|Will indicate whether the joining client is a Player or Spectator|


|Command *(String)*|Description|Outcome|
|--------|---|---|
|"join"|Registers client for the game|Returns player ID on success, -1 on failure|
|"move"|Indicates the player just moved, expect "direction" in JSON object as well|Update players position|
|"button"|Indicates the player just pressed button (other than direction)| Calls relevant button handler|
|"load"|Indicates that we want to load a board state|Parses passed in JSONArray of board and stores the data in board for when the game gets started|



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

|type *(String)*|
|---|
|"player"|
|"spectator"|

|Bomb *(Object)*|key|value|
|---|---|---|
||"x"|int|
||"y"|int|


Server -> Client: Message Specification
===
Json Message

|Key|Value|Description|
|---|---|---|
|"type"|String|Indicates the type of message|
|"resp" *(optional)*|String|This will be either "Success" or "Failure", indicating the Servers response to their message|
|"game" *(optional)*|GameBoard|An object containing the current state of the game board|
|"pid" *(optional)*|int|Integer indicating the player id of for the client, will be -1 on failure.|


|type *(String)*|Description|
|---|---|
|"player_join"|This message will only be sent when a client tries to join as a Player. Response will include resp parameter which will indicate "Success" or "Failure", "Success" (ful) joins will include parameter pid which indicates their player id|
|"spectator_join"|This message will only be sent when a client tries to join as a Spectator. Response will include resp parameter which will indicate "Success" or "Failure"|
|"response"|This is a message for a single client that indicates an aditional parameter, "resp", which will indicate the success or failure of the request|
|"broadcast"|This message was broadcast to all players, indicting there is also a GameBoard object in the message.|
|"game_over"|Indicates the game is over, will be accompanied by ending game state|

|GameBoard *(object)*|Key|Value|Description|
|---|-----|---|---|
||"width"|int|width of the board|
||"height"|int|height of the board|
||board|[(GameObjectType)int][(GameObjectType)int]|A two dimensional integer array of GameObjectType defined in GameObjectType enum|

If one of the clients finishes the game (e.g. exits a door or collides with the last player on the board), the Server will not respond
with an updated board state, but will instead broadcast a `{“type":"game_over"}` message to all players alerting them that the game is over.
