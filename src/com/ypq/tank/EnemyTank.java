package com.ypq.tank;
import java.awt.*;
import java.util.*;

public class EnemyTank extends Tank {
	private static final Color COLOR = Color.BLUE;
	public EnemyTank(int x, int y, boolean good, TankClient tc) {
		super(x, y, COLOR, good, tc);
		random = new Random(new Date().getTime());
	}
	
	protected static Random random = null;
	protected static final int MINTIME = 1, MAXTIME = 2;		//按照某一方向最多走和最少走的时间
	protected static final int HIT = 1, ALL = 15;				//敌对坦克发子弹的概率为HIT/ALL
	protected static final int AIHIT = 3, AIALL = 4;			//AIHIT/ALL表明AI的强度,代表有多少概率向着己方坦克移动
	protected int step = 0;
	
	//计算AI的方向和发射子弹
	public void calDir() {
		/**
		 * 如果某一个方向的步数已经结束,重新寻找方向并且计算步数
		 */
		if(step == 0) {
			step = (MINTIME + random.nextInt(MAXTIME - MINTIME))*1000/TankClient.FRESHTIME;
			
			Direction dirs[] = Direction.values();
			Direction leftUp[] = {Direction.LEFT, Direction.UP};
			Direction rightUp[] = {Direction.RIGHT, Direction.UP};
			Direction rightDown[] = {Direction.RIGHT, Direction.DOWN};
			Direction leftDown[] = {Direction.LEFT, Direction.DOWN};
			
			if(!tc.myTankList.isEmpty()) {
				if(random.nextInt(AIALL) < AIHIT) {
					MyTank mt = tc.myTankList.get(0);
					if(mt.x <= x && mt.y <= y) {		//如果属于左上方向,随机选择左方向或者上方向
						dir = leftUp[random.nextInt(leftUp.length)];
					}
					else if(mt.x >= x && mt.y <= y) {	//如果属于右上方向,随机选择右方向或者上方向
						dir = rightUp[random.nextInt(rightUp.length)];
					}
					else if(mt.x >= x && mt.y >= y) {	//如果属于右下方向,随机选择右方向或者下方向
						dir = rightDown[random.nextInt(rightDown.length)];
					}
					else if(mt.x <= x && mt.y >= y) {	//如果属于左下方向,随机选择左方向或者下方向
						dir = leftDown[random.nextInt(leftDown.length)];
					}
				}
				else {
					dir = dirs[random.nextInt(dirs.length)];
					
				}
				if(dir != Direction.STOP)
					BarrelDir = dir;
			}
		}
		else
			step--;
		
		if(random.nextInt(ALL) >= (ALL - HIT))
			fire(good);
	}
	
	public static EnemyTank newRandomEnemyTank(TankClient tc) {
		while(true) {
			EnemyTank t = new EnemyTank((int)(Math.random()*TankClient.WIDTH), (int)(Math.random()*TankClient.HEIGHT), false, tc);
			if(!t.detectHitTank() && !t.detectHitWall())		//保证新建的坦克撞不到墙或者其他坦克
				return t;
			else
				System.out.println("发现重复坐标!");
				
		}
	}
}
