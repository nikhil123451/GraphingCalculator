import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;

public class GraphingCalculator implements ActionListener, ChangeListener{
	
	static Panel pn = new Panel();
	static JFrame frame;
	static JTextField eB;
	static JLabel eBL;
	static JButton eBB;
	static JButton cB;
	static JSlider dS;
	static JLabel dL;
	static JLabel eL;
	
	static ExpressionSimplifier es = new ExpressionSimplifier();
	
	static int screenWidth;
	static int screenHeight;
	static final int BOTTOM_SCREEN_LENGTH = 400;
	static final int X_SIZE_OFFSET = 40;
	static final int Y_SIZE_OFFSET = 4; //x and y offsets make graph window (-12<=x<=12, -45<=y<=45) approximately
	static final int POINT_THICKNESS = 7;
	static final double X_BOUND = 15;
	static final double Y_BOUND = 45;
	static int detail = 1;
	static int xOffset;
	static int yOffset; //both x and y offsets should represent the origin on the graph (0,0)
	
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
		
		cB = new JButton("Clear");
		size = cB.getPreferredSize();
		cB.setBounds(425, 447, size.width + X_SIZE_OFFSET, size.height);
		cB.addActionListener(gc);
		
		dS = new JSlider(0, 10);
		size = dS.getPreferredSize();
		dS.setBounds(80, 500, size.width + X_SIZE_OFFSET, size.height + X_SIZE_OFFSET);
        dS.setPaintTrack(true);
        dS.setPaintTicks(true);
        dS.setPaintLabels(true);
        dS.setValue(1);
        dS.setOrientation(SwingConstants.HORIZONTAL);
        dS.setMajorTickSpacing(1);
        dS.setSnapToTicks(true);
        dS.addChangeListener(gc);
        dS.setFont(new Font("Arial", Font.BOLD, 14));
		
		dL = new JLabel("Detail: 1");
		size = dL.getPreferredSize();
		dL.setBounds(100, 480, size.width + X_SIZE_OFFSET, size.height);
		
		eL = new JLabel("");
		size = eL.getPreferredSize();
		eL.setBounds(100, 430, size.width + 6*X_SIZE_OFFSET, size.height);
		
		//adding everything to the frame and panel
		frame.add(pn);
		pn.add(eB);
		pn.add(eBL);
		pn.add(eBB);
		pn.add(dS);
		pn.add(dL);
		pn.add(cB);
		pn.add(eL);
		
		frame.setVisible(true);
	}
	
	private void graph(String expression) {
		
		double deltaX = X_BOUND / (detail * 10.0); //scaling detail by a factor of 10
		
		for (double i = 0 ; i > -X_BOUND ; i -= deltaX) {
			plotPoint(i, expression);
		}
		for (double i = deltaX ; i < X_BOUND ; i += deltaX) {
			plotPoint(i, expression);
		}
	}
	
	private void plotPoint(double x1, String expression) {
		String newExpression = expression.replace("x", "("+Double.toString(x1)+")");
		double y1 = 0;
		try {
			y1 = es.simplifyExpression(newExpression);
		} catch (NumberFormatException e) {
			eL.setText("Sorry, I didn't understand your input.");
			return;
		}
		
		eL.setText("");
		double x2 = x1;
		double y2 = y1;
		
		x1 = xOffset + x1*X_SIZE_OFFSET;
		y1 = yOffset - y1*Y_SIZE_OFFSET;
		x2 = xOffset + x2*X_SIZE_OFFSET;
		y2 = yOffset - y2*Y_SIZE_OFFSET;
		
		if (y1 >= BOTTOM_SCREEN_LENGTH) return;
		pn.addLine(x1, y1, x2, y2, POINT_THICKNESS);
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Graph")) {
            graph(eB.getText());
        } else if (s.equals("Clear")) {
        	pn.clear();
        }
    }
	
	protected static void addMainLines() {
		pn.addLine(0, 400, screenWidth, 400); //bottom screen line
		pn.addLine(xOffset, 400, xOffset, 0, 2); //y-axis
		pn.addLine(0, yOffset, screenWidth, yOffset, 2); //x-axis
	}
	
	public void stateChanged(ChangeEvent e)
    {
		detail = dS.getValue();
        dL.setText("Detail: " + dS.getValue());
    }
}