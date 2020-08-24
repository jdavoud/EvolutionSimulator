import java.awt.Graphics2D;

public class TextComponent extends PanelComponent {
	private String text;
	
	public TextComponent (String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
		
	}
	@Override
	public void draw(Graphics2D g) {
		g.drawString(text, (int) x, (int) y);
		
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
