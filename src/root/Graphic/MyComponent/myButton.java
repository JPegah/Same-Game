package root.Graphic.MyComponent;

import javax.swing.JButton;

/**
 * for displaying the cells of the game
 * @author Pegah Jandaghi(90105978)
 *
 */
public class myButton extends JButton{
	private int row;
	private int col;
	private boolean isHighlighted;
	private int[] color;
	
	//setters and getters
	public int getCol(){
		return col;
	}
	
	public int getRow(){
		return row;
	}
	
	public void setRow(int n){
		this.row = n;
	}
	
	public void setCol(int  n){
		this.col = n;
	}
	
	public boolean getH(){
		return this.isHighlighted;
	}
	
	public int[] getColor(){
		return this.color;
	}
	
	
	public void setH(boolean b){
		this.isHighlighted = b;
	}
	
	//set the color of the cell in RGB
	public void setColor(int r, int g, int b){
		this.color = new int[3];
		color[0] = r;
		color[1] =  g;
		color[2] = b;
	}
}
