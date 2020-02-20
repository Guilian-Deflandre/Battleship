import java.io.*;
import java.net.*;

public class Battleship extends Thread{
	/*
	 * USE OF THE CLASS:
	 *  The Battleship class is the "worker" of BattleshipServer class, it's this class that will communicate 
	 *  with BattleshipClient class to manage the evolution of the naval battle part of the client clientID.
	 */
	private static OutputStream out;
	private static InputStream in;
	private static final boolean DEBUG = true;
	private Socket socket;
	private int clientID;
	
	private static byte receiver[] = new byte[ConstantsConversion.HEADER_SIZE+ConstantsConversion.TILE_SIZE];
	private static byte sender[] = new byte[ConstantsConversion.HEADER_SIZE+ConstantsConversion.TILE_SIZE];
	
	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public Battleship(Socket socket, int clientID) {
		super();
		this.socket = socket;
		this.clientID = clientID;
		
		try{
			this.socket.setSoTimeout(20000);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			Grid game = new Grid();;
			
			// This Thread will live until it will be interrupt
			while(!this.isInterrupted()){ 
				// Read client request
				read();
				
				// Check the protocol ID
				if(receiver[0] != ConstantsConversion.PROTOCOL_VERSION) {
					System.err.println("Wrong protocol version!");
					System.exit(0);
				}else{
					// If we have the good potocol ID, we treat the user request 
					switch(receiver[1]){
						case ConstantsConversion.NEW_GAME_REQUEST:
							game = new Grid();
							game.gridInitializer();
							
							if(DEBUG){
								System.out.printf("Client %d grid:\n", clientID);
								game.displayGrid();
							}
						
							sender[0] = ConstantsConversion.PROTOCOL_VERSION;
							sender[1] = ConstantsConversion.NEW_GAME_RESPONSE;
							
							write(sender);
							
							break;
						case ConstantsConversion.SHOT_REQUEST:
							if(DEBUG){
								System.out.printf("\nClient %d choice: %d %d\n", clientID, receiver[2], 
																							  receiver[3]);
							}
							
							sender[0] = ConstantsConversion.PROTOCOL_VERSION;
							sender[1] = ConstantsConversion.SHOT_RESPONSE;
							sender[2] = game.shotHandler(receiver[2], receiver[3]);
							
							write(sender);
							
							if(DEBUG){
								System.out.printf("Client %d grid:\n", clientID);
								game.displayGrid();
							}
							
							break;
						case ConstantsConversion.STATE_REQUEST:
							game.sendGameState(out);
							break;
						default:
							sender[0] = ConstantsConversion.PROTOCOL_VERSION;
							sender[1] = ConstantsConversion.ERROR_RESPONSE;
							
							write(sender);
							
							break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	private void write(byte sender[]) throws IOException{
		/*
		 * USE OF THE METHOD:
		 *  Write the array sender to the Output Stream out.
		 * Arguments:
		 * 	sender		The buffer of byte who countain the message to send.
		 * Returns:
		 *  /
		 */
		try{
			out.write(sender);
			out.flush();
		}catch(Exception e){
			this.socket.close();
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	
	private void read() throws IOException{
		/*
		 * USE OF THE METHOD:
		 *  Read data in the Input Stream in and place them in the receiver array.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		try{
			in.read(receiver);
		}catch(Exception e){
			this.socket.close();
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}