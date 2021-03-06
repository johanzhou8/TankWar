package com.hz.tank;
import java.awt.*;

public class Explode {

	int x, y;
	private boolean live = true;
	private TankClient tc;
	//int[] diameter = { 4, 7, 12, 18, 26, 32, 49, 30, 14, 6 };
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] images = {  //don't have to load images every time using static 
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),//reflection
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
	};
	int step = 0;
	private static boolean init;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		
		if(!init){
			for (int j = 0; j < images.length; j++) {
				g.drawImage(images[j], x, y, null);
			}
			init = true;
		}
		if (!live) {
			tc.explodes.remove(this);
			return;
		}
		if (step == images.length) {
			live = false;
			step = 0;
			return;
		}
		g.drawImage(images[step], x, y, null);
		step++;
	}
}
