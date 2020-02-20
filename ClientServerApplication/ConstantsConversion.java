
public class ConstantsConversion {
	//Game options
	public final static int GRID_SIZE = 10;
	
    public final static byte UNEXPLORED = 0;
    public final static byte MISSED = -6;
    
    public final static byte CARRIER_SIZE = 5;
    public final static byte BATTLESHIP_SIZE = 4;
    public final static byte CRUISER_SIZE = 3;
    public final static byte SUBMARINE_SIZE = 3;
    public final static byte DESTROYER_SIZE = 2;
    
    		// If you modify these 5 ID constants be sure they remain of different values
    public final static byte CARRIER_ID = 5;
    public final static byte BATTLESHIP_ID = 4;
    public final static byte CRUISER_ID = 3;
    public final static byte SUBMARINE_ID = 2;
    public final static byte DESTROYER_ID = 1;
    
    public final static byte MAX_TRIES = 5;
    
    //Message options
    public final static byte HEADER_SIZE = 2;
    public final static byte TILE_SIZE = 2;
    
    public final static byte PROTOCOL_VERSION = 1;
    
    public final static byte NEW_GAME_REQUEST = 0;
    public final static byte SHOT_REQUEST = 1;
    public final static byte STATE_REQUEST = 2;
    
    public final static byte NEW_GAME_RESPONSE = 1;
    public final static byte SHOT_RESPONSE= 2;
    public final static byte STATE_RESPONSE = 3;
    public final static byte ERROR_RESPONSE = 4;
    
    //Connection Options
    public final static int PORT = 2601;
    
    //Conversion Tools
	public static byte convertRowCharToInt(char tile) {
		switch(tile) {
			case 'A':
				return 0;
			case 'B':
				return 1;
			case 'C':
				return 2;
			case 'D':
				return 3;
			case 'E':
				return 4;
			case 'F':
				return 5;
			case 'G':
				return 6;
			case 'H':
				return 7;
			case 'I':
				return 8;
			case 'J':
				return 9;
			default:
				return -1;
		}
	}
	
	public static String convertRowIntToChar(byte tile) {
		switch(tile) {
			case 0:
				return "A";
			case 1:
				return "B";
			case 2:
				return "C";
			case 3:
				return "D";
			case 4:
				return "E";
			case 5:
				return "F";
			case 6:
				return "G";
			case 7:
				return "H";
			case 8:
				return "I";
			case 9:
				return "J";
			default:
				return "";
		}
	}
	
	public static String defineBoat(byte ID){
		switch(ID) {
			case DESTROYER_ID:
			case -DESTROYER_ID:
				return "destroyer";
			case SUBMARINE_ID:
			case -SUBMARINE_ID:
				return "submarine";
			case CRUISER_ID:
			case -CRUISER_ID:
				return "cruiser";
			case BATTLESHIP_ID: 
			case -BATTLESHIP_ID:
				return "battleship";
			case CARRIER_ID:
			case -CARRIER_ID:
				return "carrier";
			default:
				return "ERROR";
		}
	}
	
	public static byte setSize(byte ID) {
		switch(ID){
		case DESTROYER_ID:
			return DESTROYER_SIZE;
		case SUBMARINE_ID:
			return SUBMARINE_SIZE;
		case CRUISER_ID:
			return CRUISER_SIZE;
		case BATTLESHIP_ID:
			return BATTLESHIP_SIZE;
		case CARRIER_ID:
			return CARRIER_SIZE;
		default:
			return -1;
		}
	}
}
