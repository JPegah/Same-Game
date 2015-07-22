package root.Game.GameTool;

import java.util.ArrayList;
import java.util.Random;

/**
 * consists of cells
 * @author Pegah Jandaghi(90105978)
 *
 */
public class board {
	private int[][] firstFilled;
	private ArrayList<ArrayList<cell>> cells;
	
	//constructor
	public board(int col, int row, int numOfColors){
		Random random = new Random();
		cells = new ArrayList<ArrayList<cell>>();
		firstFilled = new int[col][];
		for (int i = 0; i < col; i++) {
			firstFilled[i] = new int[row];
			cells.add(new ArrayList<cell>());
			for (int j = 0; j < row; j++) {
				int k = random.nextInt(numOfColors);
				cells.get(i).add(new cell(k));
				firstFilled[i][j] = k;
			}
		}
	}
	

	// check if the cell with the give position exists
	public boolean isInside(int x, int y) {
		if (x < 0)
			return false;
		if (x >= this.cells.size())
			return false;
		if (y < 0)
			return false;
		if (y >= this.cells.get(x).size())
			return false;
		return true;
	}

	//return the cells
	public  ArrayList<ArrayList<cell>> getCells(){
		return this.cells;
	}
	

	//reset the game
	public void reset(){
		cells = new ArrayList<ArrayList<cell>>();
		for (int i = 0; i < firstFilled.length; i++){
			cells.add(new ArrayList<cell>());
			for (int j = 0; j < firstFilled[i].length; j++){
				cells.get(i).add(new cell(firstFilled[i][j]));
			}
		}
	}
	
	//create a new board
	public void newBoard(int numberOfColors, int col, int row){
		cells = new ArrayList<ArrayList<cell>>();
		firstFilled = new int[col][];
		for (int i = 0; i < col; i++){
			cells.add(new ArrayList<cell>());
			Random r = new Random();
			firstFilled[i] = new int[row];
			for (int j = 0; j < row; j++){
				firstFilled[i][j] = r.nextInt(numberOfColors);
				cells.get(i).add(new cell(firstFilled[i][j]));
			}
		}
	}
}