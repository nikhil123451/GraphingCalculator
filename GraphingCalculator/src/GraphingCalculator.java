import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;

public class GraphingCalculator implements ActionListener{
	
	static Panel pn = new Panel();
	static JFrame frame;
	static JTextField eB;
	static JLabel eBL;
	static JButton eBB;
	static int screenWidth;
	static int screenHeight;
	static final int X_SIZE_OFFSET = 40;
	static final int Y_SIZE_OFFSET = 4; //x and y offsets make graph window (-12<=x<=12, -45<=y<=45) approximately
	static int detail = 4;
	static int xOffset;
	static int yOffset;
	
	public static void main(String[] args) {
		GraphingCalculator gc = new GraphingCalculator();
		
		//making frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		
		int realWidth = screenHeight/2;
		int realHeight = screenWidth/8;
		
		xOffset = realWidth + X_SIZE_OFFSET;
		yOffset = realHeight;
		
		frame = new JFrame("Graphing Calculator");
		frame.setBounds(realWidth, realHeight, 960, 600); //found these create the ideal window
		frame.getContentPane();
		
		//making panel and calculator initial graphics
		pn.setLayout(null);
		addMainLines();
		
		eB = new JTextField(17);
		Dimension size = eB.getPreferredSize();
		eB.setBounds(100, 450, size.width, size.height);
		
		eBL = new JLabel("y =");
		size = eBL.getPreferredSize();
		eBL.setBounds(80, 450, size.width, size.height);
		
		eBB = new JButton("Graph");
		size = eBB.getPreferredSize();
		eBB.setBounds(300, 447, size.width + X_SIZE_OFFSET, size.height);
		eBB.addActionListener(gc);
		
		//adding everything to the frame and panel
		frame.add(pn);
		pn.add(eB);
		pn.add(eBL);
		pn.add(eBB);
		
		frame.setVisible(true);
		
		pn.addLine(xOffset, yOffset, xOffset + 9*X_SIZE_OFFSET, yOffset - 40*Y_SIZE_OFFSET, 3, Color.BLUE);
		pn.addLine(xOffset, yOffset, xOffset + 10*X_SIZE_OFFSET, yOffset - 40*Y_SIZE_OFFSET, 3, Color.GREEN);
		pn.addLine(xOffset, yOffset, xOffset + 10*X_SIZE_OFFSET, yOffset - 30*Y_SIZE_OFFSET, 3, Color.RED);
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Graph")) {
            pn.clear();
        }
    }
	
	protected static void addMainLines() {
		pn.addLine(0, 400, screenWidth, 400); //bottom screen line
		pn.addLine(xOffset, 400, xOffset, 0, 2); //y-axis
		pn.addLine(0, yOffset, screenWidth, yOffset, 2); //x-axis
	}
}
