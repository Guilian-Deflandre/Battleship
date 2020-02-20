import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Battleship extends Thread{
	/*
	 * USE OF THE CLASS:
	 *  The Battleship class is the "worker" of webServer class, it's this class that will communicate
	 *  with te browser used by the the client to manage its evolution of its battle game.
	 */
	private static final boolean DEBUG = true;
	private static OutputStream outStream;
	private static InputStream in;
	private static PrintWriter out;
	private Socket socket;
	private static String requestedContent, requestedHeader;

	private static HTTPRequest request;
	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public Battleship(Socket socket) {
		super();
		this.socket = socket;

		try{
			this.socket.setSoTimeout(ConstantsConversion.TIMEOUT);

		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {

		try{
			in = socket.getInputStream();
            outStream = socket.getOutputStream();
            out = new PrintWriter(outStream);
		}catch(Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
			return;
		}
		// Get request;
		request = new HTTPRequest(new InputStreamReader(in));
		// Generate the valid response
		if(request.returnStatus == ConstantsConversion.CODE.OK){

			out.print(generateResponseStatusLine(request));
			out.flush();

			if(request.getUrl().getQuery() == null){
				//Not argument for any page, we just want the requested url path or a response to a POST
				requestedContent = (new HTMLGenerator(request.getGame(), request.getUrl())).HTMLPage();
				requestedHeader = generateResponseHeader1(requestedContent);

				// Handle the POST request method win (javascript is unactive).
				if(request.getMethod().equals("POST") && request.getGame().getNbTouch() >=
																		ConstantsConversion.MAX_NB_TOUCH){
					webServer.hallOfFameManager(new Fame(request.getGame().getCookie().toString(),
																		 request.getGame().getNbTries()));
					webServer.actifGame.remove(request.getGame());

					try{
						requestedContent = (new HTMLGenerator(request.getGame(),
										   (new URL(webServer.BASE_URL + "/halloffame.html")))).HTMLPage();
						out.print(generateResponseHeader2(requestedContent));
						out.flush();
						out.print(requestedContent);
						out.flush();
						Thread.currentThread().isInterrupted();
						return;
					}catch(MalformedURLException e){
						requestedContent = (new HTMLErrorGenerator(
											   ConstantsConversion.CODE.INTERNAL_SERVER_ERROR).HTMLPage());
						out.print(generateResponseHeader2(requestedContent));
						out.flush();
						out.print(requestedContent);
						out.flush();
						e.printStackTrace();
						Thread.currentThread().isInterrupted();
						return;
					}
				}
				// Handle the POST request method lose (javascript is unactive).
				if(request.getMethod().equals("POST") && request.getGame().getNbTries() >=
																		ConstantsConversion.MAX_NB_TRIES){
					webServer.actifGame.remove(request.getGame());

					requestedContent = (new HTMLGenerator(request.getGame(), request.getUrl())).HTMLPage();
					requestedHeader = generateResponseHeader1(requestedContent);

					out.print(generateResponseHeader2(requestedContent));
					out.flush();
					out.print(requestedContent);
					out.flush();
					Thread.currentThread().isInterrupted();
					return;
				}

			}else if(request.getUrl().getPath().equals("/play.html")){

				// If the query is not empty we have to ensure that this request is made from play.html.
				// After what we can respond to the proper message (Boat ID, GAME_WIN or GAME LOST)
				if(request.getGame().getNbTouch() >= ConstantsConversion.MAX_NB_TOUCH){
					webServer.hallOfFameManager(new Fame(request.getGame().getCookie().toString(),
																		 request.getGame().getNbTries()));
					webServer.actifGame.remove(request.getGame());
					requestedContent = Integer.toString(ConstantsConversion.GAME_WIN_VALUE);
					requestedHeader = generateResponseHeader3(requestedContent);
				}else if(request.getGame().getNbTries() >= ConstantsConversion.MAX_NB_TRIES){
					webServer.actifGame.remove(request.getGame());
					requestedContent = Integer.toString(ConstantsConversion.GAME_LOST_VALUE);
					requestedHeader = generateResponseHeader3(requestedContent);
				}else{
					requestedContent = Integer.toString(request.getTarget());
					requestedHeader = generateResponseHeader2(requestedContent);
				}
			}else{
				// The request is obviously made by url to another place than play.html => NOT FOUND
				requestedContent = (new HTMLErrorGenerator(ConstantsConversion.CODE.NOT_FOUND)).HTMLPage();
				requestedHeader = generateResponseHeader2(requestedContent);
			}

			if(DEBUG){
				System.out.println("-----RESPONSE-----");
				System.out.println(generateResponseStatusLine(request));
				System.out.println(requestedHeader);
				System.out.println(requestedContent);
				System.out.println();
				System.out.println();
			}

			out.print(requestedHeader);
			out.flush();
			out.print(requestedContent);
			out.flush();

		// Ask for more informations because we were enable to read the message
		}else if(request.returnStatus == ConstantsConversion.CODE.SEE_OTHER){
			out.print(generateResponseStatusLine(request));
			out.flush();
			out.print(generateRedirectionHeader(request));
			out.flush();

		// Generate the error response
		}else{
			out.print(generateResponseStatusLine(request));
			out.flush();
			
			requestedContent = (new HTMLErrorGenerator(request.returnStatus)).HTMLPage();
			requestedHeader = generateResponseHeader2(requestedContent);

			out.print(requestedHeader);
			out.flush();
			out.print(requestedContent);
			out.flush();
		}
		Thread.currentThread().isInterrupted();
		return;
	}

	private String generateResponseStatusLine(HTTPRequest receivedMessage){
		/*
		 * USE OF THE METHOD:
		 *  Generate the status line of the HTTP response to send
		 * Arguments:
		 * 	-receivedMessage		The HTTPRequest send by the client that will motivating the server response
		 * Returns:
		 *  						A String containing all status line arguments
		 */
		return "HTTP/" + ConstantsConversion.VERSION + " " + receivedMessage.returnStatus.toString() + "\r\n";
	}

	private String generateResponseHeader1(String requestedContent){
		/*
		 * USE OF THE METHOD:
		 *  Generate the header of the HTTP response to send with the complete page and set a Cookie
		 * Arguments:
		 * 	-requestedContent	The String containing the body of the message in order to set the Content-
		 * 						Lenght header field
		 * Returns:
		 *  	-(INDEFINED)			A String containing all header arguments
		 */
		StringBuilder header = new StringBuilder();
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_TYPE.toString() + "text/html\r\n");

        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        now.add(Calendar.MINUTE, 30);

		header.append(ConstantsConversion.HEADER_FIELD.SET_COOKIE.toString()
									+ request.getGame().getCookie().getValue() + "; " + "Path=/; Expires="
												  + dateFormat.format(now.getTime()) + "; HttpOnly \r\n");
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_LENGTH.toString()
										  + Integer.toString(requestedContent.length()) + "\r\n" + "\r\n");
		return header.toString();
	}

	private String generateResponseHeader2(String requestedContent){
		/*
		 * USE OF THE METHOD:
		 *  Generate the header of the HTTP response to send with to POST and GET shoot result request and
		 *  also in case of error page
		 * Arguments:
		 * 	-requestedContent	The String containing the body of the message in order to set the Content-
		 * 						Lenght header field
		 * Returns:
		 *  	-(INDEFINED)			A String containing all header arguments
		 */
		StringBuilder header = new StringBuilder();
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_TYPE.toString() + "text/html\r\n");
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_LENGTH.toString()
										  + Integer.toString(requestedContent.length()) + "\r\n" + "\r\n");
		return header.toString();
	}

	private String generateResponseHeader3(String requestedContent){
		/*
		 * USE OF THE METHOD:
		 *  Generate the header of the winning game page and unset the Cookie of the winner.
		 * Arguments:
		 * 	-requestedContent	The String containing the body of the message in order to set the Content-
		 * 						Lenght header field
		 * Returns:
		 *  	-(INDEFINED)			A String containing all header arguments
		 */
		StringBuilder header = new StringBuilder();
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_TYPE.toString() + "text/html\r\n");
		header.append(ConstantsConversion.HEADER_FIELD.SET_COOKIE.toString()
				  + "SESSID=deleted; Path=/; Expires=Thu, 01 Jan 1970 00:00:00\n" +
				  "GMT" + "\r\n");
		header.append(ConstantsConversion.HEADER_FIELD.CONTENT_LENGTH.toString()
										  + Integer.toString(requestedContent.length()) + "\r\n" + "\r\n");
		return header.toString();
	}

	private String generateRedirectionHeader(HTTPRequest receivedMessage){
		/*
		 * USE OF THE METHOD:
		 *  Generate the header of the HTTP response to send in case of redirecting
		 * Arguments:
		 * 	-receivedMessage		The HTTPRequest send by the client that will motivating the server response
		 * Returns:
		 *  	-(INDEFINED)			A String containing all header arguments
		 */
		return ConstantsConversion.HEADER_FIELD.LOCATION + receivedMessage.getUrl().toString();
	}

}
