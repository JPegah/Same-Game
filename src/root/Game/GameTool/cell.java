package root.Game.GameTool;

/**
 * cells of the game
 * @author Pegah Jandaghi(90105978)
 *
 */
public class cell {
	private int color;


	//constructor
	public cell (int color){
		this.color = color;
	}
	
	//check if the given cell has the same color 
	public boolean isSame(cell Cell){
		if (this.color == Cell.color)
			return true;
		return false;
	}
	
	//get the color of the cell
	public int getColor(){
		return this.color;
	}
}
	