import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel{
	protected int x1, y1, x2, y2;
	protected Color color = Color.BLACK;
	
	public void newLine(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(4));
        
        g2d.drawLine(x1, y1, x2, y2);
	}
}
