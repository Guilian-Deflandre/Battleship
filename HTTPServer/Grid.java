import java.util.Random;


public class Grid{
	/*
	 * USE OF THE CLASS:
	 * 	Class who will build the plate game of the Battleship. This is a 10x10 board where 5 boats are rand-
	 *  omly placed.
	 */
	private Cookie userCookie;
	private byte[][] grid;
	private Boat Carrier;
	private Boat Battleship;
	private Boat Cruiser;
	private Boat Submarine;
	private Boat Destroyer;
	private int nbTouch = 0;
	private int nbTries = 0;
	
	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public Grid(Cookie cookieValue){
		grid = new byte[ConstantsConversion.GRID_SIZE][ConstantsConversion.GRID_SIZE];
		userCookie = cookieValue;
	}
	
	public void gridInitializer(){
		/*
		 * USE OF THE METHOD:
		 *  -Initialize the five boats of the game (as define by rules);
		 *  -Place the five initialized boats randomly on the Battleship grid avoiding collisions;
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		Carrier = new Boat(ConstantsConversion.CARRIER_ID,initOrientation(), (byte)-1, (byte)-1);
		Battleship = new Boat(ConstantsConversion.BATTLESHIP_ID,initOrientation(), (byte)-1, (byte)-1);
		Cruiser = new Boat(ConstantsConversion.CRUISER_ID,initOrientation(), (byte)-1, (byte)-1);
		Submarine = new Boat(ConstantsConversion.SUBMARINE_ID,initOrientation(), (byte)-1, (byte)-1);
		Destroyer = new Boat(ConstantsConversion.DESTROYER_ID,initOrientation(), (byte)-1, (byte)-1);
		
		placeBoat(Carrier);
		placeBoat(Battleship);
		placeBoat(Cruiser);
		placeBoat(Submarine);
		placeBoat(Destroyer);
		
	}
	
	private byte initRow(Boat boat){
		/*
		 * USE OF THE METHOD:
		 *  Initialize a boat row.
		 * Arguments:
		 * 	/
		 * Return:
		 * 	-A random value to place the boat head in a row in the grid
		 */
		if(boat.getOrientation()){
			Random rand = new Random();
		    return (byte)rand.nextInt(10 - boat.getSize());
		}else{
			Random rand = new Random();
		    return (byte)rand.nextInt(10);
		}
	}
	
	private byte initColumn(Boat boat){
		/*
		 * USE OF THE METHOD:
		 *  Initialize a boat column.
		 * Arguments:
		 * 	/
		 * Return:
		 * 	-A random value to place the boat head in a column in the grid
		 */
		if(boat.getOrientation()){
			Random rand = new Random();
		    return (byte)rand.nextInt(10);
		}else{
			Random rand = new Random();
		    return (byte)rand.nextInt(10 - boat.getSize());
		}
	}
	
	private boolean initOrientation(){
		/*
		 * USE OF THE METHOD:
		 *  Initialize a boat orientation.
		 * Arguments:
		 * 	/
		 * Return:
		 * 	-A random value to set the boat orientation in the grid
		 */
		Random rand = new Random();
	    return rand.nextBoolean();
	    
	}
	
	private void placeBoat(Boat boat){
		/*
		 * USE OF THE METHOD:
		 *  Place a boat on the grid game, this function work in 2 phases:
		 * 		-Check if it's possible to place the boat given its head coordinates helping by checkCollis-
		 *       ions();
		 *   	-Place the boat on the grid (place its ID in the targeted cells);
		 * Arguments:
		 * 	-boat		The boat that we want to place.
		 * Returns:
		 *  /
		 */
		do{
			boat.setColumn(initColumn(boat));
			boat.setRow(initRow(boat));
		}while(collisionsCheck(boat));
			
		for(int i=0; i<boat.getSize(); i++){
			if(boat.getOrientation() == true){
				grid[boat.getRow()+i][boat.getColumn()] = boat.getID();
			}else{
				grid[boat.getRow()][boat.getColumn()+i] = boat.getID();
			}
		}
	}
	
