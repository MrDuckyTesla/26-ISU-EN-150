package game.entity.movement;

import java.util.ArrayList;
import game.entity.Entity;
import game.entity.abilities.Ability;
import game.entity.trigger.Trigger;
import game.util.Point;

public abstract class MoveSet {
	
	private ArrayList<Trigger> triggers = new ArrayList<Trigger>();
	
	public abstract void move(Entity e);
	
	public final void move(Entity e, Ability ab) {this.reset(e); ab.update(e, this); this.check(ab);}
	public final void move(Entity e, Ability[] ab) {this.reset(e); for (Ability a : ab) {a.update(e, this); this.check(a);}}
	
	private void reset(Entity e) {this.triggers.clear(); this.move(e);}
	
	private void check(Ability ab) {
		Trigger t = ab.getTrigger();
		if (t != null) {triggers.add(t);}
	}
	
	public final ArrayList<Trigger> getTriggers() {return this.triggers;}
	
	public abstract MoveSet get();
	public abstract float getX();
	public abstract float getY();
	public abstract float getW();
	public abstract float getH();
	public abstract float getS();
	public abstract float getSW();
	public abstract float getSH();
	public abstract Point getPoint();
	public abstract Moves getMoveType();
	
	public abstract void setDir(int d);
	public abstract void setIdle(boolean i);
	
	public abstract int getDir();
	public abstract boolean dirChanged();
	public abstract boolean getIsIdle();

}