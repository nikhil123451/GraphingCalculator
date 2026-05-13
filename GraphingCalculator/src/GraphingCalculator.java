import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GraphingCalculator {
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		JFrame frame = new JFrame("Graphing Calculator");
		frame.setVisible(true);
		frame.setBounds(screenHeight/2, screenWidth/8, 960, 600); //found these create the ideal window
	}
}
