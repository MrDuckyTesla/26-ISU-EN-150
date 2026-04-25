package game.entity.abilities;

import game.entity.*;
import game.entity.movement.*;
import game.entity.trigger.*;
import game.util.*;

public class Interact8d extends Ability {
	
	private boolean isActive = false;
	private Trigger currTrig = null;

	public Interact8d() {}
	public Interact8d(int key) {super(key);}
	public Interact8d(int[] keys) {super(keys);}
	
	@Override
	public Trigger getTrigger() {return this.currTrig;}

	@Override
	public void update(Entity e, MoveSet m) throws IllegalArgumentException {
		if (m.getMoveType() != Moves.eightDirectional) {throw new IllegalArgumentException();}
		if (this.getKeys() != null) {
			this.isActive = false;
			for (int key : this.getKeys()) {if (ToolKit.keyIsDown(key)) {this.isActive = true;}}
		} this.currTrig = this.isActive? Interaction.createInteraction(e.getRoom(), m, e, Triggers.INTERACT) : null;
	}
	
	@Override
	public void setActive(boolean activate) {this.isActive = activate;}
	@Override
	public boolean isActive() {return this.isActive;}
	@Override
	public Ability get() {return this.getKeys() == null? new Interact8d() : new Interact8d(this.getKeys());}
	@Override
	public Abilities getType() {return Abilities.INTERACT_EIGHT_DIR;}

}
