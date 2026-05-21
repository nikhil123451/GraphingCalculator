import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Panel extends JPanel{
	protected LinkedList<Line> lines = new LinkedList<Line>();
	static final int DEFAULT_STROKE = 4;
	static boolean clearing = false;
	
	public void addLine(int x1, int y1, int x2, int y2, int stroke, Color color) {
		Line line = new Line(x1, y1, x2, y2, stroke, color);
		lines.add(line);
		this.repaint();
	}
	
	public void addLine(int x1, int y1, int x2, int y2) {
		addLine(x1, y1, x2, y2, DEFAULT_STROKE, Color.BLACK);
	}
	
	public void addLine(int x1, int y1, int x2, int y2, int stroke) {
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
            g2d.setStroke(new BasicStroke(line.stroke));
        	g2d.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
	}
}
