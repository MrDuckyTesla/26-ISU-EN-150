package game.entity;

import java.util.ArrayList;
import processing.core.PImage;
import game.util.*;
import game.entity.trigger.*;
import game.entity.movement.*;
import game.Room;
import game.entity.abilities.Ability;

public abstract class Entity implements Comparable<Entity> {
	
	// Static variables
	public static int state = 0;
	public static final int[] hurtColor = new int[] {255, 0, 0, 255, 0, 0, 255, 200, 0};
	private static long id = 0;
	
	// Instance variables
	private ArrayList<Integer> colorLists;
	private boolean isTangible, isBreakable;
	private int[][] colorLayer;
	private Ability[] ability;
	private int[] colorTints;
	private PImage image;
	private MoveSet moveset;
	private Animator anim;
	public Room currRoom;
	private Point showXY;
	public long entID;
	private int hash;
	
	// THIS CLASS WILL BE ABSTRACT AND ONLY CONTAIN NECESSARY VARIABLES AND FUNCTIONS THAT APPLY TO ALL CHARACTERS 
	
	public Entity(Room room, PImage img, MoveSet move, Ability[] abilities, int[][] colorLayers, int[] colorTints, boolean isTangible, boolean isBreakable) {  // Allow pre-computing color list outside class and using it here in constructor
		this.entID = Entity.id; Entity.id++; this.showXY = move.getPoint(); anim = new Animator(); this.currRoom = room; this.hash = 0;
		try {
			this.ability = new Ability[abilities.length]; this.image = img.copy(); this.moveset = move.get();
			for (int i = 0; i < abilities.length; i++) {this.ability[i] = abilities[i].get();} 
			this.colorLayer = colorLayers; this.colorTints = colorTints;
			
			if (this.colorLayer.length != 0) {
				this.colorLists = ToolKit.PreCompile(ToolKit.getApp(), this.image, this.colorLayer);
				ToolKit.changeColor(ToolKit.getApp(), this.image, this.colorLists, this.colorTints);
			}
		} catch (NullPointerException e) {
			this.moveset = new ObjectAffectedMove(move.getX(), move.getY(), move.getW(), move.getH());
			this.ability = new Ability[0]; this.isTangible = isTangible; this.isBreakable = isBreakable;
		}
	}
	
	public void update() {
		this.moveset.move(this, this.ability);
	}
	
	public void show() {if (this.anim.canAnimate()) {this.anim.update(this.showXY);}}
	
	public int getHash() {return this.hash;}
	public void setHash() {this.hash = ToolKit.hash((int)(this.getRX()/Room.CHUNK_SIZE), (int)(this.getRY()/Room.CHUNK_SIZE));}

	public boolean isTangible() {return this.isTangible;}
	public boolean isBreakable() {return this.isBreakable;}
	
	// Abstract methods
	public abstract void interact(Trigger t);
	public abstract Trigger getTrigger();
	public abstract Entities getType();
	public abstract boolean isDelete();
	public abstract boolean isMarked();
	
	// Getter methods
	public boolean getOverState() {return this.moveset.getIsIdle();}
	public int getOverDir() {return this.moveset.getDir();}
	public float getRX() {return this.getMoveSet().getX();}
	public float getRY() {return this.getMoveSet().getY();}
	public float getX() {return this.showXY.getX();}
	public float getY() {return this.showXY.getY();}
	public float getW() {return this.getMoveSet().getSW();}
	public float getH() {return this.getMoveSet().getSH();}
	public Room getRoom() {return this.currRoom;}
	public Animator getAnimator() {return this.anim;}
	public ArrayList<Entity> getRoomList() {return this.currRoom.getRoom(this);} // GET ONLY WHAT IS AROUND ENTITY
	public ArrayList<Integer> getColorList() {return colorLists;}
	public int getRW() {return this.currRoom.getImageWidth();}
	public int getRH() {return this.currRoom.getImageHeight();}
	public float[] getXYWH() {return new float[] {this.getRX(), this.getRY(), this.getW(), this.getH()};}
	public Point getPotential() {return this.getMoveSetType() == Moves.eightDirectional? ((EightDirectionalMove) this.getMoveSet()).getMoveDist() : new Point();}
	public Point getXY() {return this.getMoveSet().getPoint();}
	public MoveSet getMoveSet() {return this.moveset;}
	public PImage getImg() {return this.image;};
	public Ability[] getAbilities() {return this.ability;}
	public Moves getMoveSetType() {return this.moveset.getMoveType();}
	
	// Setter methods
	public void setOverState(boolean isIdle) {this.moveset.setIdle(isIdle);}
	public void setOverDir(int dir) {this.moveset.setDir(dir);}
	public void setX(float x) {this.showXY.setX(x);}
	public void setY(float y) {this.showXY.setY(y);}
	public void setXY(float x, float y) {this.showXY.setX(x); this.showXY.setY(y);}
	
	// Adder methods
	public void addX(float x) {this.showXY.addX(x);}
	public void addY(float y) {this.showXY.addY(y);}
	public void addXY(float x, float y) {this.showXY.addX(x); this.showXY.addY(y);}
	
	// Overridden methods
	@Override
	public int compareTo(Entity e) {return Float.compare(this.getRY() + this.getH(), e.getRY() + e.getH());}
	
	@Override
	public String toString() {return "("+this.getRX()+", "+this.getRY() + ", "+this.getW()+", "+this.getH()+")"+", Type \""+this.getType()+"\"";}
	@Override
	public boolean equals(Object other) {if(other.getClass() != this.getClass()) {return false;} return this.entID == ((Entity) other).entID;}

}
