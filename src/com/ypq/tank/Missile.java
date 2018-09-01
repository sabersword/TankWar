package com.ypq.tank;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

//import Tank;

public class Missile {
	int x, y;
	Tank.Direction dir;
	private TankClient tc;
	private boolean lived = true, good = true;
	private Color c = null;
	public static final int WIDTH = 10;	//�ӵ��Ŀ��
	public static final int HEIGHT = 10;	//�ӵ��ĸ߶�
	private static final int SPEED = 10;

	public Missile(int x, int y, Tank.Direction dir, boolean good, Color c, TankClient tc) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.good = good;
		this.c = c;
		this.tc = tc;
	}
	
	public void draw(Graphics g, Iterator<Missile> j) {
		move(j);
		
		Color c = g.getColor();
		g.setColor(this.c);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);	
		
		hitTankList(tc.enemyTankList);
		hitTankList(tc.myTankList);
		hitWallList(tc.wallList);
	}
	
	public void hitTankList(List tankList) {
		Iterator<Tank> i = tankList.iterator();
		while(i.hasNext()) {
			Tank t = i.next();
			if(isHitTank(t)) {		//����ӵ�����̹��,ʹ�ӵ���̹�˾�����
				this.lived = false;
				t.setLived(false);
			}
		}
	}
	
	public void hitWallList(List wallList) {
		Iterator<Wall> i = wallList.iterator();
		while(i.hasNext()) {
			Wall w = i.next();
			if(isHitWall(w)) {
				this.lived = false;
			}
		}
	}

	public void move(Iterator<Missile> j) {
		switch(dir) {
		case LEFT:
			x -= SPEED;
			break;
		case LEFTUP:
			x -= SPEED/1.4;
			y -= SPEED/1.4;
			break;
		case UP:
			y -= SPEED;
			break;
		case RIGHTUP:
			x += SPEED/1.4;
			y -= SPEED/1.4;
			break;
		case RIGHT:
			x += SPEED;
			break;
		case RIGHTDOWN:
			x += SPEED/1.4;
			y += SPEED/1.4;
			break;
		case DOWN:
			y += SPEED;
			break;
		case LEFTDWON:
			x -= SPEED/1.4;
			y += SPEED/1.4;
			break;
		default:
			break;
		}
		if(x < 0 || y < 0 || x > TankClient.WIDTH || y > TankClient.HEIGHT || !lived) {		//����ӵ��ɳ��߽�����Ѿ����˾��Ƴ�missileList
			j.remove();
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(this.x, this.y, Missile.WIDTH, Missile.HEIGHT);
	}
	
	public boolean isHitTank(Tank t) {
		if(t.lived && this.lived && t.good != this.good && this.getRect().intersects(t.getRect())) {		//����ӵ�����Ӿ�����̹������н���,����Ϊ����
			return true;
		}
		return false;
	}
	
	public boolean isHitWall(Wall w) {
		if(this.lived && this.getRect().intersects(w.getRect())) {
			return true;
		}
		return false;
	}
}
