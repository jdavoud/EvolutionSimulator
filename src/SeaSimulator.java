import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

public class SeaSimulator extends Simulation {
	
	public List<Fish> fishList;
	public List<Shark> sharkList;
	
	public int days = 1;
	public int hour = 0;
	
	public Double averageWidth = 0.0, averageMutability = 0.0, averageAge = 0.0, averageGen = 0.0, averageSpeed = 0.0;
	public Double visionPct = 0.0, awarenessPct = 0.0, burstPct = 0.0, aggressionPct = 0.0;
	
	public List<Average> averages;
	public List<Average> percents;
	
	
	private Timer t; 
	
	final static int sharkStart = 10;
	final static int fishStart = 20;
	
	final static boolean endDayNoSharks = true;
	
	private int stillMoving;
	
	private Shark selected;
	
	public static int speed = 1;
	
	
	public boolean paused;
	

	private int countFrames = 0;

	private List<Shark> newSharks;
	
	private Button pause, speedUp, speedDown;
	private Button restart;
	
	private Spotlight spotlight; 
	
	Image sharkImage = Toolkit.getDefaultToolkit().getImage("sharkright.png");
	int sharkWidth = Main.panel_width - 40, sharkHeight = 0;
	
	
	public SeaSimulator (Main m) {
		super (m);
		beginSim();
		defineAverages();
		createBoard(); 
		createPanel();
		defineTimer();
	}

	private void defineTimer() {
		t = new Timer(1000 / speed, e->  { //every 1000 / speed is an hour in this
			if (getPaused()) {
			}
			else if (hour < 24) {
				hour++;
				board.setTitle("Day " + getDay() +  " Hour " + getHour());
			} else {
				t.stop();
				days++;
				removeDeadSharks();
				for (Shark s : sharkList) {
					s.setDaysAlive(s.getDaysAlive() + 1);
					produceOffspring(s); //produce new sharks at end of day
				}
				addNewSharks();
				if (sharkList.size() == 0) {
					sharkList.add(new Shark()); //Adds a random new one
				}
				fishList = randomizeFish();
				/*for (int i = 0; i < sharkList.size(); i++) { //ascertains no fish spawns under a shark, may slow things down
					Fish f;
					while ((f = collides (sharkList.get(i))) != null) {
						fishList.remove(f);
						fishList.add(new Fish());
					}
				}*/
				//System.out.println(countFrames);
				countFrames = 0;
				try { Thread.sleep(500); } catch (InterruptedException i) { }
				start();
			}
		});
	}


