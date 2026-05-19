import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Panel extends JPanel{
	protected LinkedList<Line> lines = new LinkedList<Line>();
	
	public void addLine(int x1, int y1, int x2, int y2) {
		Line line = new Line(x1, y1, x2, y2);
		lines.add(line);
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        for (int i = 0 ; i < lines.size() ; i++) {
        	Line line = lines.get(i);
        	
        	g2d.setColor(line.color);
            g2d.setStroke(new BasicStroke(line.stroke));
        	g2d.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
	}
}
