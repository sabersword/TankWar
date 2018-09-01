package com.ypq.tank;
import java.awt.*;
import java.util.*;


public class Explode {
	private int x, y, step = 0;		//x,y为坐标,step指明爆炸第几步
	private Color c = Color.ORANGE;
	private int diameter[] = {4, 8, 15, 25, 39, 45, 30, 10};
	
	public Explode(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public Explode getExplode(int x, int y, Color c) {
		return new Explode(x, y, c);
	}
	
	//绘制爆炸并且判断爆炸是否结束,结束的话移出爆炸队列
	public void drawExplode(Graphics g, Iterator<Explode> k) {
		Color c = g.getColor();
		g.setColor(this.c);
		g.fillOval(x + Tank.WIDTH/2 - diameter[step]/2, y + Tank.WIDTH/2 - diameter[step]/2, diameter[step], diameter[step++]);
		if(step >= diameter.length) {
			k.remove();
		}
		g.setColor(c);
	}
}
