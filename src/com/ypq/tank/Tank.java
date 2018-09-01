package com.ypq.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

/**
 * 坦克父类
 * @author god
 *
 */
public class Tank {
	protected int x, y, oldX = 0, oldY = 0;			//oldX,oldY用于记录上一次的坐标
	protected Color color;
	protected boolean good, lived = true;
	protected TankClient tc = null;
	
	enum Direction {LEFT, LEFTUP, UP, RIGHTUP, RIGHT, RIGHTDOWN, DOWN, LEFTDWON, STOP};		//记录坦克的方向
	Direction dir = Direction.STOP;
	Direction BarrelDir = Direction.RIGHT;
	
	protected static final int SPEED = 5;				//坦克移动距离
	public static final int WIDTH = 30;			//坦克宽度
	public static final int HEIGHT = 30;			//坦克高度
	private static final int RANDOMSAFE = 50;	//随机生成的与屏幕边缘的安全距离

	public Tank(int x, int y, Color color, boolean good, TankClient tc) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.good = good;
		this.tc = tc;
	}
	
	//重画坦克,先将坦克移动再重画
	public void drawTank(Graphics g,Iterator iterator) {
		move();
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(x, y, WIDTH, HEIGHT);
		drawBarrel(g);				//画出炮筒
		g.setColor(c);
		popList(iterator);
	}
	
	/**
	 * 画出炮筒,炮筒的一端是坦克中心,另一端计算出,x,y
	 * @param g 传递需要画的画笔
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
	 * 坦克的移动方法,每隔一段时间调用
	 */
	public void move() {
		oldX = x;		//在移动前备份上一个坐标
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
	
	//将已经死亡的坦克移出容器,并且产生爆炸效果
	public void popList(Iterator<Tank> iterator) {
		if(!this.isLived()) {
			iterator.remove();
			tc.explodeList.add(new Explode(x, y, Color.ORANGE));
		}
	}
	
	//fire函数需要指明是哪个坦克阵营发出的子弹
	public void fire(boolean good) {
		if(good)				//己方坦克是黑色子弹,敌方坦克是蓝色子弹
			tc.missileList.add(new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, BarrelDir, good, Color.BLACK, tc));
		else
			tc.missileList.add(new Missile(x + Tank.WIDTH/2 - Missile.WIDTH/2, y + Tank.HEIGHT/2 - Missile.HEIGHT/2, BarrelDir, good, Color.BLUE, tc));
	}
	
	//子类需要重写改方法,以便确认坦克是怎样控制的
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
	
	//返回是否碰撞到墙壁
	public boolean detectHitWall() {
		boolean flag = false;
		Iterator<Wall> i = tc.wallList.iterator(); 
		while(i.hasNext()) {
			if(isHitWall(i.next()) && this.isLived()) {			//这个竟然有短路的问题,需要先next再后面一个条件
				x = oldX;
				y = oldY;
				flag = true;
			}
		}
		return flag;
	}
	
	//根据我方和敌方坦克容器查找有没相撞的坦克,返回坦克地图上是否有坦克相撞
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

	//判断两辆坦克是否相撞
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
			if(x < RANDOMSAFE)		//对x进行大小限制
				x = RANDOMSAFE;
			else if(x > TankClient.WIDTH - RANDOMSAFE)
				x = TankClient.WIDTH - RANDOMSAFE;
			if(y < RANDOMSAFE)		//对y进行大小限制
				y = RANDOMSAFE;
			else if(y > TankClient.HEIGHT - RANDOMSAFE)
				y = TankClient.HEIGHT - RANDOMSAFE;
			if(good){
				t = new MyTank(x, y, good, tc);
			}
			else {
				t = new EnemyTank(x, y, good, tc);
			}
				
			if(!t.detectHitTank() && !t.detectHitWall())		//保证新建的坦克撞不到墙或者其他坦克
				return t;		
		}
	}
	
}