	private boolean collisionsCheck(Boat boat){
		/*
		 * USE OF THE METHOD:
		 *  Check whether or not there is a collision between a boat alredy on the grid and the given boat 
		 *  that we want to place.
		 * Arguments:
		 * 	-boat		The boat that we want to place.
		 * Returns:
		 * 	-true: 		If there is a collision.
		 * 	-false: 		Otherwise.
		 */
		if(boat.orientation) {
			for(int i=0; i<boat.getSize(); i++){
				if(grid[boat.getRow()+i][boat.getColumn()] != 0) {
					return true;
				}
			}
		}else{
			for(int i=0; i<boat.getSize(); i++){
				if(grid[boat.getRow()][boat.getColumn()+i] != 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void displayGrid(){
		/*
		 * USE OF THE METHOD:
		 * 	Display the current grid game state.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  /
		 */
		System.out.println("\n");
		System.out.printf("   ");
		for(byte j=0; j<ConstantsConversion.GRID_SIZE; j++) {
			System.out.printf("%3d", j);
		}
		
		System.out.println("");
		for(byte j=0; j<ConstantsConversion.GRID_SIZE+1; j++) {
			System.out.printf("---");
		}
		
		System.out.println("");
		for(byte i=0; i<ConstantsConversion.GRID_SIZE; i++) {
			System.out.printf("%s |", ConstantsConversion.convertRowIntToChar(i));
			for(byte j=0; j<ConstantsConversion.GRID_SIZE; j++) {
				System.out.printf("%3d", grid[i][j]);
			}
			System.out.println("");
		}
	}
	
	public byte shotHandler(int row, int column){
		/*
		 * USE OF THE METHOD:
		 *  Update the grid game in function of the tile targeted by the user and give information obout what
		 *  type of shot it was.
		 * Arguments:
		 * 	-row			The row of the targeted grid place
		 *  -column		The column of the targeted grid place
		 * Returns:
		 *  -target		The value of the targeted place. This value will be useful to display the right mes-
		 *  				sage at the user (see ConstantsConversion.java for more detail about its possible 
		 *  				values)
		 */
		nbTries++;
		byte target = grid[row][column];
		
		if(grid[row][column] > 0 && grid[row][column] < 6){
			grid[row][column] = (byte)-grid[row][column];
			nbTouch++;
		}else if(grid[row][column] == 0){
			grid[row][column] = ConstantsConversion.MISSED;
		}
		return target;
	}
	
	public String toHTML(){
		/*
		 * USE OF THE METHOD:
		 *  Convert the current grid game int its HTML code in order to facilitate the user utilisation.
		 * Arguments:
		 * 	/
		 * Returns:
		 *  HTMLGrid.toString():  String containing the requested HTML piece of code.
		 */
		StringBuilder HTMLGrid = new StringBuilder(1000);
		for(byte i=0; i<ConstantsConversion.GRID_SIZE;i++){
			HTMLGrid.append("		<tr>\n");
			HTMLGrid.append("			<th class=\"border\">"+ ConstantsConversion.convertRowIntToChar(i) + "</th>\n");
			for(byte j=0; j<ConstantsConversion.GRID_SIZE;j++){
				if(grid[i][j]==ConstantsConversion.MISSED){
					HTMLGrid.append("			<td class=\"missedCell\"><button></button></td>\n");
				}else if(grid[i][j]>ConstantsConversion.MISSED&&grid[i][j]<ConstantsConversion.UNEXPLORED){
					HTMLGrid.append("			<td class=\"touchedCell\"><button></button></td>\n");
				}else{
					HTMLGrid.append("			<td class=\"unknowCell\"><button></button></td>\n");
				}
			}
			HTMLGrid.append("		</tr>\n");
		}
		return HTMLGrid.toString();
	}

	/*
	 * GETTER AND SETTER
	 */
	public Cookie getCookie(){
		return userCookie;
	}
	
	public int getNbTouch(){
		return nbTouch;
	}
	
	public int getNbTries(){
		return nbTries;
	}
}










