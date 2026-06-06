import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphingCalculator implements ActionListener, ChangeListener, MouseMotionListener{
	
	static Panel pn = new Panel();
	static JFrame frame;
	static JTextField eB;
	static JLabel eBL;
	static JButton eBB;
	static JButton cB;
	static JSlider dS;
	static JLabel dL;
	static JTextField dB;
	static JButton dBB;
	static JLabel eL;
	static JLabel dW;
	static JLabel wI;
	static JLabel mI;
	
	static ExpressionSimplifier es = new ExpressionSimplifier();
	static ArrayList<double[]> points = new ArrayList<double[]>();
	
	static int screenWidth;
	static int screenHeight;
	static final int IDEAL_SCREEN_WIDTH = 960;
	static final int IDEAL_SCREEN_HEIGHT = 600;
	static final int BOTTOM_SCREEN_LENGTH = 400;
	static final int X_SIZE_OFFSET = 40;
	static final int Y_SIZE_OFFSET = 4; //x and y offsets make graph window (-12<=x<=12, -45<=y<=45) approximately
	static final int POINT_THICKNESS = 5;
	static final int STANDARD_LINE_THICKNESS = 4;
	static final double X_BOUND = 50;
	static int detail = 1;
	static int xOffset;
	static int yOffset; //both x and y offsets should represent the origin on the graph (0,0)
	static int xLower;
	static int xUpper;
	static int yLower;
	static int yUpper;
	
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
		frame.setBounds(realWidth, realHeight, IDEAL_SCREEN_WIDTH, IDEAL_SCREEN_HEIGHT);
		frame.getContentPane();
		
		//making panel and calculator initial graphics
		pn.setLayout(null);
		pn.setBackground(Color.WHITE);
		pn.addMouseMotionListener(gc);
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
        dS.setBackground(Color.WHITE);
        dS.setValue(1);
        dS.setOrientation(SwingConstants.HORIZONTAL);
        dS.setMajorTickSpacing(1);
        dS.setSnapToTicks(true);
        dS.addChangeListener(gc);
        dS.setFont(new Font("Arial", Font.BOLD, 14));
		
		dL = new JLabel("Detail: 1");
		size = dL.getPreferredSize();
		dL.setBounds(100, 480, size.width + X_SIZE_OFFSET, size.height);
		
		dB = new JTextField(10);
		size = dB.getPreferredSize();
		dB.setBounds(350, 510, size.width, size.height);
		
		dBB = new JButton("Set Custom Detail");
		size = dBB.getPreferredSize();
		dBB.setBounds(480, 505, size.width + X_SIZE_OFFSET, size.height);
		dBB.addActionListener(gc);
		
		eL = new JLabel("Enter a function below:");
		size = eL.getPreferredSize();
		eL.setBounds(100, 425, size.width + 6*X_SIZE_OFFSET, size.height);
		
		dW = new JLabel("(Note: Any detail higher than 100 causes significant lag)");
		size = dW.getPreferredSize();
		dW.setBounds(350, 535, size.width + 6*X_SIZE_OFFSET, size.height);
		
		xLower = (0 - xOffset)/X_SIZE_OFFSET;
		xUpper = (IDEAL_SCREEN_WIDTH - xOffset)/X_SIZE_OFFSET;
		yLower = (BOTTOM_SCREEN_LENGTH - yOffset)/-Y_SIZE_OFFSET;
		yUpper = (0 - yOffset)/-Y_SIZE_OFFSET;
		
		wI = new JLabel(String.format("<html>Window: <br/>X: [%d, %d] <br/>Y: [%d, %d] </html>", xLower, xUpper, yLower, yUpper));
		size = wI.getPreferredSize();
		wI.setBounds(5, 400, size.width + X_SIZE_OFFSET, size.height);
		
		mI = new JLabel("<html>Position: <br/>()");
		size = mI.getPreferredSize();
		mI.setBounds(5, 450, size.width + X_SIZE_OFFSET, size.height);
		
		//adding everything to the frame and panel
		frame.add(pn);
		pn.add(eB);
		pn.add(eBL);
		pn.add(eBB);
		pn.add(dS);
		pn.add(dL);
		pn.add(cB);
		pn.add(eL);
		pn.add(dB);
		pn.add(dBB);
		pn.add(dW);
		pn.add(wI);
		pn.add(mI);
		
		frame.setVisible(true);
	}
	
	private void graph(String expression) {
		
		double deltaX = X_BOUND / (detail * 10.0); //scaling detail by a factor of 10
		points.clear();
		
		for (double i = 0 ; i > -X_BOUND ; i -= deltaX) {
			plotPoint(i, expression);
		}
		drawLines();
		
		points.clear();
		
		for (double i = 0 ; i < X_BOUND ; i += deltaX) {
			plotPoint(i, expression);
		}
		drawLines();
	}
	
	private void plotPoint(double x1, String expression) {
		String newExpression = expression.replace("x", "("+Double.toString(x1)+")");
		double y1 = 0;
		try {
			y1 = es.simplifyExpression(newExpression);
			
			if (Double.isNaN(y1)) return;
		} catch (Exception e) {
			sendError();
			return;
		}
		
		eL.setText("");
		double x2 = x1;
		double y2 = y1;
		
		x1 = xOffset + x1*X_SIZE_OFFSET; //xActual = offset + simulatedX*sizeOffset (simulatedX = (xActual - offset)/sizeOffset)
		y1 = yOffset - y1*Y_SIZE_OFFSET;
		x2 = xOffset + x2*X_SIZE_OFFSET;
		y2 = yOffset - y2*Y_SIZE_OFFSET;
		
		if (y1 >= BOTTOM_SCREEN_LENGTH) {
			if(x1 == xOffset) {
				points.add(new double[] {xOffset, BOTTOM_SCREEN_LENGTH});
				return;
			}
			double[] previous = points.getLast();
			double previousX = previous[0];
			double previousY = previous[1];
			
			if (previousY == BOTTOM_SCREEN_LENGTH) {
				points.add(new double[] {x1, BOTTOM_SCREEN_LENGTH});
				return;
			}
			
			double slope = (y1 - previousY) / (x1 - previousX);
			
			double newX = ((BOTTOM_SCREEN_LENGTH - y1) / slope) + x1;
			points.add(new double[] {newX, BOTTOM_SCREEN_LENGTH});
			
			return;
		}
		
		points.add(new double[] {x1, y1});
		pn.addLine(x1, y1, x2, y2, POINT_THICKNESS);
	}
	
	private void drawLines() {
		for (int i = 0 ; i < points.size() - 1 ; i++) {
			double[] coords1 = points.get(i);
			double[] coords2 = points.get(i + 1);
			
			pn.addLine(coords1[0], coords1[1], coords2[0], coords2[1], STANDARD_LINE_THICKNESS);
		}
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Graph")) {
            graph(eB.getText());
        } else if (s.equals("Clear")) {
        	pn.clear();
        } else if (s.equals("Set Custom Detail")) {
        	try {
        	
        	if (Integer.parseInt(dB.getText()) < 0) throw new NumberFormatException();
        	
        	detail = Integer.parseInt(dB.getText());
        	
        	} catch (NumberFormatException nE) {
        		sendError();
        		return;
        	}
        	eL.setText("");
            dL.setText("Detail: " + detail);
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
        dL.setText("Detail: " + detail);
    }
	
	private void sendError() {
		eL.setText("Sorry, I didn't understand your input.");
	}
	
	public void mouseMoved(MouseEvent e) {
		int xPos = (e.getX() - xOffset)/X_SIZE_OFFSET;
		int yPos = (e.getY() - yOffset)/-Y_SIZE_OFFSET;
		
		if ((xLower <= xPos && xPos <= xUpper) && (yLower <= yPos && yPos <= yUpper)) {
			mI.setText(String.format("<html>Position: <br/>(%d, %d)", xPos, yPos));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
}