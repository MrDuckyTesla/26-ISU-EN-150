package game;

import java.util.*;

import eng.Main;
import game.entity.*;
import game.entity.attack.*;
import game.menu.TextBox;
import game.util.*;
import processing.core.PImage;

// A Room holds obstacles and by extension characters
public class Room {
	
	public static final int CHUNK_SIZE = 200;
	public static int encounter = 0;
	private int WIDTH_CHUNK, HEIGHT_CHUNK;
	public static boolean pause = false;
	public long startMusic = 0, win= 0;
	

	private HashMap<Integer, ArrayList<Entity>> hash = new HashMap<>();
	private ArrayList<Entity> add, sub, mod, see;  // Lists to keep track of what added, removed, modified and shown
	
	private Player p;
	// BACKGROUND VARIABLES
	private PImage background;
	private Point backCoords;
	
	
	public Attack[] a;
	
	
	public Room(PImage background) {this.instantiate(background, new Point());}
	public Room(int w, int h) {this.instantiate(new PImage(w, h), new Point());}
	public Room(Entity o, PImage background) {this.instantiate(background, new Point()); add.add(o);}
	public Room(Entity[] o, PImage background) {this.instantiate(background, new Point()); this.add(o);}
	public Room(ArrayList<Entity> o, PImage background) {this.instantiate(background, new Point()); this.add(o);}
	
	public void add(Entity e) {add.add(e);}
	public void add(Entity[] l) {for (Entity e : l) {add.add(e);}}
	public void add(Iterable<? extends Entity> l) {for (Entity e : l) {add.add(e);}}
	public void add(float x, float y, float w, float h) {add.add(new Wall(x, y, w, h));}
	public void add(PImage p, float x, float y, float w, float h) {add.add(new Wall(p, x, y, w, h));}
	public void add(PImage p, float x, float y, float w, float h, float px, float py, int s) {add.add(new Wall(p, x, y, w, h, px, py, s));}
	
	public boolean setPlayer(Player p) {if (this.p == null) {this.p = p; add.add(this.p); return true;} return false;}
	
	private void instantiate(PImage background, Point backCoords) {
		this.background = background; this.backCoords = backCoords; 
		this.add = new ArrayList<>(); this.sub = new ArrayList<>(); this.mod = new ArrayList<>();
		if (this.background == null) {this.background = new PImage(ToolKit.getAppWidth(),ToolKit.getAppHeight());}
		this.WIDTH_CHUNK = this.background.width / Room.CHUNK_SIZE; this.HEIGHT_CHUNK = this.background.height / Room.CHUNK_SIZE;
	}
	
	// HASH ADD
	private void addHash(Entity e) {e.setHash(); this.hash.computeIfAbsent(e.getHash(),  k -> new ArrayList<>()).add(e);}
	private void addHash(Iterable<? extends Entity> l) {for (Entity e : l) {this.addHash(e);}}
	
	// HASH REMOVE
	private void removeHash(Entity e, int key) {
		ArrayList<Entity> chunk = hash.get(key); if (chunk != null) {chunk.remove(e); if (chunk.isEmpty()) {hash.remove(key);}}
	} private void removeHash(Entity e) {this.removeHash(e, e.getHash());}
	private void removeHash(Iterable<? extends Entity> l) {for (Entity e : l) {this.removeHash(e);}}
	
//	public boolean setPlayer(Player p) {if (this.p == null) {this.p = p; room.add(this.p); return true;} return false;}
	
	//TODO implement reading from file
	public void add(String file) {
			
	}
	//TODO implement loading assets dynamically
	public void load() {
		
	}
	//TODO implement unloading assets
	public void unLoad() {
		
	}
	
