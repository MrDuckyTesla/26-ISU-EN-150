package game.entity.trigger;

import game.entity.*;
import game.util.*;
import game.Room;
import game.entity.movement.MoveSet;

public class Interaction extends Trigger {
	
	public static Interaction createInteraction(Room r, MoveSet m, Entity e, Triggers t) {
		float halfW = m.getSW()/2, halfH = m.getSH()/2;
		int dirInt = m.getDir(); Point xy = new Point();
		if (dirInt % 4 != 2) {xy.setX(dirInt % 7 < 2? halfW : -halfW);}
		if (dirInt % 4 != 0) {xy.setY(dirInt < 4? halfH : -halfH);}
		if (dirInt % 2 == 1) {xy.multpilyXY(0.7071068f);}  // sin 45
		return new Interaction(m.getX() + xy.getX() + halfW/2, m.getY() + xy.getY() + halfH/2, halfW, halfH, e, t);
	}
		
	private Triggers t;

	public Interaction(float x, float y, float w, float h, Entity i, Triggers t) {
		super(x, y, w, h, i); this.t = t;
	}

	@Override
	public void update() {
		for (Entity e : this.getCaster().getRoomList()) {
			if (e.getType() != Entities.TRIGGER && !this.getCaster().equals(e)) {
				if (ToolKit.rectRectCollide (
						this.getRX(), this.getRY(), this.getW(), this.getH(), 
						e.getRX(), e.getRY(), e.getW(), e.getH()
				)) {e.interact(this);}
			}
		}
	}

	@Override
	public Triggers getTriggerType() {return this.t;}

	@Override
	public boolean isDelete() {return true;}

	@Override
	public boolean isMarked() {return false;}
}
