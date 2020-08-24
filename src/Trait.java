import java.util.Random;

public class Trait {
	
	static Random rnd = new Random();

	public static double randomRange(double min, double max) {
		return (max - min) * rnd.nextDouble() + min;
		
	}
	
	public static double randomizeFromParent (double value, double mutability) {
		double var = value * mutability;
		return randomRange (value + var, value - var);
	}
	
	public static boolean developTrait (boolean hasTrait, double mutability) {
		double var = rnd.nextDouble();
		//System.out.println(var);
		if (var <= mutability) {
			return !hasTrait;
		}
		return hasTrait;
	}
	
}
