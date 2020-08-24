import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Shark extends Creature {
	
	private Point2D target = null;
	public int ateToday = 0;
	public int moveX; 
	public boolean vision = false;
	private boolean fishTarget = false;
	public boolean awareness = false;
	public boolean burst = false;
	public boolean aggression = false;
	
	private static Image rightShark = Toolkit.getDefaultToolkit().getImage("sharkright.png");
	private static Image leftShark = Toolkit.getDefaultToolkit().getImage("sharkleft.png");
	
	public static int numberOfSpecies = 1;
	public int species;
	
	

	public Shark (Shark parent) {
		super(parent);
		
		setSpecies(parent.getSpecies());
		vision = Trait.developTrait(parent.hasVision(), getMutability());
		awareness = Trait.developTrait(parent.hasVision(), getMutability());
		burst = Trait.developTrait(parent.hasBurst(), getMutability());
		aggression = Trait.developTrait(parent.hasAggression(), getMutability());
	}

	public Shark () {
		super();
		setHealth(100);
		setSize(Trait.randomizeFromParent(getSize(), 0.5));
		setMaxSpeed (Trait.randomizeFromParent(0.25, 0.5));
		setCurSpeed (getMaxSpeed());
		setHealthUse();
		setSpecies(numberOfSpecies);
		numberOfSpecies++;
	}
	
	
	public void randomizeTarget() {
		target = new Point2D.Double(Trait.randomRange(0, 800 - getWidth()), Trait.randomRange(100, 600 - getHeight()));
		changeVelocity();
	}
	
	public void setTarget(Point2D point) {
		target = point;
		changeVelocity();
	}
	
	public Point2D getTarget() {
		return target;
	}
	
	public void changeVelocity() {
		
		setAngle(Math.atan2(target.getY() - getY(), target.getX() - getX()));
		moveX = target.getX() >= getX() ? 1 : -1;
		
		setCurSpeed(getMaxSpeed() * (burst && fishTarget ? 2 : 1));
		
		setxVel(Math.cos(getAngle()) * getCurSpeed());
		setyVel(Math.sin(getAngle()) * getCurSpeed());
	}

	@Override
	public void move() {
		if (getCurHealth() <= 0) {
			return;
		}
		if (target == null) {
			randomizeTarget();
			if (hasVision()) {
				fishTarget = false;
			}
		}
		if (ateToday < 1 || aggression) {
			setX(getX() + getxVel());
			setY(getY() + getyVel());
			useHealth();
		} else {
			return;
		}
		
		//if (ateToday < 1) {
		//	useHealth();
		/*} else {
			if (getCurHealth() > 1) {
				useHealth();
			}
			if (getCurHealth() < 1) {
				setCurHealth(1);
				setxVel(0);
				setyVel(0);
			}
		}*/
		int polarity = target.getX() >= getX() ? 1 : -1;
		if (moveX != polarity) {
			target = null;
		}
	}
	
	public boolean hasVision() {
		return vision;
	}

	@Override
	double baseHeight() {
		return image().getHeight(null);
	}

	@Override
	double baseWidth() {
		return image().getWidth(null);
	}

	@Override
	Image image() {
		return (getxVel() < 0 ? leftShark : rightShark);
	}

	public boolean lookingForFish() {
		if (hasVision() && !fishTarget) {
			return true;
		}
		return false;
	}
	public void setFishTarget(boolean b) {
		fishTarget = b;
	}
	
	public boolean hasAwareness() {
		return awareness;
	}
	
	public double awarenessLevel () {
		return getHeight();
	}
	
	public boolean hasBurst() {
		return this.burst;
	}
	
	public boolean hasAggression() {
		return this.aggression;
	}
	
	public int getSpecies() {
		return species;
	}

	public void setSpecies(int species) {
		this.species = species;
	}
	
	public void drawMiniShark (Graphics2D g, Shark s, int x, int y, int width, int height, Main main) {
		g.drawImage(rightShark, x, y, width, height, main);
		
		g.setColor(Color.BLACK);
		g.drawRoundRect(x, (int) y - 2, width, 5, 3, 3);
		g.setColor(Color.RED);
		g.fillRoundRect(x, (int) y - 2, width, 5, 3, 3);
		g.setColor(Color.GREEN);
		g.fillRoundRect(x, y - 2,  (width) * (int) (s.getCurHealth() / s.getHealth()), 5, 3, 3);
		
	}
	
	public int ateToday() {
		return ateToday;
	}

	@Override
	Image baseImage() {
		// TODO Auto-generated method stub
		return rightShark;
	}
}

