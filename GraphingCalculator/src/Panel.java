import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Panel extends JPanel{
	
	protected LinkedList<Line> lines = new LinkedList<Line>();
	static final double DEFAULT_STROKE = 4;
	static boolean clearing = false;
	
	public void addLine(double x1, double y1, double x2, double y2, double stroke, Color color) {
		Line line = new Line(x1, y1, x2, y2, stroke, color);
		lines.add(line);
		this.repaint();
	}
	
	public void addLine(double x1, double y1, double x2, double y2) {
		addLine(x1, y1, x2, y2, DEFAULT_STROKE, Color.BLACK);
	}
	
	public void addLine(double x1, double y1, double x2, double y2, double stroke) {
		addLine(x1, y1, x2, y2, stroke, Color.BLACK);
	}
	
	public void clear() {
		clearing = true;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        if (clearing) {
        	lines.clear();
        	clearing = false;
        	GraphingCalculator.addMainLines();
        }
        
        for (int i = 0 ; i < lines.size() ; i++) {
        	Line line = lines.get(i);
        	
        	g2d.setColor(line.color);
            g2d.setStroke(new BasicStroke((int) line.stroke));
        	g2d.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
        }
	}
}
