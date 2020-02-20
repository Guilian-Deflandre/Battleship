
public class Boat {
	/*
	 * USE OF THE CLASS:
	 *  A boat is an object intended to be placed on a grid to implement the Battleship game.
	 *   -Each boat will be refereced by its ID, there are define by the ConstantsConversion class;
	 *   -Each boat as an orientation on the grid game, it is defined as a boolean who takes value:
	 * 	 	 -true: the boat orientation is vertical;
	 *  		 -false: the boat orientation is horizontal;
	 *   -Each boat as a size (number of contiguous box ocuppied), define in the ConstantsConversion class;
	 *   -Each boat as a couple (row, column) being the coordinates of the first box which composes it;
	 */
	protected byte ID;
	protected byte size;
	protected byte row;
	protected byte column;
	protected boolean orientation;
	
	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public Boat(byte ID, boolean orientation, byte row, byte column) {
		this.ID = ID;
		this.row = row;
		this.column = column;
		this.orientation = orientation;
		this.size = ConstantsConversion.setSize(ID); 
	}
	
	/*
	 * GETTER AND SETTER
	 */
	public byte getSize(){
		return size;
	}
	
	public boolean getOrientation(){
		return orientation;
	}
	
	public byte getRow(){
		return row;
	}
	
	public byte getColumn(){
		return column;
	}
	
	public byte getID(){
		return ID;
	}
	
	public void setRow(byte row){
		this.row = row;
	}
	
	public void setColumn(byte column){
		this.column = column;
	}
	
}
