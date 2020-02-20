import java.util.*;

public class Cookie {
	/*
	 * USE OF THE CLASS:
	 *  Implement a clear and simple vision of the cookie using during the Battleship game. We chose to use 
	 *  the header cookie for our implementation.
	 */
	private String cookieValue;
	
	public Cookie(String cookieValue){
		this.cookieValue = cookieValue;
	}
	
	public Cookie(){
		this.cookieValue = generateCookie();
	}

	public String getValue() {
		return cookieValue;
	}

	public void setValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	@Override
	public String toString() {
		return "Cookie: " + cookieValue;
	}
	
	public String generateCookie() {
		Random r = new Random();
        int n = r.nextInt(100000);
        return "SESSID=" + Integer.toString(n);
	}
	
}