package root.Graphic.MyComponent;

import java.awt.Font;
import javax.swing.*;
/**
 * @author Pegah Jandaghi(90105978)
 *
 */
public class MyLabel extends JLabel{

	//constructor
	public MyLabel(String a){
		super(a);
		this.setFont(new Font("Algerian", Font.PLAIN, 15));
	}
	
	//change the text of the label
	public void showText(String a){
		this.setText(a);
	}

}