import java.awt.Graphics2D;

public abstract class PanelComponent {
	public double x;
	public double y;
	public double width;
	public double height;
	
	public abstract void draw(Graphics2D g);
	
}
