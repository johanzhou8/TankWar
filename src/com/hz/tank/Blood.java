package com.hz.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Blood {
	int x, y, w, h;
	TankClient tc;
		int step =0;
	private boolean live = true;
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}


	
	private int[][] pos= {
			{350,305},{350,310},{350,315},{350,320},{350,315},{350,310},{350,305}
	};
	
	public Blood(){
		x = pos[0][0];
		y = pos[0][1];
		w = 15;
		h = 15;
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}

	private void move() {
		
		if(step == pos.length) step = 0;
		x = pos[step][0];
		y = pos[step][1];
		step++;
		
		
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, y);
	}
}
