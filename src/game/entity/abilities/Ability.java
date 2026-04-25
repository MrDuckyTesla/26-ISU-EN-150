package game.entity.abilities;

import game.entity.Entity;
import game.entity.movement.MoveSet;
import game.entity.trigger.Trigger;

public abstract class Ability {
	
	private int[] keys;

	public Ability() {this.keys = null;}
	public Ability(int key) {this.keys = new int[] {key};}
	public Ability(int[] keys) {this.keys = keys;}
	
	public abstract void update(Entity e, MoveSet m) throws IllegalArgumentException;
	
	public abstract void setActive(boolean activate);
	public abstract boolean isActive();
	/**
	 * Only override this if you want to create an interaction
	 * @return Returns the trigger being requested
	 */
	public Trigger getTrigger() {return null;}
	
	public abstract Ability get();
	
	public int[] getKeys() {return this.keys;}
	
	public void setKeys(int key) {this.keys = new int[] {key};}
	public void setKeys(int[] keys) {this.keys = keys;}
	
	public abstract Abilities getType();
}
