package game.entity;

import game.Room;
import game.entity.abilities.Ability;
import game.entity.movement.MoveSet;
import game.entity.trigger.Trigger;
import processing.core.PImage;

public class MultiStateEntity {
	
	private Entity[] entities;
	private int state = 0;

	public MultiStateEntity(Entities[] types, Room room[], PImage[] img, MoveSet[] move, Ability[][] abilities, int[][][] colorLayers, int[] colorTints, boolean isTangible, boolean isBreakable) {
		entities = new Entity[types.length];
		try {
			for (int i = 0; i < types.length; i++) {entities[i] = getType(types[i], room[i], img[i], move[i], abilities[i], colorLayers[i], colorTints, isTangible, isBreakable);}
		} catch (IndexOutOfBoundsException e) {throw new IllegalArgumentException();}
	}
	
	private Entity getType(Entities type, Room room, PImage img, MoveSet move, Ability[] abilities, int[][] colorLayers, int[] colorTints, boolean isTangible, boolean isBreakable) {
		switch (type) {
			case ENEMY:
				return new Enemy(room, img, move, abilities, colorLayers, colorTints);
			case NON_PLAYER_CHARACTER:
				return new NonPlayerCharacter(room, img, move, abilities, colorLayers, colorTints, isTangible, isBreakable);
			case PLAYER:
				return new Player(room, img, move, abilities, colorLayers, colorTints);
			default:
				return null;
		}
	}
	
	public Entity get() {return entities[this.state];}
	
	public void changeState(Trigger t) {this.state++;}

}
