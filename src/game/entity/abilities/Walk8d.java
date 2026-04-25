package game.entity.abilities;

import game.Room;
import game.entity.Entity;
import game.entity.movement.MoveSet;
import game.entity.movement.Moves;

public class Walk8d extends Ability {
	
	private boolean lIsIdle = true;

	@Override
	public void update(Entity e, MoveSet m) throws IllegalArgumentException {
		if (m.getMoveType() != Moves.eightDirectional) {throw new IllegalArgumentException();}
		if (m.getIsIdle() || Room.pause) {e.getAnimator().setAnim(e.getImg(), m, 0, 2, 12);}
		else {e.getAnimator().setAnim(e.getImg(), m, 16, 4, 12, this.lIsIdle != m.getIsIdle());}
		this.lIsIdle = m.getIsIdle();
	}
	
	@Override
	public void setActive(boolean activate) {}

	@Override
	public boolean isActive() {return true;}
	
	@Override
	public Ability get() {return new Walk8d();}
	@Override
	public Abilities getType() {return Abilities.WALK_EIGHT_DIR;}

}
