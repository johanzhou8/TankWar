import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	
	public static final int GAME_WIDTH = 800;//常量 容易维护
	public static final int GAME_HEIGHT = 600;
	
	Tank myTank = new Tank(50,50,true,this);
	Tank enemyTank = new Tank(100,100,false,this);
	//int x = 50, y = 50;//控制位置
	
	Missile m;
	List<Missile> missiles = new ArrayList<Missile>();
	Image offScreenImage = null;
	public void paint(Graphics g) {//初始化时，paint会被自动调用
		g.drawString("Missiles counts:" + missiles.size(), 10, 50);
		for(int i = 0 ; i< missiles.size();i++){
			m = missiles.get(i);
			m.hitTank(enemyTank);
				m.draw(g);
					}

		myTank.draw(g);
		enemyTank.draw(g);
		
	}
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	public void launchFrame(){
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e) {//重写了父类中的函数
				System.exit(0);
			}
			});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		new Thread(new PaintThread()).start();//启动线程重画
	}
	
	public static void main(String[] args) {
		TankClient tc= new TankClient();
		tc.launchFrame();
	}
	
	private class PaintThread implements Runnable{//线程 由内部类完成

		public void run() {
			while(true){ 
				repaint();//父类中的，内部调用paint方法
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);

		}

	}
}






