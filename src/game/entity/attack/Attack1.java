package game.entity.attack;

import eng.Main;
import game.entity.Player;
import game.util.ToolKit;
import processing.core.PImage;

public class Attack1 extends Attack {
	
	public long timeStart;
	public float x, y;
	public Player p;
	public float targetX, targetY, theta = Float.NEGATIVE_INFINITY;
	int speed, wait;

	public Attack1(float x, float y, Player pl, long delay, int wait, int speed) {
		this.timeStart = System.currentTimeMillis() + delay;
		this.x = x;
		this.y = y;
		this.p = pl;
		this.speed = speed;
		this.wait = wait;
	}
	
	public void update(PImage p, int s) {
		long deltaTime = System.currentTimeMillis() - this.timeStart;
		if (deltaTime > 0) {
			ToolKit.pushApp();
			ToolKit.getApp().translate(x, y);
			ToolKit.getApp().rotate((float)(ToolKit.getApp().frameCount)/(speed-10));
			ToolKit.getApp().image(p, -p.width*s/2, -p.height*s/2, p.width*s, p.height*s);
			ToolKit.popApp();
		}
		if (deltaTime > wait) {
			if (theta == Float.NEGATIVE_INFINITY) {theta = (float) Math.atan2(targetY-y, targetX-x);}
			this.x += Math.cos(theta)*speed;
			this.y += Math.sin(theta)*speed;
		}
		else {
			this.targetX = this.p.getRX()+this.p.getW()/2;
			this.targetY = this.p.getRY()+this.p.getH()/2;
		}
		if (ToolKit.circRectCollide(x, y, 100 + 20*(s-3), this.p.getX(), this.p.getY(), this.p.getW(), this.p.getH()) && Main.immuneTill <  System.currentTimeMillis()) {
			ToolKit.getApp().circle(x, y, 100 + 20*(s-3));
			Main.hp -= 5;
			Main.immuneTill = System.currentTimeMillis() + 500;
		}
		
	}
	
	public boolean offscreen() {
		return !ToolKit.circRectCollide(x, y, 100, 0, 0, 800, 800);
	}
	

}
