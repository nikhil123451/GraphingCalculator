import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;

public class GraphingCalculator {
	
	static Panel pn = new Panel();
	static JFrame frame;
	static JTextField eB;
	static JLabel eBL;
	static JButton eBB;
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		
		frame = new JFrame("Graphing Calculator");
		frame.setBounds(screenHeight/2, screenWidth/8, 960, 600); //found these create the ideal window
		frame.getContentPane();
		
		pn.setLayout(null);
		pn.addLine(0, 400, screenWidth, 400);
		
		eB = new JTextField(17);
		Dimension size = eB.getPreferredSize();
		eB.setBounds(100, 450, size.width, size.height);
		
		eBL = new JLabel("y =");
		size = eBL.getPreferredSize();
		eBL.setBounds(80, 450, size.width, size.height);
		
		eBB = new JButton("Graph");
		size = eBB.getPreferredSize();
		eBB.setBounds(300, 447, size.width, size.height);
		
		GraphingCalculator gc = new GraphingCalculator();
		eBB.addActionListener(new ActionListener());
		
		frame.add(pn);
		pn.add(eB);
		pn.add(eBL);
		pn.add(eBB);
		
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Graph")) {
            System.out.println(eB.getText());
        }
    }
}
