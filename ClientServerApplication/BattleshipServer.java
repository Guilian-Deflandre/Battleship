import java.io.*;
import java.net.*;


public class BattleshipServer {
	/*
	 * USE OF THE CLASS:
	 *  The BattleshipServer class allows the connection of a client. This connection will then launch a Bat-
	 *  tleship thread, allowing each client to have his own game independent of any other current. This 
	 *  class also associates each connected client with a clientID, passed to each thread in order to ease 
	 *  the displaying of the game state in the server side.
	 */
	private static ServerSocket socketServer;
	private static int clientID = 1;
	
	public static void main(String[] args){
		try{
			socketServer = new ServerSocket(ConstantsConversion.PORT);
			
			while(true) {
				Socket socket = socketServer.accept();
				new Battleship(socket, clientID).start();
				clientID++;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}



