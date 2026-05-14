import javax.swing.*;
import java.awt.*;

public class GraphingCalculator {
	
	static Panel pn = new Panel();
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		JFrame frame = new JFrame("Graphing Calculator");
		frame.setBounds(screenHeight/2, screenWidth/8, 960, 600); //found these create the ideal window
		frame.getContentPane();
		
		pn.setLayout(null);
		pn.newLine(0, 400, 500, 400);
		
		JTextField eB = new JTextField(17);
		Dimension size = eB.getPreferredSize();
		eB.setBounds(100, 450, size.width, size.height);
		
		JLabel eBL = new JLabel("y =");
		size = eBL.getPreferredSize();
		eBL.setBounds(80, 450, size.width, size.height);
		
		frame.add(pn);
		pn.add(eB);
		pn.add(eBL);
		
		frame.setVisible(true);
	}
}
