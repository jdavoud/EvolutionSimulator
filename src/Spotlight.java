import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Spotlight extends PanelComponent {
	
	List<UpdateString> stats;
	Image image;
	int imageWidth = Main.panel_width - 40, imageHeight;
	int count = 0;
	Main main;
	Creature selected;
	
	public Spotlight(Main main, int x, int y) {
		this.main = main;
		this.x = x;
		this.y = y;
		this.width = Main.panel_width - 20;
		this.height = 190; 
		stats = new ArrayList<UpdateString>();
	}
	
	/*public void addStat(Object ... objects) {
		stats.add(new UpdateString ((int) x + 10, (int) y + 60 + 15 * count, objects));
		count++;
	}*/
	
	public void setSelected(Creature selected) {
		this.selected = selected;
		if (selected != null) {
			setImage(selected.baseImage());
			imageHeight = (int) (selected.baseHeight() / selected.baseWidth() * imageWidth);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.drawRoundRect((int) x, (int) y, (int) width, (int) height, 5, 5);
		
		if (selected != null && selected.getClass() == Shark.class) {
			Shark s = (Shark) selected;
			g.drawImage(image, Main.sim_width + 20, Main.panel_height - 240, imageWidth, imageHeight, main);
			
			int textX = Main.sim_width + 12;
			double curHealth = selected.getCurHealth();
			g.drawString("H: " + (curHealth > 0 ? Main.df.format(curHealth) : "RIP"), textX, Main.panel_height - 190);
			g.drawString("Age: " + selected.getDaysAlive(), textX, Main.panel_height - 175);
			g.drawString("Gen: " + selected.getGeneration(), textX, Main.panel_height - 160);
			g.drawString("Ate: " + s.ateToday(), textX, Main.panel_height - 145);
			g.drawString("Spd: " + Main.df.format(selected.getCurSpeed()), textX, Main.panel_height - 130);
			g.drawString("Mut: " + Main.df.format(selected.getMutability()), textX, Main.panel_height - 115);
			g.drawString("Wid: " + Main.df.format(selected.getWidth()), textX, Main.panel_height - 100);
			g.drawString("Spec: " + (s.getSpecies()), textX, Main.panel_height - 85);
			
		} else {
			g.drawString("No Shark", Main.sim_width + 23, Main.panel_height - 220);
			g.drawString("Selected", Main.sim_width + 25, Main.panel_height - 200);
			
		}
		
	}
	
	public void setImage (Image i ) {
		this.image = i;
	}
	

}
