import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;

public class Main extends Applet implements MouseListener {
	Simulation sim;
	boolean simulating = true;
	Graphics2D g2;
	
	public static int sim_width = 800, sim_height = 500;
	public static int board_width = 800, board_height = 100;
	public static int panel_width = 100, panel_height = 600;
	public static int frame_width = sim_width + panel_width, frame_height = 600;
	
	static DecimalFormat df = new DecimalFormat("#.###");
	
	public void init() {
		sim = new SeaSimulator(this);
		setSize(frame_width,frame_height);
		addMouseListener(this);
		sim.start();
	}

	public void paint(Graphics g) {
		g2 = (Graphics2D) g;
		sim.drawSim(g2);
		sim.drawBoard(g2);
		sim.drawPanel(g2);
	}
	
	/*private void drawPanel(Graphics2D g2) {
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(sim_width, 0, panel_width, panel_height);
		drawButtons(g2);
		g2.drawString("Speed", sim_width + 30, 70);
		g2.drawString(String.valueOf(sim.speed), sim_width + 45, 95);
		
		g2.drawRoundRect(sim_width + 10, panel_height - 250,panel_width - 20, 190, 5, 5);
		Shark selected = sim.getSelected();
		if (selected != null) {
			if (sharkHeight == 0) {
				sharkHeight = (int) (selected.baseHeight() / selected.baseWidth() * sharkWidth);
			}
			g2.drawImage(sharkImage, sim_width + 20, panel_height - 240, sharkWidth, sharkHeight, this);
			
			int textX = sim_width + 12;
			double curHealth = selected.getCurHealth();
			g2.drawString("H: " + (curHealth > 0 ? df.format(curHealth) : "RIP"), textX, panel_height - 190);
			g2.drawString("Age: " + selected.getDaysAlive(), textX, panel_height - 175);
			g2.drawString("Gen: " + selected.getGeneration(), textX, panel_height - 160);
			g2.drawString("Ate: " + selected.ateToday, textX, panel_height - 145);
			g2.drawString("Spd: " + df.format(selected.getCurSpeed()), textX, panel_height - 130);
			g2.drawString("Mut: " + df.format(selected.getMutability()), textX, panel_height - 115);
			g2.drawString("Wid: " + df.format(selected.getWidth()), textX, panel_height - 100);
			
		} else {
			g2.drawString("None", sim_width + 33, panel_height - 220);
			g2.drawString("???", sim_width + 40, panel_height - 180);
			
		}
		
	}*/

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() >= sim_width) { //Could be a button, in the panel
			sim.panelPress(e);
		} else {
			sim.simPress(e);
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
