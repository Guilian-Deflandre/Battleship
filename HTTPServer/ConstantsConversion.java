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
    public final static int MAX_NB_TOUCH = CARRIER_SIZE + BATTLESHIP_SIZE + CRUISER_SIZE + SUBMARINE_SIZE +
    																						   DESTROYER_SIZE;
    public final static int MAX_NB_TRIES = 70;
    
    	// If you modify these 5 ID constants be sure they remain of different values
    public final static byte CARRIER_ID = 5;
    public final static byte BATTLESHIP_ID = 4;
    public final static byte CRUISER_ID = 3;
    public final static byte SUBMARINE_ID = 2;
    public final static byte DESTROYER_ID = 1;
    
    public final static byte MAX_TRIES = 5;
    
    // Message options
    // If you modify these constants be sure they remain of different values and that client code is update
    public final static byte HEADER_MAX_SIZE = 100; // In line
    public final static byte GAME_WIN_VALUE = 10;
    public final static byte GAME_LOST_VALUE = 11;
    
    // Connection Options
    public final static int PORT = 8028;
    public final static int TIMEOUT = 3600000; //ms
    public final static int MAX_THREADS = 20;
    public final static String PROTOCOL = "HTTP";
    public final static String PROTOCOL_URL = "http";
    public final static String HOST = "localhost:";
    public final static String VERSION = "1.1";
    
    
    // Request status int code <-> String type
    enum CODE{
        OK(200, "OK"),
        SEE_OTHER(303, "See Other"),//Redirection
        BAD_REQUEST(400, "Bad Request"),//Client Error
        NOT_FOUND(404, "Not Found"),//Resource not available
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        LENGTH_REQUIRED(411, "Length Required"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented"),
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    		REQUEST_TIMEOUT(408, "Request Timeout");
    	
        private final int code;
        private final String status;
        /*CONSTRUCTOR*/
        CODE(int c, String s) {
            this.code = c;
            this.status = s;
        }
        @Override
        public String toString() {
            return Integer.toString(code) + " " + status;
        }
    }
    
  // All possible header content
    enum HEADER_FIELD{
    	ACCEPT("Accept"),
        ACCEPT_CHARSET("Accept-Charset"),
        ACCEPT_ENCODING("Accept-Encoding"),
        ACCEPT_LANGUAGE("Accept-Language"),
        ACCEPT_RANGES("Accept-ranges"),
        AGE("Age"),
        ALLOW("Allow"),
        AUTHORIZATION("Authorization"),
        CACHE_CONTROL("Cache-Control"),
        CONNECTION("Connection"),
        CONTENT_ENCODING("Content-Encoding"),
        CONTENT_LANGUAGE("Content-Language"),
        CONTENT_LENGTH("Content-Length"),
        CONTENT_LOCATION("Content-Location"),
        CONTENT_MD5("Content-MD5"),
        CONTENT_RANGE("Content-Range"),
        CONTENT_TYPE("Content-Type"),
        COOKIE("Cookie"),
        DATE("Date"),
        DNT("DNT"),
        ETAG("ETag"),
        EXPECT("Expect"),
        EXPIRES("Expires"),
        FROM("From"),
        HOST("Host"),
        IF_MATCH("If-Match"),
        IF_MODIFIED_SINCE("If-Modified-Since"),
        IF_NONE_MATCH("If-None-Match"),
        IF_RANGE("If-Range"),
        IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
        LAST_MODIFIED("Last-Modified"),
        LOCATION("Location"),
        MAX_FORWARDS("Max-Forwards"),
        ORIGIN("Origin"),
        PRAGMA("Pragma"),
        PROXY_AUTHENTICATE("Proxy-Authenticate"),
        PROXY_AUTHORIZATION("Proxy-Authorization"),
        RANGE("Range"),
        REFERER("Referer"),
        RETRY_AFTER("Retry-After"),
        SERVER("Server"),
        SET_COOKIE("Set-Cookie"),
        TE("TE"),
        TRAILER("Trailer"),
        TRANSFER_ENCODING("Transfer-Encoding"),
        UPGRADE("Upgrade"),
        UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
        USER_AGENT("User-Agent"),
        VARY("Vary"),
        VIA("Via"),
        WARNING("Warning"),
        WWW_AUTHENTICATE("WWW-Authenticate");
    	
	private final String name;
		
        /*CONSTRUCTOR*/
        HEADER_FIELD(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name + ": ";
        }
        
        public String getName() {
        		return name;
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
