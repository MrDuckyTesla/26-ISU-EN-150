package game.entity.movement;

import game.Room;
import game.entity.*;
import game.entity.trigger.*;
import game.util.*;
import processing.core.PApplet;

public class EightDirectionalMove extends MoveSet {
	
	private boolean isIdle = true, forceWalk = false, canChange = true;
	private int dir = 0, ldir = 0, fdir = -1;
	private float maxSpeed, currSpeed, scale = 1;  // How much object is allowed to move in a frame
	private Point totalDist;  // The total amount that the object has moved in a frame
	private Rect xywh;
	
	public EightDirectionalMove() {this.instantiate(new Rect(0, 0, 28, 28), 3, 3);}
	public EightDirectionalMove(Rect xywh, float speed) {this.instantiate(xywh, speed, 1);}
	public EightDirectionalMove(Rect xywh, float speed, float scale) {this.instantiate(xywh, speed, scale);}
	
	private void instantiate(Rect xywh, float speed, float scale) {this.xywh = xywh; this.totalDist = new Point(); this.maxSpeed = speed*2; this.currSpeed = speed; this.scale = scale;}

	@Override
	public void move(Entity c) {
		if (Room.pause) {this.isIdle = true;}
		Point p; Trigger t = c.getTrigger();
		if (t != null && t.getTriggerType() == Triggers.DELETE) {
			setForceWalk(true); setCanChange(false); this.setDoubSpeed(); 
			this.fdir = this.fdir == -1? t.getCastDir() : this.fdir;
			p = this.getPotential(this.fdir);
		} else {p = this.getPotential();}
		this.totalDist = this.xywh.getPoint();
		for (Entity e : c.getRoomList()) {
			if (e != c && e.isTangible()) {  // If o isn't c and o is tangible, then if c collides with o
				if (ToolKit.rectRectCollide(this.xywh.getX()+p.getX(), this.xywh.getY() + p.getY(), this.getSW(), this.getSH(), e.getRX(), e.getRY(), e.getW(), e.getH())) {
					if (p.getX() < 0) {if (this.setX(e.getRX() + e.getW() + 0.0001f)) {p.resetX();}}
					else if (p.getX() > 0) {if (this.setX(e.getRX() - c.getW() - 0.0001f)) {p.resetX();}}
					if (p.getY() < 0) {if (this.setY(e.getRY() + e.getH() + 0.0001f)) {p.resetY();}}
					else if (p.getY() > 0) {if (this.setY(e.getRY() - c.getH() - 0.0001f)) {p.resetY();}}
				} 
			} 
		} if (ToolKit.nRectRectCollide(this.xywh.getX()+p.getX(), this.xywh.getY() + p.getY(), this.getSW(), this.getSH(), 0, 0, c.getRW(), c.getRH())) {
			if (p.getX() < 0) {if (this.setX(0.0001f)) {p.resetX();}}
			else if (p.getX() > 0) {if (this.setX(c.getRW() - c.getW() - 0.0001f)) {p.resetX();}}
			if (p.getY() < 0) {if (this.setY(0.0001f)) {p.resetY();}}
			else if (p.getY() > 0) {if (this.setY(c.getRH() - c.getH() - 0.0001f)) {p.resetY();}}
		} this.isIdle = p.isZero() && !this.forceWalk; this.ldir = this.dir; this.xywh.addXY(p); this.setNormSpeed();
		this.totalDist.subXY(this.xywh.getPoint()); this.totalDist.negatePoint();
	}
	
	@Override
	public MoveSet get() {return new EightDirectionalMove(this.xywh.get(), this.maxSpeed/2, this.scale);}
	@Override
	public float getX() {return this.xywh.getX();}
	@Override
	public float getY() {return this.xywh.getY();}
	@Override
	public float getW() {return this.xywh.getW();}
	@Override
	public float getH() {return this.xywh.getH();}
	@Override
	public float getS() {return this.scale;}
	@Override
	public float getSW() {return this.xywh.getW() * this.scale;}
	@Override
	public float getSH() {return this.xywh.getH() * this.scale;}
	@Override
	public Point getPoint() {return this.xywh.getPoint();}

	@Override
	public Moves getMoveType() {return Moves.eightDirectional;}
	
	private Point getPotential(int dir) {
		
		if (Room.pause) {return new Point();}
		
		Point s = new Point();
		if (this.isIdle) {return s;} float speed = this.currSpeed;
		if (dir % 2 == 1) {speed *= 0.7071068f;}  // sin 45
		if (dir % 4 != 2) {s.setX(dir % 7 < 2? speed : -speed);}
		if (dir % 4 != 0) {s.setY(dir < 4? speed : -speed);}
		return s;
	}
	private Point getPotential() {return this.getPotential(this.dir);}
	
	public Point getMoveDist() {return this.totalDist;}
	
	public void halfSpeed() {this.currSpeed = this.currSpeed/2;}
	public void doubSpeed() {this.currSpeed = Math.min(this.maxSpeed, this.currSpeed*2);}
	public void setHalfSpeed() {this.currSpeed = this.maxSpeed/4;}
	public void setNormSpeed() {this.currSpeed = this.maxSpeed/2;}
	public void setDoubSpeed() {this.currSpeed = this.maxSpeed;}
	
	private boolean setX(float x) {if (this.setHelper(x, true)) {this.xywh.setX(x); return true;} return false;}
	private boolean setY(float y) {if (this.setHelper(y, false)) {this.xywh.setY(y); return true;} return false;}
	private boolean setHelper(float x, boolean isX) {return PApplet.abs((isX? this.xywh.getX() : this.xywh.getY()) - x) <= this.maxSpeed;}
	
	public void showHitBox() {
		ToolKit.pushApp();
		if (isIdle) {ToolKit.fillApp(255, 0, 0);}
		ToolKit.getApp().rect(xywh.getX(), xywh.getY(), this.getSW(), this.getSH());
		ToolKit.popApp();
	}
	
	public void setForceWalk(boolean forceWalk) {this.forceWalk = forceWalk;}
	public void setCanChange(boolean canChange) {this.canChange = canChange;}
	
	@Override
	public void setDir(int d) {if (this.canChange && !Room.pause) {this.dir = d;}}
	@Override
	public void setIdle(boolean i) {if (!this.forceWalk) {this.isIdle = i;}}
	@Override
	public int getDir() {return this.dir;}
	@Override
	public boolean dirChanged() {return this.dir == this.ldir;}
	@Override
	public boolean getIsIdle() {return this.isIdle;}

}