	public void update() {
//		System.out.println(Entity.state);
		if (Entity.state == 0) {
			ToolKit.getApp().image(this.background, this.backCoords.getX(), this.backCoords.getY());
		}
		else {
			ToolKit.pushApp();
			ToolKit.fillApp(40, 40, 40);
			ToolKit.rectApp(0, 0, 800, 800);
			ToolKit.popApp();
//			System.out.println(true);
		}
		
		for (ArrayList<Entity> l : this.hash.values()) {
			for (Entity e : l) {
				
				
				
				
				e.currRoom = this;
				
					
					if (e.isDelete()) {
						if (e.getType() == Entities.TRIGGER) {e.update();}
						this.sub.add(e);
					} 
					
					else {
						e.update(); this.add.addAll(e.getMoveSet().getTriggers()); 
					}
					
					if (e.getHash() != ToolKit.hash((int)(e.getRX()/CHUNK_SIZE), (int)(e.getRY()/CHUNK_SIZE))) {
						this.mod.add(e);
					}
				
					if (Entity.state == 1 && e.entID == 3) {
//						System.out.println(e.entID);
						e.setY((float)(Math.sin(ToolKit.getApp().frameCount/10)));
						e.setX((float)(Math.cos(ToolKit.getApp().frameCount/20)));
//						ToolKit.rectApp(200, 350, 400, 400);
					}
				
			} 
		}
		
		
		for (Entity e : this.mod) {this.removeHash(e); e.setHash(); this.addHash(e);}
		if (!Room.pause) {this.moveBackground(); }
		this.see = ToolKit.getNeighborsRender(this.p, this.hash, this.WIDTH_CHUNK, this.HEIGHT_CHUNK, 3);
		Collections.sort(see); for (Entity e : see) {
			ToolKit.pushApp();
			ToolKit.fillApp(100, 100, 100);
			e.setXY(e.getRX()+this.backCoords.getX(), e.getRY()+this.backCoords.getY()); e.show();
			if (e.entID == 3) {
				ToolKit.pushApp();
				ToolKit.fillApp(50, 50, 50);
				ToolKit.rectApp(200, 350, 400, 400);
				ToolKit.popApp();
			} ToolKit.popApp();
		}
		this.removeHash(sub); this.addHash(add); sub.clear(); add.clear(); mod.clear(); see.clear();
		
		switch (encounter) {
			case 1:
				TextBox.display("well, well, well... look who it is.");
				break;
			case 2:
				TextBox.display("I wont let you destroy communism or escape to Brazil");
				break;
			case 3:
				TextBox.display("Lets see you get through my strongest solder.. KARL MARX");
				break;
			case 4:
				Entity.state = 1;
				Room.pause = false;
				Room.encounter++;
				break;
			case 5:
				if (startMusic == 0) {
					startMusic = System.currentTimeMillis() + 1500;
				}
				if (startMusic > System.currentTimeMillis()) {
					ToolKit.getApp().text("Now Playing: OURLOVANIA - Magentium", 200, 325);
					Main.s.loop();
					Main.s.amp(0.03f);
				}
				
				
				if (a == null) {
					a = new Attack1[10];
					for (int i = 0; i < a.length; i++) {
						float x = (float)Math.random()*800, y = (float)Math.random()*800;
						while (ToolKit.circRectCollide(x, y, 100, 200, 350, 400, 400)) {
							x = (float)Math.random()*800;
							y = (float)Math.random()*800;
						}
						a[i] = new Attack1(x, y, p, i*500, 5000, 3);
					}
				}
				boolean allOffScreen = true;
				for (Attack a1 : a) {
					a1.update(Main.sythe, 3);
					allOffScreen = a1.offscreen() && allOffScreen;
				}
				if (allOffScreen) {a = null; encounter++;}
				break;
			case 6:
				if (a == null) {
					a = new Attack1[20];
					for (int i = 0; i < a.length; i++) {
						float x = (float)Math.random()*800, y = (float)Math.random()*800;
						while (ToolKit.circRectCollide(x, y, 100, 200, 350, 400, 400)) {
							x = (float)Math.random()*800;
							y = (float)Math.random()*800;
						}
						a[i] = new Attack1(x, y, p, i*300, 300, 5);
					}
				}
				allOffScreen = true;
				for (Attack a1 : a) {
					a1.update(Main.sythe, 2);
					allOffScreen = a1.offscreen() && allOffScreen;
				}
				if (allOffScreen) {a = null; encounter++;}
				break;
			case 7:
				if (a == null) {
					a = new Attack2[20];
					for (int i = 0; i < a.length; i++) {
						a[i] = new Attack2(p, i*500, 500, 5, i%2==1);
					}
				}
				allOffScreen = true;
				for (Attack a1 : a) {
					a1.update(Main.hammer, 3);
					allOffScreen = a1.offscreen() && allOffScreen;
				}
				if (allOffScreen) {encounter++; a = null;}
				break;
			case 8:
				if (a == null) {
					a = new Attack2[30];
					for (int i = 0; i < a.length; i++) {
						a[i] = new Attack2(p, i*300, 500, 6, i%2==1);
					}
				}
				allOffScreen = true;
				for (Attack a1 : a) {
					a1.update(Main.hammer, 2);
					allOffScreen = a1.offscreen() && allOffScreen;
				}
				if (allOffScreen) {encounter++; a = null;}
				break;
			case 9:
				if (a == null) {
					a = new Attack[25]; 
					for (int i = 0; i < a.length; i++) {
						if (i < 10) {
							float x = (float)Math.random()*800, y = (float)Math.random()*800; 
							while (ToolKit.circRectCollide(x, y, 100, 200, 350, 400, 400)) { 
								x = (float)Math.random()*800; y = (float)Math.random()*800; 
							} 
							a[i] = new Attack1(x, y, p, i*500, 5000, 2); } 
						else { 
							a[i] = new Attack2(p, (i-10)*500, 500, 5, i%2==1); 
						} 
					} 
				}
				allOffScreen = true; 
				for (int i = 0; i < a.length; i++) { 
					if (i < 10) { 
						a[i].update(Main.sythe, 2); 
					} 
					else { 
						a[i].update(Main.hammer, 3); 
					} 
					allOffScreen = a[i].offscreen() && allOffScreen; 
				} 
				if (allOffScreen) {encounter++; a = null;} 
				break;
			case 10:
				if (a == null) {
					a = new Attack[50]; 
					for (int i = 0; i < a.length; i++) {
						if (i < 10) {
							float x = (float)Math.random()*800, y = (float)Math.random()*800; 
							while (ToolKit.circRectCollide(x, y, 100, 200, 350, 400, 400)) { 
								x = (float)Math.random()*800; y = (float)Math.random()*800; 
							} 
							a[i] = new Attack1(x, y, p, i*500, 10000, 5); } 
						else { 
							a[i] = new Attack2(p, (i-10)*500, 500, 3, true); 
						} 
					} 
				}
				allOffScreen = true; 
				for (int i = 0; i < a.length; i++) { 
					if (i < 10) { 
						a[i].update(Main.hammer, 3); 
					} 
					else { 
						a[i].update(Main.sythe, 2); 
					} 
					allOffScreen = a[i].offscreen() && allOffScreen; 
				} 
				if (allOffScreen) {encounter++; a = null;} 
				break;
			case 11:
				Room.pause = true;
				Main.s.stop();
				Entity.state = 0;
				TextBox.display("[You defeated Karl Marx!]");
				break;
			case 12:
				TextBox.display("Impossible.. Karl Marx.. he cant be..");
				break;
			case 13:
				TextBox.display("*Karl Marx death noises*\n\n*Castro running away noises*");
				break;
			case 14:
				Room.pause = false;
				hash.clear();
				ArrayList<Entity> e = new ArrayList<Entity>();
				e.add(p);
				hash.put(p.getHash(), e);
				encounter++;
				break;
			case 15:
				if (this.win == 0) {win = System.currentTimeMillis()+2500;}
				Main.win = win < System.currentTimeMillis();
				break;

		}
		
		if (Main.hp <= 0) {
			a = null;
//			encounter = 5;
		}
	}

