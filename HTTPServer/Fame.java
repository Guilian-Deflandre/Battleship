
public class Fame{
	/*
	 * USE OF THE CLASS:
	 *  Define a Fame by it's cookie value and its game final score in order to place him or not in the hall
	 *  of fame of the Battleship game.
	 */
	private String cookieValue;
	private int score;
	
	public Fame(String cookieValue, int score){
		this.cookieValue = cookieValue;
		this.score = score;
	}

	public String getName() {
		return cookieValue;
	}

	public void setName(String cookieValue) {
		this.cookieValue = cookieValue;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}