bomberman
=========

Bomberman UDP client/server game written in Java

Code
===

## Usage

*Recommended JAVA version 1.7.x*
We have two Eclipse projects under the following directories:

Server: `code/Server`

The server listens for UDP packets (datagrams) on port 5000.

Client: `code/Client`

The client communicates with the server using UDP datagrams.

To run, import Client and Server as separate projects in Eclipse. Right-click each project and click Properties.
Adjust the Java Build Path to include `lib/JSON.jar` as a jar.

### Running the server

Run the main in ServerMain.java

### Running GUI Client

Run the main in ClientMain.java (after running the server)

### Running the Tests

Add TestFramework in `/code` folder as a new project. Add the other projects (Server and Client) as part of the TestFramework project by right clicking the project in the sidebar --> Properties --> Java Build Path --> Projects Tab and add both projects. Then in the Libraries tab, add **JUnit 4**.

Then just run the project as JUnit tests. If any tests fail, file an issue please :)

## License

MIT
