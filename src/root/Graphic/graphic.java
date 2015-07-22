package root.Graphic;
import root.Game.GameLogic.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import root.Graphic.MyComponent.MyLabel;
import root.Graphic.MyComponent.myButton;

import java.util.ArrayList;
/**
 * the graohical same game
 * @author Pegah Jandaghi(90105978)
 *
 */
public class graphic {
	final static int[][] colorPattern = { { 192, 30, 30 }, { 192, 192, 30 },
			{ 30, 192, 30 }, { 30, 192, 192 }, { 30, 30, 192 },
			{ 192, 30, 192 } };
	private JFrame gameFrame;
	private game game;
	final private JPanel score;
	private ArrayList<ArrayList<myButton>> cell;
	private JLayeredPane cellLayer;
	private boolean changeHighlight;
	private MyLabel scoreValue;
	private MyLabel player;
	private int row;
	private int col;
	private ArrayList<ArrayList<Integer>> highlightedCell;

	// constructor
	public graphic(game sameGame) {
		this.game = sameGame;
		this.game.setName(this.getName());
		gameFrame = new JFrame();
		gameFrame.setLayout(new BorderLayout());
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cellLayer = new JLayeredPane();
		score = new JPanel(new GridLayout(4, 2));
		score.add(new MyLabel("player name: "));
		player = new MyLabel(this.game.getPlayerName());
		score.add(player);
		score.add(new MyLabel("score: "));
		scoreValue = new MyLabel(null);
		score.add(scoreValue);
		scoreValue.showText(new String("" + 0));
		score.setVisible(true);
		gameFrame.add(cellLayer, BorderLayout.CENTER);
		createMenu();
		gameFrame.add(score, BorderLayout.EAST);
	}

	// create the board of the game
	public void createBoard(int row, int col) {
		this.row = row;
		this.col = col;
		gameFrame.setVisible(true);
		gameFrame.getContentPane().setPreferredSize(new Dimension(40 * col + 140, 40 * row + 70));

		cell = new ArrayList<ArrayList<myButton>>();

		for (int i = 0; i < col; i++) {
			cell.add(new ArrayList<myButton>());
			for (int j = 0; j < row; j++) {
				cell.get(i).add(new myButton());
				cell.get(i).get(j).setRow(j);
				cell.get(i).get(j).setCol(i);
				cell.get(i).get(j).setBounds(i * 40 + 10, 40 * (row - j), 40, 40);
				ArrayList<ArrayList<root.Game.GameTool.cell>> b = this.game.getBoard();
				int[] color = createColor((b.get(i).get(j)).getColor());
				cell.get(i).get(j).setBackground(new Color(color[0], color[1], color[2]));
				cell.get(i).get(j).setColor(color[0], color[1], color[2]);
				cell.get(i).get(j).setBorderPainted(true);
				cell.get(i).get(j).addMouseListener(new MouseClicked());
				cellLayer.add(cell.get(i).get(j), new Integer(2));
			}
		}
		gameFrame.pack();
		gameFrame.repaint();

	}

	// handle click on the cells
	private class MouseClicked extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			myButton tmpButton = (myButton) e.getComponent();
			ArrayList<ArrayList<Integer>> possibleCells = game.getPossibleChoices(tmpButton.getCol(), tmpButton.getRow());

