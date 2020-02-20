import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPRequest{

	//Set this value to true to diplay boat location and message (TESTING)
	private boolean DEBUG = true;

	private int messageLength=0, xCoordinate=-1, yCoordinate=-1;
	private byte target;

	private String method, version, currentLine, pageToGenerate;
	private BufferedReader buffer;
	private Cookie userCookie;
	private Grid game;
	private URL url;

	private ArrayList<String> requestHeader = new ArrayList<String>();
	public ConstantsConversion.CODE returnStatus = null;

	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public HTTPRequest(InputStreamReader in) {

		buffer = new BufferedReader(in);
		String statusLine = null;

		try{
			statusLine = buffer.readLine();

			String[] statusLineSplit = statusLine.split(" ");
			if(statusLineSplit.length != 3){
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return;
			}else{
				if(!checkStatusLine(statusLineSplit)){
					return;
				}
			}

			if(DEBUG){
				System.out.println("-----REQUEST-----");
				System.out.println("STATUS LINE: ");
				System.out.println(Arrays.toString(statusLineSplit));
				System.out.println();
			}

			currentLine = buffer.readLine();
			for(; buffer.ready() && !currentLine.isEmpty(); currentLine = buffer.readLine()){
				requestHeader.add(currentLine);
			}

			if(DEBUG){
				System.out.println("HEADER: ");
				System.out.println(requestHeader.toString());
				System.out.println();
			}

			if(!initializeCookie()){
				// We don't have any cookie information in the received message. We need to create one.
				userCookie = new Cookie();
			}

			// If the client was already played we associate him with it's game (in webServer.actifGame).
			// If it wasn't, we create a new game for him and we save it in webServer.actifGame.
			if(!alreadyPlay()){
				game = new Grid(userCookie);
				game.gridInitializer();
				if(DEBUG){
					game.displayGrid();
				}
				webServer.actifGame.add(game);
			}

			// If there is no query in the URL or no 'Content-length' field in the header then the client
			// doesn't want anything more than it's page
			if((url.getQuery() == null) && (!messageLength())){
				returnStatus = ConstantsConversion.CODE.OK;
				return;
			}

			// Get arguments (shot coordinates) by POST or GET methods
			if(method.equals("POST")){
				if(!setArgumentsPOST()){
					return;
				}
			}else if(method.equals("GET") && (url.getQuery() != null)){
				if(!setArgumentsGET()){
					return;
				}
			}

			if(DEBUG){
				System.out.println("Method: " + method);
				System.out.println("Url: " + url.toString());
				System.out.println("Version: " + version);
				System.out.println("Message Length: " + messageLength);
				System.out.println("Coordinates: (" + xCoordinate + ";" + yCoordinate + ")");
				System.out.println();
				System.out.println();
				System.out.println();
			}

			// Update the grid
			if((xCoordinate != -1) && (yCoordinate != -1)){
				target = game.shotHandler(yCoordinate, xCoordinate);
			}else{
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return;
			}

			returnStatus = ConstantsConversion.CODE.OK;
			return;
		}catch(Exception e){
			returnStatus = ConstantsConversion.CODE.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
			System.exit(0);
		}
	}

	private boolean checkStatusLine(String[] statusLineSplit){
		/*
		 * USE OF THE METHOD:
		 *  Check the validity of each arguments from the Status request line and then initialize them to
		 *  there values in this object:
		 *  		METHOD USED = method;
		 *  		URL FROM SENDER = url;
		 *  		HTTP VERSION = version;
		 * Arguments:
		 *  /
		 * Returns:
		 *  -true		If arguments where valid and initialisation work fine.
		 *  -false 		Otherwise and initialise returnStatus to the corresponding throwable error value.
		 *  				The differents error codes can be seen @ConstantsConversion.CODE.
		 */

		// METHOD check
		switch(statusLineSplit[0]){
			case "GET":
				method = statusLineSplit[0];
				break;
			case "POST":
				method = statusLineSplit[0];
				break;
			case "OPTIONS":
			case "HEAD":
			case "PUT":
			case "DELETE":
			case "TRACE":
			case "CONNECT":
				returnStatus = ConstantsConversion.CODE.METHOD_NOT_ALLOWED;
				return false;
			default:
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return false;
		}
		// URL check
		try {
			url = new URL(webServer.BASE_URL + statusLineSplit[1]);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			returnStatus = ConstantsConversion.CODE.NOT_FOUND;
			return false;
		}

		try{
			// Verify the validity of url path content
			switch(url.getPath()){
				case "/":
				case "\\":
					// Redirection from root
					if(url.getQuery() == null){
						URL tmpUrl = new URL(webServer.BASE_URL + "/play.html");
						url = tmpUrl;
					}else{
						URL tmpUrl = new URL(webServer.BASE_URL + "/play.html?" + url.getQuery());
						url = tmpUrl;
					}
					break;
				case "/play.html":
				case "\\play.html":
					break;
				case "/halloffame.html":
				case "\\halloffame.html":
					break;
				default:
					returnStatus = ConstantsConversion.CODE.NOT_FOUND;
					return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			returnStatus = ConstantsConversion.CODE.NOT_FOUND;
			return false;
		}
		// HTTP VERSION check
		if(!(version = statusLineSplit[2]).equals("HTTP/1.1")){
			returnStatus = ConstantsConversion.CODE.HTTP_VERSION_NOT_SUPPORTED;
			return false;
		}
		return true;
	}

	private boolean initializeCookie(){
		/*
		 * USE OF THE METHOD:
		 *  Initialize the client COOKIE. We choose to work with the cookie that we can find in the header
		 *  after the field "Cookie". By using that, the server is able to recognize the client even after
		 *  he close its session. If there is no Cookie in the browser request we create a new one;
		 * Arguments:
		 *  /
		 * Returns:
		 *  -true		If the initialisation work fine
		 *  -false 		Otherwise
		 */
		String[] headerField;
		for(int headerLenght = 0; headerLenght<requestHeader.size(); headerLenght++){
			headerField = (requestHeader.get(headerLenght)).split(": ");

			if(headerField[0].equals(ConstantsConversion.HEADER_FIELD.COOKIE.getName())) {
				try{
					userCookie = new Cookie(headerField[1]);
					return true;
				}catch(Exception e){
					returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	private boolean messageLength(){
		/*
		 * USE OF THE METHOD:
		 *  Initialize the lenght of a POST type request by using the header file 'Content-Length'
		 * Arguments:
		 *  /
		 * Returns:
		 *  -true		If the initialisation work fine
		 *  -false 		Otherwise
		 */
		String[] headerField;
		for(int headerLenght = 0; headerLenght<requestHeader.size(); headerLenght++){
			headerField = (requestHeader.get(headerLenght)).split(": ");
			if(headerField[0].equals(ConstantsConversion.HEADER_FIELD.CONTENT_LENGTH.getName())){
				try{
					messageLength=Integer.parseInt(headerField[1]);
					return true;
				}catch(Exception e){
					returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
					e.printStackTrace();
					return false;
				}
			}
		}
		returnStatus = ConstantsConversion.CODE.LENGTH_REQUIRED;
		return false;
	}

	public boolean alreadyPlay(){
		/*
		 * USE OF THE METHOD:
		 *  Check whether or not a client has already a game in progress
		 *  /
		 * Returns:
		 *  -true		If it has one
		 *  -false 		Otherwise
		 */
		for(int i=0;i<webServer.actifGame.size();i++){
			if((webServer.actifGame.get(i).getCookie().getValue()).equals(userCookie.getValue())){
				game = webServer.actifGame.get(i);
				return true;
			}
		}
		return false;
	}

	/*
	 * GETTER AND SETTER
	 */
	private boolean setArgumentsPOST(){
		char[] message = new char[messageLength];
		if(messageLength != 7){
			returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
			return false;
		}else{
			try{
				buffer.read(message, 0, messageLength);
			}catch(Exception e){
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				e.printStackTrace();
				return false;
			}
		}
		try{
			// Check message validity, FORMT DEFINED: y=yCoordinate&x=xCoordinate
			if((Character.getNumericValue(message[6])>=0) && (Character.getNumericValue(message[6])<10)){
				xCoordinate = Character.getNumericValue(message[6]);
			}else{
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return false;
			}
			if((Character.getNumericValue(message[2])>=0) && (Character.getNumericValue(message[2])<10)){
				yCoordinate = Character.getNumericValue(message[2]);
			}else{
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
			return false;
		}
		return true;
	}

	private boolean setArgumentsGET(){
		String urlData = url.getQuery();
		String[] urlDataSplit = urlData.split("[&]");

		try{
			if((Integer.parseInt(urlDataSplit[0])>=0) && (Integer.parseInt(urlDataSplit[0])<10)){
				xCoordinate = Integer.parseInt(urlDataSplit[0]);
			}else{
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return false;
			}
			if((Integer.parseInt(urlDataSplit[0])>=0) && (Integer.parseInt(urlDataSplit[0])<10)){
				yCoordinate = Integer.parseInt(urlDataSplit[1]);
			}else{
				returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			returnStatus = ConstantsConversion.CODE.BAD_REQUEST;
			return false;
		}
		return true;
	}

	public int getyCoordinate(){
		return yCoordinate;
	}

	public int getxCoordinate(){
		return xCoordinate;
	}

	public Grid getGame(){
		return game;
	}

	public ConstantsConversion.CODE getError(){
		return returnStatus;
	}

	public URL getUrl(){
		return url;
	}

	public String getRequestedPage(){
		return pageToGenerate;
	}

	public String getVersion(){
		return version;
	}

	public String getMethod(){
		return method;
	}

	public byte getTarget(){
		return target;
	}
}
