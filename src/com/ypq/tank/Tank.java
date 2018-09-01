package com.ypq.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

/**
 * ̹�˸���
 * @author god
 *
 */
public class Tank {
	protected int x, y, oldX = 0, oldY = 0;			//oldX,oldY���ڼ�¼��һ�ε�����
	protected Color color;
	protected boolean good, lived = true;
	protected TankClient tc = null;
	
	enum Direction {LEFT, LEFTUP, UP, RIGHTUP, RIGHT, RIGHTDOWN, DOWN, LEFTDWON, STOP};		//��¼̹�˵ķ���
	Direction dir = Direction.STOP;
	Direction BarrelDir = Direction.RIGHT;
	
	protected static final int SPEED = 5;				//̹���ƶ�����
	public static final int WIDTH = 30;			//̹�˿��
	public static final int HEIGHT = 30;			//̹�˸߶�
	private static final int RANDOMSAFE = 50;	//������ɵ�����Ļ��Ե�İ�ȫ����

	public Tank(int x, int y, Color color, boolean good, TankClient tc) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.good = good;
		this.tc = tc;
	}
	
	//�ػ�̹��,�Ƚ�̹���ƶ����ػ�
	public void drawTank(Graphics g,Iterator iterator) {
		move();
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(x, y, WIDTH, HEIGHT);
		drawBarrel(g);				//������Ͳ
		g.setColor(c);
		popList(iterator);
	}
	
	/**
	 * ������Ͳ,��Ͳ��һ����̹������,��һ�˼����,x,y
	 * @param g ������Ҫ���Ļ���
	 */
	public void drawBarrel(Graphics g) {
		int x = 0, y = 0;
		switch (BarrelDir) {
		case LEFT:
			x = this.x;
			y = this.y + HEIGHT/2;
			break;
		case LEFTUP:
			x = this.x;
			y = this.y;
			break;
		case UP:
			x = this.x + WIDTH/2;
			y = this.y;
			break;
		case RIGHTUP:
			x = this.x + WIDTH;
			y = this.y;
			break;
		case RIGHT:
			x = this.x + WIDTH;
			y = this.y + HEIGHT/2;
			break;
		case RIGHTDOWN:
			x = this.x + WIDTH;
			y = this.y + HEIGHT;
			break;
		case DOWN:
			x = this.x + WIDTH/2;
			y = this.y + HEIGHT;
			break;
		case LEFTDWON:
			x = this.x;
			y = this.y + HEIGHT;
			break;
		default:
			break;
		}
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine(this.x + WIDTH/2, this.y + HEIGHT/2, x, y);
		g.setColor(c);
	}

	/**
	 * ̹�˵��ƶ�����,ÿ��һ��ʱ�����
	 */
	public void move() {
		oldX = x;		//���ƶ�ǰ������һ������
		oldY = y;
		calDir();
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
		case STOP:
			break;
		default:
			break;
		}
		detectHitWall();
		detectHitTank();
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x > TankClient.WIDTH - Tank.WIDTH) x = TankClient.WIDTH - Tank.WIDTH;
		if(y > TankClient.HEIGHT - Tank.HEIGHT) y = TankClient.HEIGHT - Tank.HEIGHT;
	}
	
	//���Ѿ�������̹���Ƴ�����,���Ҳ�����ըЧ��
	public void popList(Iterator<Tank> iterator) {
		if(!this.isLived()) {
			iterator.remove();
			tc.explodeList.add(new Explode(x, y, Color.ORANGE));
		}
	}
	
	//fire������Ҫָ�����ĸ�̹����Ӫ�������ӵ�
	public void fire(boolean good) {
		if(good)				//����̹���Ǻ�ɫ�ӵ�,�з�̹������ɫ�ӵ�
			tc.missileList.add(new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, BarrelDir, good, Color.BLACK, tc));
		else
			tc.missileList.add(new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, BarrelDir, good, Color.BLUE, tc));
	}
	
	//������Ҫ��д�ķ���,�Ա�ȷ��̹�����������Ƶ�
	public void calDir() {}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, Tank.WIDTH, Tank.HEIGHT);
	}
	
	public boolean isLived() {
		return lived;
	}
	public void setLived(boolean lived) {
		this.lived = lived;
	}
	
	public boolean isHitWall(Wall w) {
		return getRect().intersects(w.getRect());
	}
	
	//�����Ƿ���ײ��ǽ��
	public boolean detectHitWall() {
		boolean flag = false;
		Iterator<Wall> i = tc.wallList.iterator(); 
		while(i.hasNext()) {
			if(isHitWall(i.next()) && this.isLived()) {			//�����Ȼ�ж�·������,��Ҫ��next�ٺ���һ������
				x = oldX;
				y = oldY;
				flag = true;
			}
		}
		return flag;
	}
	
	//�����ҷ��͵з�̹������������û��ײ��̹��,����̹�˵�ͼ���Ƿ���̹����ײ
	public boolean detectHitTank() {
		boolean flag = false;
		Iterator<MyTank> myTankIterator = tc.myTankList.iterator(); 
		while(myTankIterator.hasNext()) {
			Tank t = myTankIterator.next();
			if(this != t && isHitTank(t) && t.isLived() && this.isLived()) {
				x = oldX;
				y = oldY;
				flag = true;
			}
		}
		
		Iterator<EnemyTank> enemyTankIterator = tc.enemyTankList.iterator(); 
		while(enemyTankIterator.hasNext()) {
			Tank t = enemyTankIterator.next();
			if(this != t && isHitTank(t) && t.isLived() && this.isLived()) {
				x = oldX;
				y = oldY;
				flag = true;
			}
		}
		return flag;
	}

	//�ж�����̹���Ƿ���ײ
	public boolean isHitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()))
			return true;
		return false;
	}
	
	public static Tank newRandomTank(TankClient tc, boolean good) {
		Tank t = null;
		while(true) {
			int x = (int)(Math.random()*TankClient.WIDTH);
			int y = (int)(Math.random()*TankClient.HEIGHT);
			if(x < RANDOMSAFE)		//��x���д�С����
				x = RANDOMSAFE;
			else if(x > TankClient.WIDTH - RANDOMSAFE)
				x = TankClient.WIDTH - RANDOMSAFE;
			if(y < RANDOMSAFE)		//��y���д�С����
				y = RANDOMSAFE;
			else if(y > TankClient.HEIGHT - RANDOMSAFE)
				y = TankClient.HEIGHT - RANDOMSAFE;
			if(good){
				t = new MyTank(x, y, good, tc);
			}
			else {
				t = new EnemyTank(x, y, good, tc);
			}
				
			if(!t.detectHitTank() && !t.detectHitWall())		//��֤�½���̹��ײ����ǽ��������̹��
				return t;		
		}
	}
	
}
