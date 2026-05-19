import java.awt.*;

public class Line {
	protected int x1;
	protected int y1;
	protected int x2;
	protected int y2;
	protected Color color;
	protected int stroke;
	
	public Line(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, Color.BLACK, 4);
	}
	
	public Line(int x1, int y1, int x2, int y2, Color color, int stroke) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
	}
}
