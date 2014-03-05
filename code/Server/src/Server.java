import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
	private static final int port = 5000;

	public static void main(String[] args) throws Exception {
		
		// Instantiate objects
		DatagramSocket serverSocket = new DatagramSocket(port);
		ServerSender sender = new ServerSender(serverSocket);
		MessageQueue messages = new MessageQueue();
		Controller controller = new Controller(sender, messages);
		byte[] recData = new byte[2048];
		
		// Check for command line args
		if(args.length > 0){
			if(args.length != 2){
				System.out.println("Invalid use of command line arguments");
				System.out.println("Enter game flag, -g, followed by file to load");
				System.out.println("Use: \"$ Java Server.java -g gameBoard.json\"");
			}
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-g")){
					System.out.println("Game board flag");
					if(!args[++i].endsWith(".json")){
						System.out.println("Trying to load: " + args[i]);
						System.out.println("Invalid file format loaded");
					}
					else{
						File f = new File(args[i]);
						if(f.exists() && !f.isDirectory()){
							BufferedReader br = new BufferedReader(new FileReader(args[i]));
							String currLine;
							String gameBoard = "";
							while((currLine = br.readLine()) != null){
								gameBoard += currLine; 
							}
							messages.add((new DatagramPacket(gameBoard.getBytes(), gameBoard.getBytes().length)));
							System.out.println("Loaded game board located at: " + args[i]);
							br.close();
						}
						else{
							System.out.println("File does not exist!");
						}
					}
				}
			}
		}
		
		System.out.println("Server started listening on port: " + port);

		while(true){
			if(!controller.isAlive()){
				System.out.println("Starting controller thread");
				controller.start();
			}
			DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
			serverSocket.receive(recPacket);
			System.out.println("Server received a packet!");
			messages.add(recPacket);
		}
		//serverSocket.close();
	}
}
