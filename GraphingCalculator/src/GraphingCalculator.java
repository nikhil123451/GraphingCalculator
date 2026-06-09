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
	
	static Panel panel = new Panel();
	static JFrame frame;
	static JTextField equationBox;
	static JLabel equationBoxLabel;
	static JButton equationBoxButton;
	static JButton clearButton;
	static JSlider detailSlider;
	static JLabel detailLabel;
	static JTextField detailBox;
	static JButton detailBoxButton;
	static JLabel errorLabel;
	static JLabel detailWarning;
	static JLabel windowInformation;
	static JLabel mouseInformation;
	
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
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.addMouseMotionListener(gc);
		addMainLines();
		
		equationBox = new JTextField(17);
		Dimension size = equationBox.getPreferredSize();
		equationBox.setBounds(100, 450, size.width, size.height);
		
		equationBoxLabel = new JLabel("y =");
		size = equationBoxLabel.getPreferredSize();
		equationBoxLabel.setBounds(80, 450, size.width, size.height);
		
		equationBoxButton = new JButton("Graph");
		size = equationBoxButton.getPreferredSize();
		equationBoxButton.setBounds(300, 447, size.width + X_SIZE_OFFSET, size.height);
		equationBoxButton.addActionListener(gc);
		
		clearButton = new JButton("Clear");
		size = clearButton.getPreferredSize();
		clearButton.setBounds(425, 447, size.width + X_SIZE_OFFSET, size.height);
		clearButton.addActionListener(gc);
		
		detailSlider = new JSlider(0, 10);
		size = detailSlider.getPreferredSize();
		detailSlider.setBounds(80, 500, size.width + X_SIZE_OFFSET, size.height + X_SIZE_OFFSET);
        detailSlider.setPaintTrack(true);
        detailSlider.setPaintTicks(true);
        detailSlider.setPaintLabels(true);
        detailSlider.setBackground(Color.WHITE);
        detailSlider.setValue(1);
        detailSlider.setOrientation(SwingConstants.HORIZONTAL);
        detailSlider.setMajorTickSpacing(1);
        detailSlider.setSnapToTicks(true);
        detailSlider.addChangeListener(gc);
        detailSlider.setFont(new Font("Arial", Font.BOLD, 14));
		
		detailLabel = new JLabel("Detail: 1");
		size = detailLabel.getPreferredSize();
		detailLabel.setBounds(100, 480, size.width + X_SIZE_OFFSET, size.height);
		
		detailBox = new JTextField(10);
		size = detailBox.getPreferredSize();
		detailBox.setBounds(350, 510, size.width, size.height);
		
		detailBoxButton = new JButton("Set Custom Detail");
		size = detailBoxButton.getPreferredSize();
		detailBoxButton.setBounds(480, 505, size.width + X_SIZE_OFFSET, size.height);
		detailBoxButton.addActionListener(gc);
		
		errorLabel = new JLabel("Enter a function below:");
		size = errorLabel.getPreferredSize();
		errorLabel.setBounds(100, 425, size.width + 6*X_SIZE_OFFSET, size.height);
		
		detailWarning = new JLabel("(Note: Any detail higher than 100 causes significant lag)");
		size = detailWarning.getPreferredSize();
		detailWarning.setBounds(350, 535, size.width + 6*X_SIZE_OFFSET, size.height);
		
		xLower = (0 - xOffset)/X_SIZE_OFFSET;
		xUpper = (IDEAL_SCREEN_WIDTH - xOffset)/X_SIZE_OFFSET;
		yLower = (BOTTOM_SCREEN_LENGTH - yOffset)/-Y_SIZE_OFFSET;
		yUpper = (0 - yOffset)/-Y_SIZE_OFFSET;
		
		windowInformation = new JLabel(String.format("<html>Window: <br/>X: [%d, %d] <br/>Y: [%d, %d] </html>", xLower, xUpper, yLower, yUpper));
		size = windowInformation.getPreferredSize();
		windowInformation.setBounds(5, 400, size.width + X_SIZE_OFFSET, size.height);
		
		mouseInformation = new JLabel("<html>Position: <br/>()");
		size = mouseInformation.getPreferredSize();
		mouseInformation.setBounds(5, 450, size.width + X_SIZE_OFFSET, size.height);
		
		//adding everything to the frame and panel
		frame.add(panel);
		panel.add(equationBox);
		panel.add(equationBoxLabel);
		panel.add(equationBoxButton);
		panel.add(detailSlider);
		panel.add(detailLabel);
		panel.add(clearButton);
		panel.add(errorLabel);
		panel.add(detailBox);
		panel.add(detailBoxButton);
		panel.add(detailWarning);
		panel.add(windowInformation);
		panel.add(mouseInformation);
		
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
		
		errorLabel.setText("");
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
		panel.addLine(x1, y1, x2, y2, POINT_THICKNESS);
	}
	
	private void drawLines() {
		for (int i = 0 ; i < points.size() - 1 ; i++) {
			double[] coords1 = points.get(i);
			double[] coords2 = points.get(i + 1);
			
			panel.addLine(coords1[0], coords1[1], coords2[0], coords2[1], STANDARD_LINE_THICKNESS);
		}
	}
	
	public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Graph")) {
            graph(equationBox.getText());
        } else if (s.equals("Clear")) {
        	panel.clear();
        } else if (s.equals("Set Custom Detail")) {
        	try {
        	
        	if (Integer.parseInt(detailBox.getText()) < 0) throw new NumberFormatException();
        	
        	detail = Integer.parseInt(detailBox.getText());
        	
        	} catch (NumberFormatException nE) {
        		sendError();
        		return;
        	}
        	errorLabel.setText("");
            detailLabel.setText("Detail: " + detail);
        }
    }
	
	protected static void addMainLines() {
		panel.addLine(0, 400, screenWidth, 400); //bottom screen line
		panel.addLine(xOffset, 400, xOffset, 0, 2); //y-axis
		panel.addLine(0, yOffset, screenWidth, yOffset, 2); //x-axis
	}
	
	public void stateChanged(ChangeEvent e)
    {
		detail = detailSlider.getValue();
        detailLabel.setText("Detail: " + detail);
    }
	
	private void sendError() {
		errorLabel.setText("Sorry, I didn't understand your input.");
	}
	
	public void mouseMoved(MouseEvent e) {
		int xPosition = (e.getX() - xOffset)/X_SIZE_OFFSET;
		int yPosition = (e.getY() - yOffset)/-Y_SIZE_OFFSET;
		
		if ((xLower <= xPosition && xPosition <= xUpper) && (yLower <= yPosition && yPosition <= yUpper)) {
			mouseInformation.setText(String.format("<html>Position: <br/>(%d, %d)", xPosition, yPosition));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
}