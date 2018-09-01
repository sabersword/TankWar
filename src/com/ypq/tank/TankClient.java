package com.ypq.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * 坦克大战的主窗口类
 * @author god
 *
 */
public class TankClient extends Frame{
	
	public List<MyTank> myTankList = null;
	public List<EnemyTank> enemyTankList = null;
	public List<Missile> missileList = null;
	public List<Explode> explodeList = null;
	public List<Wall> wallList = null;
	
	private Image offScreen = null;
	public static final int WIDTH = 800, HEIGHT = 600;
	public static final int FRESHTIME = 50;	//重画线程的睡眠时间
	private static final int INCTANK = 3;		//每次按键增加的坦克数
	
	public void paint(Graphics g) {		
		
		Iterator<MyTank> myTankIterator = myTankList.iterator();
		while(myTankIterator.hasNext()) {
			MyTank mt = myTankIterator.next();
			mt.drawTank(g, myTankIterator);
		}
		
		Iterator<EnemyTank> i = enemyTankList.iterator();
		while(i.hasNext()) {
			Tank t = i.next();
			t.drawTank(g, i);			
		}
		
		Iterator<Missile> j = missileList.iterator();
		while(j.hasNext()) {
			Missile m = j.next();
			m.draw(g, j);			//将迭代器传给Missile类,让类内部删掉出界的子弹
		}
		
		Iterator<Explode> k = explodeList.iterator();
		while(k.hasNext()) {
			Explode e = k.next();
			e.drawExplode(g, k);
		}
		
		Iterator<Wall> l = wallList.iterator();
		while(l.hasNext()) {
			Wall e = l.next();
			e.drawWall(g);
		}
		
		g.drawString("子弹已经发出:" + missileList.size(), 10, 40);
		g.drawString("已经爆炸:" + explodeList.size(), 10, 55);
		g.drawString("剩余敌方坦克:" + enemyTankList.size(), 10, 70);
	}

	public void launchFrame(int width, int height) {
		this.setBounds(50, 50, width, height);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("坦克大战"); 
		this.setBackground(Color.GREEN);
		this.addWindowListener(new WindowMonitor());
		this.addKeyListener(new KeyMonitor());
		new Thread(new PaintThread()).start();
		
		if(myTankList == null)
			myTankList = new LinkedList<MyTank>();
		myTankList.add(new MyTank(50, 50, true, this));
		
		if(enemyTankList == null)
			enemyTankList = new LinkedList<EnemyTank>();
		enemyTankList.add(new EnemyTank(500,500, false, this));
		
		if(missileList == null)
			missileList = new LinkedList<Missile>();

		if(explodeList == null)
			explodeList = new LinkedList<Explode>();
		
		if(wallList == null)
			wallList = new LinkedList<Wall>();
		wallList.add(new Wall(300, 100, 200, 20));
		wallList.add(new Wall(400, 200, 20, 300));
		
	}
	
	private class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			TankClient tc = (TankClient)e.getSource();
			tc.setVisible(false);
			System.exit(0);
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if(!myTankList.isEmpty())
				myTankList.get(0).arrowPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			if(!myTankList.isEmpty())
				myTankList.get(0).arrowReleased(e);
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				for(int i = 0; i < INCTANK; i++)
					//enemyTankList.add(EnemyTank.newRandomEnemyTank(TankClient.this));
					enemyTankList.add((EnemyTank)Tank.newRandomTank(TankClient.this, false));
			}
			if(e.getKeyCode() == KeyEvent.VK_Z) {
				myTankList.add((MyTank)Tank.newRandomTank(TankClient.this, true));
			}
		}
	}
	
	class PaintThread implements Runnable {
		public void run(){
			while(true) {
			repaint();
			try {
				Thread.sleep(FRESHTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		}
	}
	
	public void update(Graphics g) {
		if(offScreen == null) {
			offScreen = this.createImage(WIDTH, HEIGHT); 
		}
		Graphics gOffScreen = offScreen.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreen, 0, 0, null);
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame(WIDTH, HEIGHT);
	}
}
