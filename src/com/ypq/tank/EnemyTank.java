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
	protected static final int MINTIME = 1, MAXTIME = 2;		//����ĳһ��������ߺ������ߵ�ʱ��
	protected static final int HIT = 1, ALL = 15;				//�ж�̹�˷��ӵ��ĸ���ΪHIT/ALL
	protected static final int AIHIT = 3, AIALL = 4;			//AIHIT/ALL����AI��ǿ��,�����ж��ٸ������ż���̹���ƶ�
	protected int step = 0;
	
	//����AI�ķ���ͷ����ӵ�
	public void calDir() {
		/**
		 * ���ĳһ������Ĳ����Ѿ�����,����Ѱ�ҷ����Ҽ��㲽��
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
					if(mt.x <= x && mt.y <= y) {		//����������Ϸ���,���ѡ����������Ϸ���
						dir = leftUp[random.nextInt(leftUp.length)];
					}
					else if(mt.x >= x && mt.y <= y) {	//����������Ϸ���,���ѡ���ҷ�������Ϸ���
						dir = rightUp[random.nextInt(rightUp.length)];
					}
					else if(mt.x >= x && mt.y >= y) {	//����������·���,���ѡ���ҷ�������·���
						dir = rightDown[random.nextInt(rightDown.length)];
					}
					else if(mt.x <= x && mt.y >= y) {	//����������·���,���ѡ����������·���
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
			if(!t.detectHitTank() && !t.detectHitWall())		//��֤�½���̹��ײ����ǽ��������̹��
				return t;
			else
				System.out.println("�����ظ�����!");
				
		}
	}
}
