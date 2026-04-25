package game.entity.abilities;

import game.entity.Entity;
import game.entity.movement.EightDirectionalMove;
import game.entity.movement.MoveSet;
import game.entity.movement.Moves;
import game.util.ToolKit;

//this class ONLY works on objects using EightDirectionalMove()
public class Sprint8d extends Ability {
	
	private boolean activate, isActive = false;
	
	public Sprint8d() {}
	public Sprint8d(int key) {super(key);}
	public Sprint8d(int[] keys) {super(keys);}

	@Override
	public void update(Entity e, MoveSet m) throws IllegalArgumentException {
		if (m.getMoveType() != Moves.eightDirectional) {throw new IllegalArgumentException();}
		if (this.getKeys() != null) {
			this.isActive = false;
			for (int key : this.getKeys()) {if (ToolKit.keyIsDown(key)) {this.isActive = true;}}
		} else {this.isActive = this.activate;}
		if (this.isActive) {
			((EightDirectionalMove) m).doubSpeed();
			e.getAnimator().setAnimSpeed(6);
		}
	}

	@Override
	public void setActive(boolean activate) {this.activate = activate;}
	@Override
	public boolean isActive() {return this.isActive;}
	@Override
	public Ability get() {return this.getKeys() == null? new Sprint8d() : new Sprint8d(this.getKeys());}
	@Override
	public Abilities getType() {return Abilities.SPRINT_EIGHT_DIR;}
	
}
