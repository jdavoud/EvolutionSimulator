import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Method;

public class Board {
	
	Main main;
	int rows, cols;
	
	int[] boardRows, boardCols;
	
	UpdateString[][] elts;
	public String title = "";
	
	public Board(Main main, int rows, int cols) {
		this.main = main;
		this.rows = rows;
		this.cols = cols;

		elts = new UpdateString[rows][cols];
		boardRows = new int[rows];
		boardCols = new int[cols];
		
		defineBoardCoords();
		
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Main.board_width, Main.board_height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, Main.board_width, Main.board_height);
		
		g.drawString (title, getCentered(title), 20);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (elts[i][j] != null) {
					elts[i][j].draw(g);
				}
			}
		}
	}
	
	private int getCentered(String titleString) {
		return 365;
	}

	private void defineBoardCoords() {
		
		for (int i = 0; i < boardRows.length; i++) {
			boardRows[i] = 40 + 20 * i;
		}
		int ySpacing = (Main.board_width - 40) / boardCols.length; 
		for (int i = 0; i < boardCols.length; i++) {
			boardCols[i] = 20 + ySpacing * i;
		}
		/*
		for (int i = 0; i < boardCols.length; i++) {
			boardCols[i] = 20 + 20 * i;
		}
		int ySpacing = (Main.board_width - 40) / boardRows.length; 
		for (int i = 0; i < boardRows.length; i++) {
			boardRows[i] = 20 + ySpacing * i;
		}*/
	}
	
	public void addString(int x, int y, String prefix, Method m, Object o) {
		elts[y][x] = new UpdateString(boardCols[x], boardRows[y], prefix, m, o);
	}
	
	
	public void setTitle(String s) {
		title = s;
	}
}


