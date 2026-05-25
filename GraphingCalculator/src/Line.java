import java.awt.*;

public class Line {
	protected double x1;
	protected double y1;
	protected double x2;
	protected double y2;
	protected Color color;
	protected double stroke;
	static final double DEFAULT_STROKE = 4;
	
	public Line(double x1, double y1, double x2, double y2) {
		this(x1, y1, x2, y2, DEFAULT_STROKE, Color.BLACK);
	}
	
	public Line(double x1, double y1, double x2, double y2, double stroke) {
		this(x1, y1, x2, y2, stroke, Color.BLACK);
	}
	
	public Line(double x1, double y1, double x2, double y2, double stroke, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
	}
}
