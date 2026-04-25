package game.entity.attack;

import eng.Main;
import game.entity.Player;
import game.util.ToolKit;
import processing.core.PImage;

public class Attack2 extends Attack {
	
	public long timeStart;
	public float x, y;
	public Player p;
	public float targetX, targetY;
	int speed, wait;
	boolean rand;

	public Attack2(Player pl, long delay, int wait, int speed, boolean rand) {
		this.timeStart = System.currentTimeMillis() + delay;
		this.p = pl;
		this.speed = speed;
		this.wait = wait;
		this.rand = rand;
	}
	
	public void update(PImage p, int s) {
		long deltaTime = System.currentTimeMillis() - this.timeStart;
		if (deltaTime > 0 && !(deltaTime > wait)) {
			ToolKit.getApp().image(p, x, y, p.width*s, p.height*s);
		}
		if (deltaTime > wait) {
			ToolKit.pushApp();
			ToolKit.getApp().translate(x, y);
			ToolKit.getApp().rotate(-(1 - (targetY - y) / 400) * (float)(Math.PI / 2));
			ToolKit.getApp().image(p, 0, 0, p.width*s, p.height*s);
			ToolKit.popApp();
			
			this.y += speed;
			
		}
		else {
			if (!rand) {
				this.x = this.p.getRX()+p.width/2;
				this.y = this.p.getRY()-p.height-300;
			}
			else {
				if (this.x == 0) {
					this.x = (float) (Math.random()*400+200);
					this.y = this.p.getRY()-p.height-300;
				}
			}
			this.targetX = this.p.getRX()+this.p.getW()/2;
			this.targetY = this.p.getRY()+this.p.getH()/2;
		}
		
		if (ToolKit.circRectCollide(x - p.width/2+20, y-10, 20, this.p.getX(), this.p.getY(), this.p.getW(), this.p.getH()) && Main.immuneTill <  System.currentTimeMillis()) {
			ToolKit.getApp().circle(x - p.width/2+20, y-10, 20);
			Main.hp -= 10;
			Main.immuneTill = System.currentTimeMillis() + 500;
		}
		
		
	}
	
	public boolean offscreen() {
		return !ToolKit.circRectCollide(x, y, 100, 0, 0, 800, 800);
	}
	

}
