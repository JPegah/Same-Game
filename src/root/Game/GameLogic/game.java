package root.Game.GameLogic;


import java.util.ArrayList;
import root.Game.GameTool.*;
import root.Graphic.graphic;

/**
 * rules and logic of the game
 * @author Pegah Jandaghi(90105978)
 *
 */
public class game {
	private graphic graphicBoard;
	private board board;
	private final static int[][] neighbors = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
	private int numberOfColors;
	private String player;
	private int score = 0;
	private int row;
	private int col;

	// constructor
	public game() {
		graphicBoard = new graphic(this);
		int cols = graphicBoard.getCol();
		int row = graphicBoard.getRow();
		this.row = row;
		this.col = cols;
		this.numberOfColors = 4;
		this.board = new board(cols, row, this.numberOfColors);
		graphicBoard.createBoard(row, cols);
	}

	// get the cells with the same color
	public ArrayList<ArrayList<Integer>> getPossibleChoices(int x, int y) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		neighbors(x, y, result);
		return result;
	}

	// get the same neighbors of the cell
	private void neighbors(int x, int y, ArrayList<ArrayList<Integer>> cells) {
		cell myCell = this.board.getCells().get(x).get(y);
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(x);
		temp.add(y);

		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 4; i++) {
			int X = x + this.neighbors[i][0];
			int Y = y + this.neighbors[i][1];
			if (board.isInside(X, Y)
					&& myCell.isSame(this.board.getCells().get(X).get(Y))) {
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				tmp.add(X);
				tmp.add(Y);
				if (cells.contains(tmp))
					continue;
				cells.add(tmp);
				res.add(tmp);
			}
		}

		if (!cells.contains(temp))
			cells.add(temp);

		for (int i = 0; i < res.size(); i++)
			neighbors(res.get(i).get(0), res.get(i).get(1), cells);
	}

	// get the name of the player
	public String getPlayerName() {
		return this.player;
	}

	// remove cells with the same color
	public void remove(ArrayList<ArrayList<Integer>> removed) {
		int score = 0;
		while (removed.size() > 0) {
			ArrayList<Integer> c = removed.get(0);
			int ind = findMax(removed, c.get(0), c.get(1), 0);
			int x = removed.get(ind).get(0);
			int y = removed.get(ind).get(1);
			board.getCells().get(x).remove(y);
			removed.remove(ind);
			score++;
		}
		this.score += score * score;
	}

	// find the max y in a column
	public int findMax(ArrayList<ArrayList<Integer>> arrayList, int x, int y, int index) {
		int res = index;
		int tmpMax = y;
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).get(1) > tmpMax) {
				tmpMax = arrayList.get(i).get(1);
				res = i;
			}
		}
		return res;
	}

	// check if there is no way but to destroy one cell
	public boolean check() {
		for (int i = 0; i < board.getCells().size(); i++) {
			for (int j = 0; j < board.getCells().get(i).size(); j++) {
				ArrayList<ArrayList<Integer>> tmp = new ArrayList<ArrayList<Integer>>();
				this.neighbors(i, j, tmp);
				if (tmp.size() > 1)
					return false;
			}
		}
		return true;
	}

	// get the score of the player
	public int getScore() {
		return this.score;
	}

	// check if the game is finished or not
	public boolean isFinished() {
		for (int i = 0; i < board.getCells().size(); i++) {
			if (this.board.getCells().get(i).size() != 0)
				return false;
		}
		return true;
	}

	// reset the game
	public void reset() {
		this.board.reset();
		this.score = 0;
	}

	// create a new board
	public void newBoard(int row, int col) {
		this.row = row;
		this.col = col;
		board.newBoard(numberOfColors, col, row);
		this.score = 0;
	}

	// change the number of colors
	public void changeColors() {
		this.numberOfColors = graphicBoard.getColors();;
		newBoard(row, col);
	}

	// get the board of the game
	public ArrayList<ArrayList<cell>> getBoard() {
		return this.board.getCells();
	}

	// set the player's name
	public void setName(String a) {
		this.player = a;
	}
}
