import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BattleshipClient {
	/*
	 * USE OF THE CLASS:
	 *  The BattleshipClient class will implement the user interface of the game BattleShip. The game itself 
	 *  will be handle by a distant host, this class will be able to communicate with it and dislay essancial
	 *  information about the game flow.
	 */
	private static Scanner sc;
	
	private static Socket socket;
	private static OutputStream out;
	private static InputStream in;
	
	private static String choice;
	private static int nbTries;
	private static byte boatState[];
	
	private static byte receiver[] = new byte[ConstantsConversion.HEADER_SIZE+
	                                          (ConstantsConversion.GRID_SIZE*ConstantsConversion.GRID_SIZE)];
	private static byte sender[] = new byte[ConstantsConversion.HEADER_SIZE+ConstantsConversion.TILE_SIZE];

	public static void main(String[] args) {
		
		try {
			connection();
			do{
				startGame();
				sc = new Scanner(System.in);
				
				do{
					System.out.println("\n1) Try a tile\n2) See game status\n3) Quit\n");
					System.out.printf("Your choice: ");
					
					choice = sc.nextLine();
					
					if(choice.equals("1")){
						tryATile();
						
						if((nbTries == ConstantsConversion.MAX_TRIES) && !sunkCheck()){
							System.out.println("Maximum value of tries reach without sinking any boat.");
							if(quitGame()){
								break;
							}
						}
						if(winCheck()){
							System.out.println("YOU WIN, EVERY BOAT HAS BEEN SUNK!");
							if(quitGame()){
								break;
							}else{
								System.out.println("Oups, the function quitgame() encountered a trouble!");
								System.exit(0);
							}
							
						}
					}else if(choice.equals("2")){
						seeGameStatut();
					}else if(choice.equals("3") && quitGame()){
						break;
					}else{
						System.out.printf("Bad request. Please choose between:\n");
					}
				}while(true);
			}while(true);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void connection() {
		/*
		 * USE OF THE METHOD:
		 *  Connect properly a client to a server. 
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		try {
			socket = new Socket("localhost", ConstantsConversion.PORT);
			socket.setSoTimeout(10000);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void write(byte sender[]) throws IOException{
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
		}catch(IOException e){
			socket.close();
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void read(){
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
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void startGame(){
		/*
		 * USE OF THE METHOD:
		 *  This function is able to request a new game to the server (first client message type) and to ver-
		 *  ify the server response to this request. It will also initialize the client variables essencial 
		 *  for the smooth running of the game (boatState[] and nbTries).
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		sender[0] = ConstantsConversion.PROTOCOL_VERSION;
		sender[1] = ConstantsConversion.NEW_GAME_REQUEST;
		
		try{
			
			write(sender);
			
			read();
			
			if(receiver[0] != ConstantsConversion.PROTOCOL_VERSION){
				System.err.println("Wrong protocol version!");
				System.exit(0);
			}else{
				if(receiver[1] == ConstantsConversion.NEW_GAME_RESPONSE){
					boatState = new byte[5];
					nbTries = 0;
					System.out.println("A new game start.");
				}else{
					System.err.println("Wrong message response! something wrong append!");
					System.exit(0);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void tryATile(){
		/*
		 * USE OF THE METHOD:
		 *  This function is able to send to the server that the client want to try a shot in the 
		 *  grid (second client message type) and the tile targeted by this shot. It also interpret the ser-
		 *  ver response (second server message type) in order to inform the user what type of shot it was 
		 *  (miss or hit).
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		sender[0] = ConstantsConversion.PROTOCOL_VERSION;
		sender[1] = ConstantsConversion.SHOT_REQUEST;
		
		do{
			System.out.printf("Enter your guess: ");
			choice = sc.nextLine();
			
			if(choice.length() != 2 || Integer.parseInt(choice.substring(1)) < 0 || 
						Integer.parseInt(choice.substring(1)) > 9 ||
							       ConstantsConversion.convertRowCharToInt(choice.charAt(0)) < 0 || 
										     ConstantsConversion.convertRowCharToInt(choice.charAt(0)) > 9){
				System.out.printf("%s is not a valid tile. Please choose between A0 and J9\n", choice);
				continue;
			}else{
				break;
			}
		}while(true);
		
		sender[2] = (byte)ConstantsConversion.convertRowCharToInt(choice.charAt(0));
		sender[3] = (byte)Integer.parseInt(choice.substring(1));
		try{
			
			write(sender);
			
			read();
			
			if(receiver[0] != ConstantsConversion.PROTOCOL_VERSION){
				System.err.println("Wrong protocol version!");
				System.exit(0);
			}
			if(receiver[1] != ConstantsConversion.SHOT_RESPONSE){
				System.err.println("Wrong response message!");
				System.exit(0);
			}
			if(receiver[2]==0){
				System.out.println("Splash, this is a miss shot.");
				nbTries++;
			}else if((receiver[2]>0) && (receiver[2]<6)){
				System.out.printf("You hit the %s\n", ConstantsConversion.defineBoat(receiver[2]));
				touchCounter(receiver[2]);
				nbTries++;
			}else if(receiver[2]==ConstantsConversion.MISSED){
				System.out.println("This place was already mark as missed. Try an other location!");
			}else if((receiver[2]<0) && (receiver[1]>ConstantsConversion.MISSED)){
				System.out.println("This place was already mark as touch. Try an other location!");
			}else{
				System.out.println("Something bad append! Restart the game");
				return;
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void seeGameStatut(){
		/*
		 * USE OF THE METHOD:
		 *  This function is able to request the game statut by sending its message type (third) and inter-
		 *  pret the third type of message from the server, namely the status of the game grid by using the
		 *  sender[] and receiver[] Output/Input-Stream buffer.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		sender[0] = ConstantsConversion.PROTOCOL_VERSION;
		sender[1] = ConstantsConversion.STATE_REQUEST;
		
		try{
			write(sender);
			
			read();
			
			for(byte i=ConstantsConversion.HEADER_SIZE; i < receiver.length; i++){
				if(receiver[i] < 0 && receiver[i] > -6) {
					System.out.println(ConstantsConversion.convertRowIntToChar(
						   (byte)((i-ConstantsConversion.HEADER_SIZE)/ConstantsConversion.GRID_SIZE)) +
							      (i-ConstantsConversion.HEADER_SIZE) % ConstantsConversion.GRID_SIZE + ": "
								        						  + ConstantsConversion.defineBoat(receiver[i]) 
																		   + " has been marked as touch.");
				}else if(receiver[i] == -6){
					System.out.println(ConstantsConversion.convertRowIntToChar(
						(byte)((i-ConstantsConversion.HEADER_SIZE)/ConstantsConversion.GRID_SIZE)) + 
										 (i-ConstantsConversion.HEADER_SIZE)%ConstantsConversion.GRID_SIZE + 
																			 ": has been marked as miss.");
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	private static boolean quitGame(){
		/*
		 * USE OF THE METHOD:
		 *  Allows the client to leave the game or to restart a game if he wishes. Once this method called,
		 *  the previous progress is lost.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  -true: 		If the client want to start a new game (return in the main function)
		 * 	-false: 		If the function meet any problem. Indeed, this function should return true or ended 
		 * 				the current execution. Anyway, the compiler force us to have a return value so we 
		 * 				use it in terms of defensive programming
		 */
		try{
			do{
				System.out.println("Would you like to start a new game? (Y/N)");
				choice = sc.nextLine();
				
				switch(choice) {
					case "Y":
						choice = "3"; //Essencial for the condition evaluation
						return true;
					case "N":
						socket.close();
						System.exit(0);
					default:
						System.out.printf("Bad request. Please choose between:\n");
						break;
				}	
			}while(true);
		}catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	private static void touchCounter(int boatID){
		/*
		 * USE OF THE METHOD:
		 *  Update the histogram of boat's strokes.
		 * Arguments:
		 * 	boatID		The ID of the touched boat.
		 * Returns:
		 *  /
		 */
		switch(boatID){
			case ConstantsConversion.DESTROYER_ID:
				boatState[0]++;
				break;
			case ConstantsConversion.SUBMARINE_ID:
				boatState[1]++;
				break;
			case ConstantsConversion.CRUISER_ID:
				boatState[2]++;
				break;
			case ConstantsConversion.BATTLESHIP_ID:
				boatState[3]++;
				break;
			case ConstantsConversion.CARRIER_ID:
				boatState[4]++;
				break;
			default:
				break;
		}
	}

	private static boolean sunkCheck(){
		/*
		 * USE OF THE METHOD:
		 *  Predicate used to know if one of the boats was sunk.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  -true: 		If a boat has been already sunk.
		 * 	-false: 		Otherwise.
		 */
		return ((boatState[0] == ConstantsConversion.DESTROYER_SIZE) ||
				        (boatState[1] == ConstantsConversion.SUBMARINE_SIZE) ||
				                   (boatState[2] == ConstantsConversion.CRUISER_SIZE) ||
				                               (boatState[3] == ConstantsConversion.BATTLESHIP_SIZE) ||
				                                         (boatState[4] == ConstantsConversion.CARRIER_SIZE));
	}
	
	private static boolean winCheck(){
		/*
		 * USE OF THE METHOD:
		 *  Predicate used to know if all of the boats was sunk.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  -true: 		If all boats are sunk.
		 * 	-false: 		Otherwise.
		 */
		return ((boatState[0] == ConstantsConversion.DESTROYER_SIZE) &&
				        (boatState[1] == ConstantsConversion.SUBMARINE_SIZE) &&
				                   (boatState[2] == ConstantsConversion.CRUISER_SIZE) &&
				                               (boatState[3] == ConstantsConversion.BATTLESHIP_SIZE) &&
				                                         (boatState[4] == ConstantsConversion.CARRIER_SIZE));
	}
}

