var dgram = require("dgram");
var client = dgram.createSocket("udp4");
var port = process.env.BOMBERMAN_PORT || 5000;
var isGameOn = false;
var gameOver = false;
var playerid = 0;
var powerups;
var bombs;
var lives = 1;
var game;

window.addEventListener('keydown', function (e) {
  if (e.keyCode === 38) {
    // up arrow
    move("up");
  }
  else if(e.keyCode === 37) {
    //left arrow
    move("left");
  }
  else if(e.keyCode === 39) {
    // right arrow
    move("right");
  }
  else if(e.keyCode === 40) {
    // down arrow
    move("down");
  }
  else if(e.keyCode === 32) {
    // space bar
    deployBomb();
  }
  document.getElementById('connectStartButton').blur();
}, false);

client.on("message", function (msg, rinfo) {
  setState(msg);
  render();
});

function connect() {
  if(playerid === 0) {
    // not connected yet so connect
    document.getElementById('connectStartButton').innerHTML = "Connecting...";

    if(document.getElementById('radioplayer').checked) {
      sendMessage({ command: "join", type: "player" });
    }
    else {
      sendMessage({ command: "join", type: "spectator" });
    }
  }
  else if(!isGameOn) {
    isGameOn = true;
    sendMessage({command: "button", button: "start", pid: playerid });
  }
  else {
    endGame();
  }
}

function sendMessage(msgObj) {
  var msgBuf = new Buffer(JSON.stringify(msgObj));
  client.send(msgBuf, 0, msgBuf.length, 5000, "localhost");
}

function resetServer() {
  sendMessage({ command: "reset"});
  document.getElementById('connectStartButton').disabled = false;
  playerid = 0;
  gameOver = false;
  isGameOn = false;
  game = null;
  document.getElementById('connectStartButton').innerHTML = "Connect";
  render();
}

function newGame() {
  gameOver = false;
  startGame();
}

function endGame() {
  if(isGameOn) {
    sendMessage({command: "button", button: "end", pid: playerid });
  }
}

function setState(msgStr) {
  var msg = JSON.parse(msgStr);
  if(msg.type == 'game_over') {
    game = null;
    isGameOn = false;
    gameOver = true;
  }
  else if(msg.type === 'player_join' && msg.resp === 'Success') {
    playerid = msg.pid;

  }
  else if(msg.type === 'spectator_join' && msg.resp === 'Success') {
    playerid = -1;

  }
  if(msg.game) {
    isGameOn = true;
    game = msg.game;
  }
}

function move(direction) {
  sendMessage({command: "move", direction: direction, pid: playerid });
}

function getGameBoardTypeLetter(type) {
  switch(type) {
    case 6:
      return "B";
    case 10:
      return "F";
    default:
      return type;
  }
}

function deployBomb() {
  sendMessage({ command: "button", button: "deploy", pid: playerid });
}

function getBoardString() {
  var result = "";
  var board;
  if(game) {
    board = game.board;
  }
  if(board) {
    for (var col = 0; col < board.length; col++) {
      for (var row = 0; row < board.length; row++) {
        result += "[" + getGameBoardTypeLetter(board[row][col]) + "]";
      }
      result += "<br>";
    }
  }
  return result;
}

function render() {
  if((playerid > 0) && !isGameOn && !gameOver) {
    document.title = "Bomberman - Player " + playerid;
    document.getElementById('connectStartButton').innerHTML = "Start Game";
    document.getElementById('gameoverStuff').style.display = "none";
  }
  else if((playerid < 0) && !isGameOn && !gameOver) {
    document.title = "Bomberman - Spectator ";
    document.getElementById('connectStartButton').disabled = true;
    document.getElementById('gameoverStuff').style.display = "none";
  }
  else if(playerid > 0 && isGameOn) {
    if(lives === 0) {
      document.title = "Bomberman - Player " + playerid + " - Dead";
      // document.getElementById('gameoverStuff').style.display = "block";
    }
    else {
      document.getElementById('radioplayer').disabled = true;
      document.getElementById('radiospec').disabled = true;
      document.getElementById('startStuff').style.display = "none";
      document.getElementById('gameoverStuff').style.display = "none";
      document.getElementById('connectStartButton').innerHTML = "End Game";
      document.title = "Bomberman - Player " + playerid + " - In Game";
    }
  }
  else if(playerid > 0 && gameOver) {
    document.getElementById('connectStartButton').disabled = false;
    document.getElementById('connectStartButton').innerHTML = "New Game";
    document.getElementById('radioplayer').disabled = false;
    document.getElementById('radiospec').disabled = false;
    document.getElementById('gameoverStuff').style.display = "block";
    document.title = "Bomberman - Player " + playerid + " - Game Over";
  }
  else if(playerid < 0 && gameOver) {
    document.getElementById('connectStartButton').disabled = false;
    document.getElementById('connectStartButton').innerHTML = "New Game";
    document.getElementById('radioplayer').disabled = false;
    document.getElementById('radiospec').disabled = false;
    document.getElementById('gameoverStuff').style.display = "block";
    document.title = "Bomberman - Game Over";
  }
  document.getElementById("board").innerHTML = getBoardString();
}
