package game.entity.movement;

import game.entity.Entity;
import game.util.Point;

public class ObjectAffectedMove extends MoveSet {
	
	private float x, y, w, h;
	
	public ObjectAffectedMove() {}
	public ObjectAffectedMove(float x, float y, float w, float h) {this.x = x; this.y = y; this.w = w; this.h = h;}

	@Override
	public void move(Entity e) {
		
	}

	@Override
	public float getX() {return this.x;}
	@Override
	public float getY() {return this.y;}
	@Override
	public float getW() {return this.w;}
	@Override
	public float getH() {return this.h;}
	@Override
	public float getS() {return 1;}
	@Override
	public float getSW() {return this.getW() * this.getS();}
	@Override
	public float getSH() {return this.getH() * this.getS();}
	
	@Override
	public MoveSet get() {
		return new ObjectAffectedMove();
	}
	
	@Override
	public Point getPoint() {
		// TODO Auto-generated method stub
		return new Point(this.x, this.y);
	}

	@Override
	public Moves getMoveType() {
		// TODO Auto-generated method stub
		return Moves.objectAffected;
	}

	@Override
	public void setDir(int d) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean dirChanged() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean getIsIdle() {
		// TODO Auto-generated method stub
		return false;
	}

}