	private void moveBackground() {
		Point pot = this.p.getPotential();
		boolean left = p.getRX() + pot.getX() > ToolKit.getAppWidth()/2 - p.getW()/2;
		if (left && p.getRX() + pot.getX() < this.background.width - ToolKit.getAppWidth()/2 - p.getW()/2) {
			this.backCoords.addX(-pot.getX());  // Move background X coord
		} else {this.backCoords.setX(left? -this.background.width + ToolKit.getAppWidth(): 0);}
		boolean up = p.getRY() + pot.getY() > ToolKit.getAppHeight()/2 - p.getH()/2;
		if (up && p.getRY() + pot.getY() < this.background.height - ToolKit.getAppHeight()/2 - p.getH()/2) {
			this.backCoords.addY(-pot.getY());  // Move background Y coord
		} else {this.backCoords.setY(up? -this.background.height + ToolKit.getAppHeight(): 0);}
	}
	
	public ArrayList<Entity> getRoom(Entity e) {return ToolKit.getNeighbors(e, this.hash, this.WIDTH_CHUNK, this.HEIGHT_CHUNK, 4);}
	public Player getPlayer() {return this.p;}
	public Point getBackCoords() {return this.backCoords == null? new Point() : this.backCoords;}
	public int getImageWidth() {return this.background == null? ToolKit.getAppWidth() : this.background.width;}
	public int getImageHeight() {return this.background == null? ToolKit.getAppHeight() :this.background.height;}
	public int getSize() {return this.hash.size();}
	
}
