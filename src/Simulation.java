import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class Simulation {
	public Panel panel;
	public Board board;
	public Color backgroundColor;
	public boolean paused;
	public int speed;
	protected Main main;
	
	public Simulation(Main main) {
		this.main = main;
		panel = new Panel(main);
		board = new Board(main, 4, 7);
	}
	
	public abstract void drawSim(Graphics2D g);
	public abstract void start();
	
	public void drawPanel(Graphics2D g) {
		panel.draw(g);
	}
	public void drawBoard(Graphics2D g) {
		board.draw(g);
	}
	public abstract void simPress(MouseEvent e);
	public void panelPress(MouseEvent e) {
		panel.checkPress(e);
	}
	
	public void togglePaused() {
		paused = !paused;
	}
	
}