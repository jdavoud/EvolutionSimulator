import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Panel {
	
	List<PanelComponent> components;
	Main main;
	
	public Panel(Main main) {
		this.main = main;
		components = new ArrayList<PanelComponent>();
	}

	public void checkPress(MouseEvent e) {
		Rectangle2D mouseCoords = new Rectangle2D.Double(e.getX(), e.getY(), 1, 1);
		for (PanelComponent c : components) {
			if (c.getClass().equals(Button.class)) {
				Button b = (Button) c;
				if (b.getButton().intersects(mouseCoords)) {
					b.performActions();
				}
			}
			
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(Main.sim_width, 0, Main.panel_width, Main.panel_height);
		for (PanelComponent c : components) {
			c.draw(g);
		}
	}
	
	public void add(PanelComponent c) {
		components.add(c);
	}
}
