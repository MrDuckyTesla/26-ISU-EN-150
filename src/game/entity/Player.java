package game.entity;

import game.Room;
import game.entity.abilities.Ability;
import game.entity.movement.*;
import game.entity.trigger.Trigger;
import game.util.ToolKit;
import processing.core.PImage;

public class Player extends Entity {
	
	public Player(Room room, PImage img, MoveSet move, Ability[] abilities, int[][] colorLayers, int[] colorTints) {
		super(room, img, move, abilities, colorLayers, colorTints, false, false); room.setPlayer(this);
	}

	@Override
	public void update() {
		super.update();
		
		if (this.getMoveSet().getMoveType() == Moves.eightDirectional) {
		    if (ToolKit.keyIsDown(68) || ToolKit.keyIsDown(39) || ToolKit.keyIsDown(83) || ToolKit.keyIsDown(40) || ToolKit.keyIsDown(65) || ToolKit.keyIsDown(37) || ToolKit.keyIsDown(87) || ToolKit.keyIsDown(38)) {this.setOverState(false);}
		    else {this.setOverState(true);}
		    // Check direction
		    if ((ToolKit.keyIsDown(68) || ToolKit.keyIsDown(39)) && (ToolKit.keyIsDown(83) || ToolKit.keyIsDown(40))) {this.setOverDir(1);}       // Walk Right - Down
		    else if ((ToolKit.keyIsDown(83) || ToolKit.keyIsDown(40)) && (ToolKit.keyIsDown(65) || ToolKit.keyIsDown(37))) {this.setOverDir(3);}  // Walk Down - Left
		    else if ((ToolKit.keyIsDown(65) || ToolKit.keyIsDown(37)) && (ToolKit.keyIsDown(87) || ToolKit.keyIsDown(38))) {this.setOverDir(5);}  // Left - Up
		    else if ((ToolKit.keyIsDown(87) || ToolKit.keyIsDown(38)) && (ToolKit.keyIsDown(68) || ToolKit.keyIsDown(39))) {this.setOverDir(7);}  // Up - Right
		    else if (ToolKit.keyIsDown(68) || ToolKit.keyIsDown(39)) {this.setOverDir(0);}  // Walk Right
		    else if (ToolKit.keyIsDown(83) || ToolKit.keyIsDown(40)) {this.setOverDir(2);}  // Walk Down
		    else if (ToolKit.keyIsDown(65) || ToolKit.keyIsDown(37)) {this.setOverDir(4);}  // Walk Left
		    else if (ToolKit.keyIsDown(87) || ToolKit.keyIsDown(38)) {this.setOverDir(6);}  // Walk Up
		}
	}

	@Override
	public void interact(Trigger t) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Entities getType() {return Entities.PLAYER;}

	@Override
	public boolean isDelete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMarked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Trigger getTrigger() {
		// TODO Auto-generated method stub
		return null;
	}

}