			if (tmpButton.getH()) {
				changeHighlight = false;
				remove(possibleCells);
				game.remove(possibleCells);
				scoreValue.showText(new String("" + game.getScore()));
				score.repaint();
				
				final ArrayList<ArrayList<Integer>> shouldMove = sort(shouldMove());

				for (int i = 0; i < shouldMove.size(); i++)
					cell.get(shouldMove.get(i).get(0)).get(shouldMove.get(i).get(1)).setRow(shouldMove.get(i).get(1));
				if(game.isFinished()){
					JOptionPane.showMessageDialog(null, "Game Over!");
				}
				new Thread() {
					public void run() {
						for (int i = 0; i < shouldMove.size(); i++) {
							myButton tempButton = cell.get(shouldMove.get(i).get(0)).get(shouldMove.get(i).get(1));
							while (tempButton.getLocation().y != 40 * (row - shouldMove.get(i).get(1))) {
								tempButton.setLocation(tempButton.getLocation().x,tempButton.getLocation().y + 40 / 20);
								gameFrame.repaint();
								try {
									Thread.sleep(1000 / 500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					};
				}.start();
				return;
			}
			
			if (highlightedCell != null && changeHighlight)
				checkHighLight(highlightedCell, -19, false, true);

			gameFrame.repaint();
			if (possibleCells.size() == 1 && !game.check())
				return;

			
			highlightedCell = possibleCells;
			changeHighlight = true;
			checkHighLight(possibleCells, 19, true, false);
			gameFrame.repaint();
		}

	}


	// create the menu bar for the frame
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		menuBar.add(file);
		menuBar.add(edit);

		JMenuItem New = new JMenuItem("New");
		New.addMouseListener(new handleNewClick());

		JMenuItem reset = new JMenuItem("Reset this game");
		reset.addMouseListener(new handleResetClick());

		JMenuItem resetAndNew = new JMenuItem("Reset");
		resetAndNew.addMouseListener(new handleNewAndResetClick());
		
		JMenuItem changeColor = new JMenuItem("change number of colors");
		changeColor.addMouseListener(new handleColorChangeClick());

		JMenuItem exit = new JMenuItem("exit");
		exit.addMouseListener(new handleExitClick());

		JMenuItem changePlayer = new JMenuItem("change player");
		changePlayer.addMouseListener(new handlePlayerClick());
		
		file.add(New);
		file.add(reset);
		file.add(resetAndNew);
		file.add(exit);
		edit.add(changeColor);
		edit.add(changePlayer);
		gameFrame.add(menuBar, BorderLayout.NORTH);
	}

	// change the status of the given cells
	private void checkHighLight(ArrayList<ArrayList<Integer>> cells,int differenceInColor, boolean isHighlighted, boolean check) {
		for (int i = 0; i < cells.size(); i++) {
			if (check) {
				if (!cell.get(cells.get(i).get(0)).get(cells.get(i).get(1)).getH())
					continue;
			}
			int[] cellColor = cell.get(cells.get(i).get(0)).get(cells.get(i).get(1)).getColor();
			cell.get(cells.get(i).get(0)).get(cells.get(i).get(1))
					.setBackground(new Color(cellColor[0] + differenceInColor,cellColor[1] + differenceInColor,
							cellColor[2] + differenceInColor));
			cell.get(cells.get(i).get(0)).get(cells.get(i).get(1)).setColor(cellColor[0] + differenceInColor,
							cellColor[1] + differenceInColor,cellColor[2] + differenceInColor);
			cell.get(cells.get(i).get(0)).get(cells.get(i).get(1)).setH(isHighlighted);
		}
	}

	// change the number of colors in the board
	private class handleColorChangeClick extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			game.changeColors();
			changeBoard(row, col);
		}
	}

	// reset this game
	private class handleResetClick extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			game.reset();
			changeBoard(row, col);
		}
	}

	//reset and create a new board
	private class handleNewAndResetClick extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			game.newBoard(row, col);
			changeBoard(row, col);
		}
	}
	
	// create a new game
	private class handleNewClick extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			clear();
			row = getRow();
			col = getCol();
			String name = getName();
			game.setName(name);
			player.setText(name);
			game.changeColors();
			game.newBoard(row, col);
			changeBoard(row, col);
		}
	}

	// exit the window
	private class handleExitClick extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			System.exit(0);

		}
	}
	
	// change player's name
	private class handlePlayerClick extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			String s = getName();
			game.setName(s);
			game.newBoard(row, col);
			changeBoard(row, col);
			player.setText(s);

		}
	}


	// remove cells from jlayer pane
	private void clear() {
		for (int i = 0; i < cell.size(); i++) {
			for (int j = 0; j < cell.get(i).size(); j++) {
				cellLayer.remove(cell.get(i).get(j));
			}
		}
	}

	// change the board of game
	private void changeBoard(int row, int col) {
		clear();
		createBoard(row, col);
		scoreValue.showText(new String("" + game.getScore()));
	}

	// find the index of max element in the array list of integers
	private int findMax(ArrayList<ArrayList<Integer>> cellNumbers, int row,
			int col) {
		int res = 0;
		int tmp = col;
		for (int i = 0; i < cellNumbers.size(); i++) {
			if (cellNumbers.get(i).get(0) == row) {
				if (cellNumbers.get(i).get(1) > tmp) {
					res = i;
					tmp = cellNumbers.get(i).get(1);
				}
			}
		}
		return res;
	}

	// get the cells which should move
	private ArrayList<ArrayList<Integer>> shouldMove() {
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < cell.size(); i++) {
			for (int j = 0; j < cell.get(i).size(); j++) {
				if (cell.get(i).get(j).getRow() != j) {
					ArrayList<Integer> tmp = new ArrayList<Integer>();
					tmp.add(i);
					tmp.add(j);
					res.add(tmp);
				}
			}
		}
		return res;
	}

	// remove cells from the board
	private void remove(final ArrayList<ArrayList<Integer>> cells) {
		ArrayList<ArrayList<Integer>> removedCells = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < cells.size(); i++) {
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			tmp.add(cells.get(i).get(0));
			tmp.add(cells.get(i).get(1));
			removedCells.add(tmp);
			cellLayer.remove(cell.get(cells.get(i).get(0)).get(
					cells.get(i).get(1)));
		}
		removeFromList(removedCells);
		gameFrame.repaint();
	}

	// remove cells from the list of cells
	private void removeFromList(ArrayList<ArrayList<Integer>> remove) {
		while (remove.size() > 0) {
			ArrayList<Integer> tmpCell = remove.get(0);
			int index = findMax(remove, tmpCell.get(0), tmpCell.get(1));
			int col = remove.get(index).get(0);
			int row = remove.get(index).get(1);
			cell.get(col).remove(row);
			remove.remove(index);
		}
	}

	// get the number of colors
	public int getColors() {
		String tmp = JOptionPane.showInputDialog("Enter number of colors");
		return Integer.parseInt(tmp);
	}

	// get the name of the player
	public String getName() {
		return JOptionPane.showInputDialog("Enter player name: ");
	}

	// get number of rows
	public int getRow() {
		return Integer.parseInt(JOptionPane
				.showInputDialog("Enter number of rows: "));
	}

	// get number of columns
	public int getCol() {
		return Integer.parseInt(JOptionPane
				.showInputDialog("Enter number of cols: "));
	}

	// sort an arrayList
	private ArrayList<ArrayList<Integer>> sort(
			ArrayList<ArrayList<Integer>> arrayList) {
		ArrayList<ArrayList<Integer>> sortedArrayList = new ArrayList<ArrayList<Integer>>();
		while (arrayList.size() > 0) {
			int min = findMin(arrayList);
			for (int i = arrayList.size() - 1; i >= 0; i--) {
				if (arrayList.get(i).get(1) == min) {
					ArrayList<Integer> tmp = new ArrayList<Integer>();
					tmp.add(arrayList.get(i).get(0));
					tmp.add(arrayList.get(i).get(1));
					sortedArrayList.add(tmp);
					arrayList.remove(tmp);
				}
			}
		}
		return sortedArrayList;
	}

	// find the element with the minimum value in an array list
	private int findMin(ArrayList<ArrayList<Integer>> arrayList) {
		int res = arrayList.get(0).get(1);
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).get(1) < res) {
				res = arrayList.get(i).get(1);
			}
		}
		return res;
	}
	
	// create color
	public int[] createColor(int n) {
		int[] res = new int[3];
		int r = n % 6;
		res[0] = colorPattern[r][0];
		res[1] = colorPattern[r][1];
		res[2] = colorPattern[r][2];

		int k = n / 6;
		int h = k / 20;
		int s = (3 - (k) % 4) * 10;
		k %= 20;
		k *= 10;
		k -= h;
		if (r == 2) {
			res[0] += s;
			res[1] += s;
		} else if (r == 4) {
			res[1] += s;
			res[2] += s;
		} else {
			res[0] += s;
			res[2] += s;
		}
		switch (r) {
		case 0:
			res[1] += k;
			break;
		case 1:
			res[1] -= k;
			break;
		case 2:
			res[2] += k;
			break;
		case 3:
			res[1] -= k;
			break;
		case 4:
			res[0] += k;
			break;
		case 5:
			res[1] += k;
			break;
		}
		return res;
	}

}
