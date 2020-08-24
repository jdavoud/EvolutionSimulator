import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Creature {
	
	
	private double curHealth;
	private double health;
	private double size;
	private double curSpeed;
	private double maxSpeed;
	private double healthUse; 
	private double mutability;
	private int generation;
	private int daysAlive; 
	private double x, y; //width, height;
	private double xVel, yVel; 
	private double angle;
	
	abstract Image image(); 
	abstract double baseHeight();
	abstract double baseWidth();
	abstract Image baseImage();
	
	private final double base_health_use = 0.001;
	
	public abstract void move();
	
	
	public Creature() {
		setMutability(Trait.randomRange(0.01, 0.2));
		setSize(Trait.randomizeFromParent(1, mutability));
		setHealth(Trait.randomizeFromParent(100, mutability));
		setCurHealth(getHealth());
		setDaysAlive(1);
		setGeneration(1);
		while (baseWidth() < 0 || baseHeight() < 0) { } //some bug that maybe the image hadn't loaded yet
		setX(Trait.randomRange(0, Main.sim_width - getWidth()));
		setY(Trait.randomRange(Main.board_height, Main.frame_height - getHeight()));
		setHealthUse();
	}
	
	public Creature(Creature parent) {
		double mutability = parent.getMutability();
		setMutability(Trait.randomizeFromParent(parent.getMutability(), mutability));
		setSize(Trait.randomizeFromParent(parent.getSize(), mutability));
		setHealth(Trait.randomizeFromParent(100, mutability));
		setCurHealth(getHealth());
		setDaysAlive(1);
		setGeneration(parent.getGeneration() + 1);
		setX(parent.getX());
		setY(parent.getY());
		setHealth(parent.getHealth());
		setSize(Trait.randomizeFromParent(getSize(), mutability));
		setMaxSpeed (Trait.randomizeFromParent(parent.getMaxSpeed(), mutability));
		setCurSpeed (getMaxSpeed());
		setHealthUse();
	}
	
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	
	public void setHealthUse() {
		healthUse = base_health_use * SeaSimulator.speed;
		healthUse += Math.pow (size, 2) * Math.pow (getCurSpeed() * 4, 2) * 100 / 24 / SeaSimulator.frames_per_hour();
		
		/*if (getClass() == Shark.class) {
			System.out.println("Size: " + Main.df.format(getSize()) + " Width: " + Main.df.format(getWidth()) + " Speed " + Main.df.format(getCurSpeed()) + " HealthUse: " + Main.df.format(healthUse));
		}*/
	}
	
	public double getCurSpeed() {
		return curSpeed;
	}
	public void setCurSpeed(double speed) {
		this.curSpeed = speed;
	}
	
	public double useHealth() {
		if (curHealth > healthUse) {
			curHealth -= healthUse;
		} else if (curHealth > 0) { 
			curHealth = 0;
		}
		
		return curHealth;
	}

	public double getMutability() {
		return mutability;
	}

	public void setMutability(double mutability) {
		this.mutability = mutability;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public int getDaysAlive() {
		return daysAlive;
	}

	public void setDaysAlive(int daysAlive) {
		this.daysAlive = daysAlive;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getHeight() {
		return baseHeight() * size;
	}
	public double getWidth() {
		return baseWidth() * size;
	}
	public double getxVel() {
		return xVel;
	}
	public void setxVel(double xVel) {
		this.xVel = xVel * SeaSimulator.speed;
	}
	public double getyVel() {
		return yVel;
	}
	public void setyVel(double yVel) {
		this.yVel = yVel * SeaSimulator.speed;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getCurHealth() {
		return curHealth;
	}
	public void setCurHealth(double curHealth) {
		this.curHealth = curHealth;
	}
	
	public static void drawHealth(Graphics2D g, Creature c) { 
		g.setColor(Color.BLACK);
		g.drawRoundRect((int) c.getX(), (int) c.getY() - 5, (int) c.getWidth(), 5, 3, 3);
		g.setColor(Color.RED);
		g.fillRoundRect((int) c.getX(), (int) c.getY() - 5, (int) c.getWidth(), 5, 3, 3);
		g.setColor(Color.GREEN);
		g.fillRoundRect((int) c.getX(), (int) c.getY() - 5, (int) ((c.getWidth()) * c.getCurHealth() / c.getHealth()), 5, 3, 3);
		
	}
	
	public static void drawCreature (Graphics2D g, Creature c, Main main) {
		g.drawImage(c.image(), (int) c.getX(), (int) c.getY(), (int) (c.getWidth()), (int) (c.getHeight()), main);		
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}

