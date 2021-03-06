package com.hz.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;

public class Tank {

	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	private int x, y;// 位置
	private int oldX, oldY;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private boolean live = true;
	private int life = 100;

private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] images = {  //don't have to load images every time using static 
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),//reflection
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),

	};
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	static{//static block 静态的语句块，可以进行赋值等function的操作。初始化时运行。
		imgs.put("L", images[0]);
		imgs.put("LU", images[1]);
		imgs.put("U", images[2]);
		imgs.put("RU", images[3]);
		imgs.put("R", images[4]);
		imgs.put("RD", images[5]);
		imgs.put("D", images[6]);
		imgs.put("LD", images[7]);
	}
	
	public void setLife(int life) {
		this.life = life;
	}

	public int getLife() {
		return life;
	}
	private BloddBar bb = new BloddBar();
	
	private static Random r = new Random();

	TankClient tc;

	private boolean good;

	private boolean bL = false, bU = false, bR = false, bD = false;// 记录按键状态

//	enum Direction {
//		L, LU, U, RU, R, RD, D, LD, STOP
//	}// 枚举类型

	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;// pt 炮筒方向

	private int step = r.nextInt(12) + 3;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;

	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.dir = dir;
	}

	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;

		}
		if(good){
			bb.draw(g);
		}
//		Color c = g.getColor();// g 是前景色
//		if (good) {
//			g.setColor(Color.RED);
//			bb.draw(g);
//		} else {
//			g.setColor(Color.BLUE);
//		}
//
//		g.fillOval(x, y, WIDTH, HEIGHT);// x,y为左顶角，后面两个函数为宽和高
//		g.setColor(c);// 还回去
		
		
		
		switch (ptDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);

			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);

			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);

			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);

			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);

			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}

		move();
	}

	void move() {
		this.oldX = x;
		this.oldY = y;

		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		if (this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}

		if (x < 0)
			x = 0;
		if (y < 20)
			y = 20;
		if (x + Tank.WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

		if (!good) {
			if (step == 0) {
				step = r.nextInt(12) + 3;
				Direction[] dirs = Direction.values();
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];

			}
			step--;
			if (r.nextInt(40) > 35) {
				this.fire();
			}

		}

	}

	private void stay() {
		x = oldX;
		y = oldY;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();// 获得按键的code
		switch (key) {
		case KeyEvent.VK_F2:
			if(!this.live){
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_S:
			superFire();
			break;
		case KeyEvent.VK_A:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();// 获得按键的code
		switch (key) {
		// case KeyEvent.VK_CONTROL:
		// fire();
		// break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locateDirection();
	}

	void locateDirection() {// 定位方向
		if (bL && !bU && !bR && !bD)
			dir = Direction.L;
		else if (bL && bU && !bR && !bD)
			dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)
			dir = Direction.U;
		else if (!bL && bU && bR && !bD)
			dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)
			dir = Direction.R;
		else if (!bL && !bU && bR && bD)
			dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)
			dir = Direction.D;
		else if (bL && !bU && !bR && bD)
			dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
	}

	public Missile fire() {
		if (!live)
			return null;
		int x = this.x + images[0].getWidth(null) / 2 - Missile.WIDTH / 2;
		int y = this.y + images[0].getHeight(null)/ 2 - Missile.HEIGHT / 2;

		Missile m = new Missile(x, y, good, ptDir, tc);

		tc.missiles.add(m);
		return m;
	}

	public Missile fire(Direction dir) {
		if (!live)
			return null;
		int x = this.x + images[0].getWidth(null) / 2 - Missile.WIDTH / 2;
		int y = this.y + images[0].getHeight(null)/ 2 - Missile.HEIGHT / 2;

		Missile m = new Missile(x, y, good, dir, tc);

		tc.missiles.add(m);
		return m;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public boolean collideWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}

	public boolean collideTank(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive()
						&& this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}

	private void superFire() {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < 8; i++) {
			tc.missiles.add(fire(dirs[i]));
		}
	}
	
	
	private class BloddBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, images[0].getWidth(null), 10);
			int w = images[0].getWidth(null) * life /100 ;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
			
		}
	}
	
	public boolean eat(Blood b){
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
		
	}
}
