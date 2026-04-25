package game.entity.movement;

import game.entity.*;
import game.util.*;

// Legacy version without procedural animation
public class PlatformerSimpleMove extends MoveSet {
	
	Point coords = new Point();
	private Rect xywh;
	private float impulse, friction, speed, accelerationY;
	
	public PlatformerSimpleMove() {
		this.xywh = new Rect(0, 0, 9, 13);
		this.impulse = 17; this.friction = 0.8f;
		this.speed = 2.5f; this.accelerationY = 1;
		// FOR WHEN ANIMATION IS NEEDED, battAnimSpeed = 5;
	}
	
//	public PlatformerMove(Obstacle xywh, Obstacle bg) {
//		this.xywh = xywh; this.bg = bg;
//	}

	@Override
	public void move(Entity c) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public MoveSet get() {
		// TODO Auto-generated method stub
		return new PlatformerSimpleMove();
	}
	@Override
	public float getX() {return this.xywh.getX();}
	@Override
	public float getY() {return this.xywh.getY();}
	@Override
	public float getW() {return this.xywh.getW();}
	@Override
	public float getH() {return this.xywh.getH();}
	@Override
	public float getS() {return 0;}
	@Override
	public float getSW() {return this.xywh.getW();}
	@Override
	public float getSH() {return this.xywh.getH();}

	@Override
	public Point getPoint() {
		// TODO Auto-generated method stub
		return new Point();
	}

	@Override
	public Moves getMoveType() {
		// TODO Auto-generated method stub
		return Moves.platformer;
	}

	@Override
	public void setDir(int d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIdle(boolean i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDir() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean dirChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getIsIdle() {
		// TODO Auto-generated method stub
		return false;
	}


}
