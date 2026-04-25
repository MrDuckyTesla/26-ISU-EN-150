package game.entity;

import game.Room;
import game.entity.abilities.Ability;
import game.entity.movement.*;
import game.entity.trigger.Trigger;
import game.util.ToolKit;
import processing.core.PImage;

public class Wall extends NonPlayerCharacter{
	
	private float imgX, imgY;
	private int imgS;
	private boolean show = true;

	public Wall(Room room, PImage img, MoveSet move, Ability[] abilities, int[][] colorLayers, int[] colorTints, boolean isTangible, boolean isBreakable) {
		super(room, img, move, abilities, colorLayers, colorTints, isTangible, isBreakable);
	} public Wall(PImage img, float x, float y, float w, float h, float imgX, float imgY, int imgS) {
		super(null, img, new ObjectAffectedMove(x, y, w, h), new Ability[0], null, null, true, true); this.imgX = imgX; this.imgY = imgY; this.imgS = imgS;
	} public Wall(PImage img, float x, float y, float w, float h) {super(null, img, new ObjectAffectedMove(x, y, w, h), new Ability[0], null, null, true, true);}
	public Wall(float x, float y, float w, float h) {super(null, null, new ObjectAffectedMove(x, y, w, h), null, null, null, true, true);}
	
	public Wall(float x, float y, float w, float h, boolean tang, Boolean show) {super(null, null, new ObjectAffectedMove(x, y, w, h), null, null, null, tang, true); this.show= show;}
	
	public void show() {
		if (this.show) {
			if (this.getImg() == null) {
				ToolKit.rectApp(this.getX(), this.getY(), this.getW(),this.getH());
			} else {ToolKit.getApp().image(getImg(), imgX+this.getX(), imgY+this.getY(), getImg().width*imgS, getImg().height*imgS);}
		}
	}
	
	@Override
	public void interact(Trigger t) {
//		TextBox.display("well, well, well... look who it is.");
//		ToolKit.getApp().text("well, well, well... look who it is.", this.getX(), this.getY());
		Room.pause = true;
//		Entity.state = 1;
//		Entity.state = 1;
	}
	
	@Override
	public Entities getType() {return Entities.NON_PLAYER_CHARACTER;}

	@Override
	public boolean isDelete() {return false;}
	
	public void setY(float y) {
		this.imgY += y;
	}

	public void setX(float y) {
		this.imgX += y;
	}
}
