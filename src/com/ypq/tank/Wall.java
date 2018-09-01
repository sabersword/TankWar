package com.ypq.tank;
import java.awt.*;


public class Wall {
	private int x, y, width, height;
	
	public Wall(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public void drawWall(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}
}