	private void createBoard() {
		try {
			
			board.setTitle("Day " + getDay() +  " Hour " + getHour());
			
			board.addString(0, 0, "Fish: ", SeaSimulator.class.getMethod("getFishAmount"), this);
			board.addString(0, 1, "Sharks: ", SeaSimulator.class.getMethod("getSharkAmount"), this);
			board.addString(0, 2, "Hunting: ", SeaSimulator.class.getMethod("getStillMoving"), this);

			board.addString(1, 0, "Avg Age: ", SeaSimulator.class.getMethod("getAverageAge"), this);
			board.addString(1, 1, "Avg Gen: ", SeaSimulator.class.getMethod("getAverageGen"), this);
			board.addString(1, 2, "Hour: ", SeaSimulator.class.getMethod("getHour"), this);
			
			board.addString(2, 0, "Avg Spd: ", SeaSimulator.class.getMethod("getAverageSpeed"), this);
			board.addString(2, 1, "Avg Mut: ", SeaSimulator.class.getMethod("getAverageMutability"), this);
			board.addString(2, 2, "Avg Wid: ", SeaSimulator.class.getMethod("getAverageWidth"), this);
			
			board.addString(3, 0, "Vision %: ", SeaSimulator.class.getMethod("getVisionPct"), this);
			board.addString(3, 1, "Aware %: ", SeaSimulator.class.getMethod("getAwarenessPct"), this);
			board.addString(3, 2, "Burst %: ", SeaSimulator.class.getMethod("getBurstPct"), this);
			
			board.addString(4, 0, "Aggr %: ", SeaSimulator.class.getMethod("getAggressionPct"), this);
			
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}


	public void produceOffspring(Shark s) {
		for (int i = 0; i < s.ateToday; i++) {
			newSharks.add(new Shark(s));
		}
		s.ateToday = 0;
	}
	
	private void defineAverages() {
		averages = new LinkedList<Average>();
		percents = new LinkedList<Average>();
		
		try {
			averages.add(new Average(Shark.class.getMethod("getDaysAlive")));
			averages.add(new Average(Shark.class.getMethod("getMutability")));
			averages.add(new Average(Shark.class.getMethod("getMaxSpeed")));
			averages.add(new Average(Shark.class.getMethod("getWidth")));
			averages.add(new Average(Shark.class.getMethod("getGeneration")));
			
			percents.add(new Average(Shark.class.getMethod("hasAwareness")));
			percents.add(new Average(Shark.class.getMethod("hasVision")));
			percents.add(new Average(Shark.class.getMethod("hasBurst")));
			percents.add(new Average(Shark.class.getMethod("hasAggression")));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	
	private void calculateAverages() {
		averages.forEach((a) -> { a.reset();});
		percents.forEach((p) -> { p.reset();});
		
		sharkList.forEach((s) -> {
			averages.forEach((a) -> {
				try {
					a.addToAverage(s);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
			percents.forEach((p) -> {
				try {
					p.addToPercent(s);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
		});
		averages.forEach((a) -> { a.calculate();});
		percents.forEach((p) -> { p.calculate();});
		
		averageAge = averages.get(0).getAverage();
		averageMutability = averages.get(1).getAverage();
		averageSpeed = averages.get(2).getAverage();
		averageWidth = averages.get(3).getAverage();
		averageGen = averages.get(4).getAverage();
	
		visionPct = percents.get(0).getAverage();
		awarenessPct = percents.get(1).getAverage();
		burstPct = percents.get(2).getAverage();
		aggressionPct = percents.get(3).getAverage();
	}
	
	public Fish collides (Shark s) {
		double leftX = s.getX(), rightX = s.getX() + s.getWidth();
		double topY = s.getY(), bottomY = s.getY() + s.getHeight();
		double midX = (leftX + rightX) / 2;
		double midY = (topY + bottomY) / 2;
		for(Fish f : fishList) {
			if (s.hasAwareness()) {
				double fishMidX = f.getX() + f.getWidth() / 2;
				double fishMidY = f.getY() + f.getHeight() / 2;
				double distance = Math.sqrt(Math.pow(midX - fishMidX, 2) + Math.pow(midY - fishMidY, 2));
				if (distance < s.awarenessLevel()) { //bigger has better awareness
					s.setTarget(new Point2D.Double(f.getX() - 2, f.getY() - 2));
					s.setFishTarget(false);
				}
			}
			
			if (f.getY() + f.getHeight() <= bottomY && f.getY() >= topY) {
				if (f.getX() >= leftX && f.getX() + f.getWidth() <= rightX) { ////fish can be eaten
					return f;
				} else if (s.lookingForFish() && (s.getxVel() * (f.getX() - rightX) > 0)) { //fish is seen
					s.setTarget(new Point2D.Double(f.getX() - 2, f.getY()- 2));
					s.setFishTarget(true);
				}
			}
		}
		
		return null;
	}
	
	/*
	@Override
	public void drawSim(Graphics2D g, Main main) {
		paintCreatures(g, main);
	}*/
	
	public void paintCreatures(Graphics2D g) {
		countFrames++;
		for (Fish f : getFish()) {
			Creature.drawCreature(g, f, main);
		}
		for (Shark s : sharkList) {
			Creature.drawCreature(g, s, main);
			Fish food = collides(s);
			if (food != null) {
				s.setCurHealth(s.getHealth());
				s.ateToday++;
				if (!s.hasAggression()) {
					stillMoving--;
				}
				fishList.remove(food);
			}
			if (hour < 24 && !paused) {
				s.move();
			}
		}
		if (selected != null && selected.getCurHealth() > 0) {
			drawSelectedShark(g);
		}
		removeDeadSharks();
		if (endDayNoSharks && stillMoving == 0) {
			hour = 24;
		}
	}
	
	public void drawSelectedShark(Graphics2D g) {
		Shark s = selected;
		g.setColor(Color.black);
		g.drawRect((int) s.getX(), (int) s.getY(), (int) s.getWidth(), (int) s.getHeight());
		drawAttributes(g, s);
		Creature.drawHealth(g, s);
	}
	
	public void addNewSharks() {
		sharkList.addAll(newSharks);
	}
	
	public void removeDeadSharks() {
		int size = sharkList.size();
		if (sharkList.removeIf(s -> (s.getCurHealth() <= 0))) {
			stillMoving -= (size - sharkList.size());
		}
	}
	
	public List<Fish> getFish(){
		return fishList;
	}
	
	public List<Shark> getSharks() {
		return sharkList;
	}
	
	public List<Fish> randomizeFish () {
		List<Fish> arr = new LinkedList<Fish>();
		for (int i = 0; i < fishStart; i++) { 
			Fish f = new Fish();
			arr.add(f);
		}
		return arr;
	}
	public List<Shark> randomizeSharks () {
		List<Shark> arr = new LinkedList<Shark>();
		for (int i = 0; i < sharkStart; i++) { 
			Shark s = new Shark();
			arr.add(s);
		}
		return arr;
	}
	
	public int getHour() {
		return hour;
	}
	public int getDay() {
		return days;
	}
	
	public double getAverageWidth() { 
		return averageWidth;
	}
	
	public double getAverageSpeed() { 
		return averageSpeed;
	}
	
	public double getAverageAge() {
		return averageAge;
	}
	public double getAverageMutability() {
		return averageMutability;
	}
	
	public double getAverageGen() {
		return averageGen;
	}
	
	public double getVisionPct () {
		return visionPct;
	}
	
	public double getAwarenessPct () {
		return awarenessPct;
	}
	
	public double getBurstPct() {
		return burstPct;
	}
	
	public double getAggressionPct() {
		return aggressionPct;
	}
	
	public int getSharkAmount() {
		return sharkList.size();
	}
	
	public int getFishAmount() {
		return fishList.size();
	}
	
	public int getStillMoving() {
		return stillMoving;
	}
	
	public boolean getPaused() {
		return paused;
	}
	
	public void togglePaused() {
		paused = !paused;
	}
	
	public void drawAttributes(Graphics2D g, Shark s) {
		
		if (s.hasVision()) {
			g.setColor(Color.red);
			int x;
			int width;
			if (s.getxVel() >= 0) {
				x = (int) (s.getX() + s.getWidth());
				width = Main.sim_width - x;
				
			} else {
				x = 0;
				width = (int) s.getX();
			}
			g.drawRect(x, (int) s.getY(), width, (int) s.getHeight());
		}
		if (s.hasAwareness()) {
			
			double midX = s.getX() +  s.getWidth() / 2;
			double midY = s.getY() + s.getHeight() / 2;
			
			int radius = (int) (s.awarenessLevel());
			int x = (int) (midX - radius);
			int y = (int) (midY - radius);
			
			g.setColor(Color.black);
			g.drawOval(x, y, 2 * radius, 2 * radius);
		}
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public static int frames_per_hour() {
		return 6300 / 24 / speed;
	}
	
	public void setSpeed(int speed) { 
		this.speed = speed;
		t.setDelay(1000 / speed);
		for (Shark s : sharkList) {
			s.setHealthUse();
			if (s.getTarget() != null) {
				s.changeVelocity();
			}
		}
	}

	public void restart() {
		t.stop();
		beginSim();
		start();
	}
	
	private void beginSim() {
		fishList = randomizeFish();
		sharkList = randomizeSharks();
		newSharks = new LinkedList<Shark>();
		days = 1;
		hour = 0;
		paused = false;
	}
	
	public Shark getSelected () {
		return selected;
	}
	
	private void createPanel() {
		
		spotlight = new Spotlight(main, Main.sim_width + 10, Main.panel_height - 250);
		panel.add(spotlight);
		
		pause = new Button("Pause", Main.sim_width + 10, 10, Main.panel_width - 20, 40);
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePaused();
				main.repaint();
			}
		});
		panel.add(pause);
		
		speedUp = new Button(">", Main.frame_width - 30, 80, 20, 20);
		speedUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int speed = getSpeed();
				if (speed < 32) {
					setSpeed(speed * 2);
				}
				main.repaint();
			}
		});
		panel.add(speedUp);
		
		speedDown = new Button("<", Main.sim_width + 10, 80, 20, 20);
		speedDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int speed = getSpeed();
				if (speed > 1) {
					setSpeed(speed / 2);
				}
				main.repaint();
			}
		});
		panel.add(speedDown);
		
		restart = new Button("Restart", Main.sim_width + 10, Main.panel_height - 50, Main.panel_width - 20, 40);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
				main.repaint();
			}
		});
		panel.add(restart);
	}


	@Override
	public void simPress(MouseEvent e) {
		for (Shark s : sharkList) {
			if (e.getX() >= s.getX() && e.getX() <= s.getX() + s.getWidth()
				&& e.getY() >= s.getY() && e.getY() <= s.getY() + s.getHeight()) {
				selected = s;
				spotlight.setSelected(s);
				return;
			}
		}
		spotlight.setSelected(null);
		selected = null;
	}


	@Override
	public void drawSim(Graphics2D g) {
		main.setBackground(Color.CYAN);
		//if (!paused) {
			paintCreatures(g);
			if (!paused) {
				main.repaint();
			}
		//}
	}


	@Override
	public void start() {
		hour = 0;
		newSharks = new LinkedList<Shark>();
		stillMoving = sharkList.size();
		calculateAverages();
		t.start();
	}
	
}

class Average {
	Double d;
	Method m;
	int count;
	public Average(Method m) {
		this.count = 0;
		this.d = 0.0;
		this.m = m;
	}
	public void reset() {
		this.count = 0;
		this.d = 0.0;
	}
	public void addToAverage(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		count++;
		Object o = m.invoke(obj);
		d += (o.getClass() == Double.class ? (double) o : (int) o);
		
	}
	public void addToPercent(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		count++;
		d += ((boolean) m.invoke(obj)) ? 1 : 0;
	}
	
	public void calculate() {
		d /= count;
	}
	
	public void subtractFromAverage(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		d *= count;
		d -= (double) m.invoke(obj);
		count--;
		d /= count;
	}
	
	public void subtractFromPercent(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		d *= count;
		d -= ((boolean) m.invoke(obj)) ? 1 : 0;
		count--;
		d /= count;
	}
	
	public Double getAverage() {
		return this.d;
	}
	
	
	
}

