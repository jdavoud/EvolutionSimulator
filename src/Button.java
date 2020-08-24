import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.ArrayList;


public class Button extends PanelComponent {
	private Rectangle2D button;
	private String text;
	private Point2D textCoordinates = null;
	private Color buttonColor = Color.BLACK;
	private Color textColor = Color.BLACK;
	private Font font = new Font("Arial",Font.PLAIN, 25);
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	
	public Button(String text, double x, double y, double width, double height){
		this.text = text;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		button = new Rectangle2D.Double (this.x, this.y, this.width, this.height);
	}
	public void setX(double x){
		this.x = x;
		button = new Rectangle2D.Double (this.x, this.y, this.width, this.height);
	}
	public void setY(double y){
		this.y = y;
		button = new Rectangle2D.Double (this.x, this.y, this.width, this.height);
	}
	public void setWidth(double width){
		this.width = width;
		button = new Rectangle2D.Double (this.x, this.y, this.width, this.height);
	}
	public void setHeight(double height){
		this.height = height;
		button = new Rectangle2D.Double (this.x, this.y, this.width, this.height);
	}
	public void setTextCoordinates(Point2D coords){
		this.textCoordinates = coords;
	}
	public void setText(String text){
		this.text = text;
	}
	public void setButtonColor(Color color){
		this.buttonColor = color;
	}
	public void setTextColor(Color color){
		this.textColor = color;
	}
	public void setFont(Font font){
		this.font = font;
	}
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getWidth(){
		return this.width;
	}
	public double getHeight(){
		return this.height;
	}
	public String getText(){
		return this.text;
	}
	public Point2D getTextCoordinates(){
		return this.textCoordinates;
	}
	public Color getButtonColor(){
		return this.buttonColor;
	}
	public Color getTextColor(){
		return this.textColor;
	}
	public Font getFont(){
		return this.font;
	}
	
	public void setButton(double x, double y, double width, double height){
		this.button = new Rectangle2D.Double(x,y,width,height);
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	public Rectangle2D getButton(){
		return new Rectangle2D.Double (this.x,this.y,this.width,this.height);
	}
	
	
	public void addActionListener(ActionListener al) {
		listeners.add(al);
	}
	public void removeActionListner (ActionListener al) {
		listeners.remove(al);
	}
	
	public void performActions () {
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Button(" + text + ") Press");
		for (ActionListener al : listeners) {
			al.actionPerformed(e);
		}
	}
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(buttonColor);
		g.draw(getButton());
		g.setColor(textColor);
		FontMetrics fm = g.getFontMetrics();
		if (textCoordinates == null) {
			double textX = (getX() + (getWidth() - fm.stringWidth(text)) / 2);
			double textY = ((getY() + (getHeight() - fm.getHeight()) / 2) + fm.getAscent());
			textCoordinates = new Point2D.Double(textX, textY);
		}
		g.drawString(getText(), (int) textCoordinates.getX(), (int) textCoordinates.getY());
	}
}