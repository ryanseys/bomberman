
/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var http = require('http');
var path = require('path');
var dgram = require('dgram'); // http://nodejs.org/api/dgram.html

var ws = require('ws');
var WebSocketServer = ws.Server;

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/users', user.list);

var server = http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});

var wss = new WebSocketServer({
  server: server
});


// WebSocket Server Handlers!!!
client_id = 0;
wss.on('connection', function(ws) {
  var id;
  id = client_id++;
  console.log("Client Connect[" + new Date() + "]: " + id);



  // Define ws handlers here...
  return ws.on('close', function() {
    return console.log("Client Disconnect[" + new Date() + "]: " + id);
  });
  return ws.on('up', function() {
    return console.log("UP[" + new Date() + "]: " + id);
  });
  return ws.on('down', function() {
    return console.log("DOWN[" + new Date() + "]: " + id);
  });
  return ws.on('left', function() {
    return console.log("LEFT[" + new Date() + "]: " + id);
  });
  return ws.on('right', function() {
    return console.log("RIGHT[" + new Date() + "]: " + id);
  });
  return ws.on('deploy', function() {
    return console.log("DEPLOY[" + new Date() + "]: " + id);
  });
  return ws.on('start', function() {
    return console.log("START[" + new Date() + "]: " + id);
  });
  return ws.on('end', function() {
    return console.log("END[" + new Date() + "]: " + id);
  });

});

// Broadcast message to all clients
wss.broadcast = function(data) {
  var i, _results;
  _results = [];
  for (i in this.clients) {
    _results.push(this.clients[i].send(data, function(err) {
      if (err) {
        return console.log(err);
      }
    }));
  }
  return _results;
};
