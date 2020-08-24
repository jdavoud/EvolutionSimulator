import java.awt.Image;
import java.awt.Toolkit;

public class Fish extends Creature {
	
	public Fish (Fish parent) {
		
	}
	
	public Fish () {
		super();
		setHealth(50);
	}
	
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
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
		return Toolkit.getDefaultToolkit().getImage("fish.png");
	}

	@Override
	Image baseImage() {
		// TODO Auto-generated method stub
		return image();
	}

}
