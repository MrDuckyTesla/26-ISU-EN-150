package game.entity.attack;

import processing.core.PImage;

public abstract class Attack {

	public Attack() {}
	
	public abstract void update(PImage p, int s);
	
	public abstract boolean offscreen();

}
