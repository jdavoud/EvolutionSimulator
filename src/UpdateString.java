import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UpdateString extends PanelComponent {
	String prefix;
	Method m;
	Object o;
	
	public UpdateString (int x, int y, String prefix, Method m, Object o) {
		this.o = o;
		this.prefix = prefix;
		this.m = m;
		this.x = x;
		this.y = y;
	}
	
	public UpdateString (String prefix, Method m, Object o) {
		this.o = o;
		this.prefix = prefix;
		this.m = m;
		x = 0;
		y = 0;
	}
	
	public UpdateString (String prefix) {
		this.prefix = prefix;
	}
	
	public String getString() {
		try {
			if (m != null && o != null) {
				Object obj = m.invoke(o);
				String s;
				if (obj.getClass() == Double.class) {
					s = Main.df.format((Double) obj);
				} else {
					s = String.valueOf(obj);
				}
				return prefix + s;
			} else {
				return prefix;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void draw(Graphics2D g) {
		g.drawString(getString(), (int) x, (int) y);
	}
}