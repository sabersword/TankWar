package com.ypq.tank;
import java.awt.Color;
import java.awt.event.KeyEvent;


public class MyTank extends Tank {
	boolean isLeftKey = false, isUpKey = false, isRightKey = false, isDownKey = false;    	//记录四个方向键的按下状态,false为未按下
	private static final Color COLOR = Color.RED;
	public MyTank(int x, int y, boolean good, TankClient tc) {
		super(x, y, COLOR, good, tc);
	}
	
	//方向键按下响应事件
	public void arrowPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP) isUpKey = true;
		else if(keyCode == KeyEvent.VK_DOWN) isDownKey = true;
		else if(keyCode == KeyEvent.VK_LEFT) isLeftKey = true;
		else if(keyCode == KeyEvent.VK_RIGHT) isRightKey = true;
		else if(keyCode == KeyEvent.VK_SPACE) {
			fire(good);
		}
	}
	
	//方向键松开响应事件
	public void arrowReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP) isUpKey = false;
		else if(keyCode == KeyEvent.VK_DOWN) isDownKey = false;
		else if(keyCode == KeyEvent.VK_LEFT) isLeftKey = false;
		else if(keyCode == KeyEvent.VK_RIGHT) isRightKey = false;
	}
	
	public void calDir() {
		if(isLeftKey && !isUpKey && !isRightKey && !isDownKey) {dir = Direction.LEFT;BarrelDir = Direction.LEFT;}
		else if(isLeftKey && isUpKey && !isRightKey && !isDownKey) {dir = Direction.LEFTUP;BarrelDir = Direction.LEFTUP;}
		else if(!isLeftKey && isUpKey && !isRightKey && !isDownKey) {dir = Direction.UP;BarrelDir = Direction.UP;}
		else if(!isLeftKey && isUpKey && isRightKey && !isDownKey) {dir = Direction.RIGHTUP;BarrelDir = Direction.RIGHTUP;}
		else if(!isLeftKey && !isUpKey && isRightKey && !isDownKey) {dir = Direction.RIGHT;BarrelDir = Direction.RIGHT;}
		else if(!isLeftKey && !isUpKey && isRightKey && isDownKey) {dir = Direction.RIGHTDOWN;BarrelDir = Direction.RIGHTDOWN;}
		else if(!isLeftKey && !isUpKey && !isRightKey && isDownKey) {dir = Direction.DOWN;BarrelDir = Direction.DOWN;}
		else if(isLeftKey && !isUpKey && !isRightKey && isDownKey) {dir = Direction.LEFTDWON;BarrelDir = Direction.LEFTDWON;}
		else if(!isLeftKey && !isUpKey && !isRightKey && !isDownKey) dir = Direction.STOP;
	}
	
	public static MyTank newRandomMyTank(TankClient tc) {
		while(true) {
			MyTank t = new MyTank((int)(Math.random()*TankClient.WIDTH), (int)(Math.random()*TankClient.HEIGHT), true, tc);
			if(!t.detectHitTank() && !t.detectHitWall()) 		//保证新建的坦克撞不到墙或者其他坦克
				return t;
		}
	}
}
