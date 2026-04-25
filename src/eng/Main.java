package eng;

import game.*;
import game.entity.*;
import game.entity.abilities.*;
import game.entity.movement.*;
import game.util.*;
import processing.core.*;
import processing.sound.SoundFile;

public final class Main extends PApplet {
	
	Rect[] obstacles;
	private final int[][][] PlayerSpriteLayers = {{}, {}};
	private int[] PlayerColorTints = {};
	private PImage[] tesla = new PImage[] {null, null}, marx = new PImage[] {null, null};
	private MoveSet[] moves = new MoveSet[] {new EightDirectionalMove(new Rect(400-13*4, 1792-40*4, 26, 40), 4, 4), new EightDirectionalMove(new Rect(400-13*3, 600-20*3, 26, 40), 4, 2)};
	private Ability[][] abilities = new Ability[][] {{new Walk8d(), new Sprint8d(16), new Interact8d(90)}, {new Walk8d()}};
	public static PImage bck1, tile1, hammer, pen, sythe, paper, castro;
	Room[] test  = new Room[2];
	MultiStateEntity p;
	public static int hp = 100;
	public static long immuneTill = 0;
	public static SoundFile s;
	public static boolean win = false;

	public static void main(String[] args) {
		PApplet.main(Main.class);
	}
	
	// Only used for the size of the canvas
	@Override
	public void settings() {
		size(800, 800);
		noSmooth();
	}
	
	@Override
	public void setup() {
//		frameRate(9999);
		surface.setTitle("ENGLISH");
		textFont(createFont("src/Assets/Fonts/TeslaCrashToFont.ttf", 36, false));
		noCursor(); noStroke(); textSize(20);
		tesla[0] = loadImage("src/Assets/player.png");
		tesla[1] = loadImage("src/Assets/player.png");
		marx[0] = loadImage("src/Assets/marx.png");
		marx[1] = loadImage("src/Assets/karl.png");
		castro = loadImage("src/Assets/castro.png");
		bck1 = loadImage("src/Assets/background1.png");
		
		hammer = loadImage("src/Assets/hammer.png");
		sythe = loadImage("src/Assets/sythe.png");
		paper = loadImage("src/Assets/paper.png");
		pen = loadImage("src/Assets/pen.png");
		
		s = new SoundFile(this, "src/Assets/SOVIET ANTHEM MEGALOVANIA.wav");
		
		
		ToolKit.setApp(this);
		test[0] = new Room(bck1); test[1] = new Room(null);
		p = new MultiStateEntity(new Entities[] {Entities.PLAYER, Entities.PLAYER}, test, tesla, moves, abilities, PlayerSpriteLayers, PlayerColorTints, false, false);
		test[0].add(marx[0], 502-17*2, 400, 17*4f, 21*4, 0, 0, 4);
		test[1].add(marx[1], 300, 50, 0, 0, 0, 0, 4);
		test[1].add(200, 350, 10, 400);
		test[1].add(200, 350, 400, 10);
		test[1].add(600, 350, 10, 400);
		test[1].add(200, 740, 400, 10);
		test[0].add(castro, 0, 400, 2000, 22*4, 350, 20, 4);
		
//		for (int i = 0; i < 20; i++) {
//			test.add((float) Math.random() * (bck1.width-28*3), (float) Math.random() * (bck1.height-28*3), (float) (Math.random() * 190)+10, (float) (Math.random() * 190)+10);
//		}
		// five hundred teslas
//		for (int i = 0; i < 1000; i++) {
//			MoveSet[] moves = new MoveSet[] {new EightDirectionalMove(new Rect((float)Math.random()*(bck1.width-28*3), (float)Math.random()*(bck1.height-28*3), 28, 28), 3, 3), new PlatformerSimpleMove()};
//			int[] EnemyColorTints = {255, 111, 111, 111, 111, 255, 255, 200, 0};
//			for (int j = 0; j < EnemyColorTints.length; j++) {EnemyColorTints[j] = (int) (Math.random() * 256);}
//			test.add(new Enemy(test, tesla, moves, abilities2, PlayerSpriteLayers, EnemyColorTints));
//		}
		
	}
	
	@Override
	public void draw() {
		background(50);
		
		test[Entity.state].update();
		
		if (Entity.state == 1) {
			ToolKit.pushApp();
			ToolKit.fillApp(50, 50, 50);
			ToolKit.rectApp(20, 760, 200, 30);
			ToolKit.fillApp(hp*2, 0, 0);
			ToolKit.rectApp(20, 760, hp*2, 30);
			ToolKit.popApp();
			ToolKit.getApp().text("HP", 240, 785);
		}
		
		if (hp <= 0) {
			ToolKit.pushApp();
			ToolKit.fillApp(50, 50, 50);
			ToolKit.rectApp(0, 0, 800, 800);
			ToolKit.popApp();
			ToolKit.getApp().text("You Lost, Press 'Z' to try again", 240, 400);
			
		}
		
		if (win) {
			ToolKit.pushApp();
			ToolKit.fillApp(50, 50, 50);
			ToolKit.rectApp(0, 0, 800, 800);
			ToolKit.popApp();
			ToolKit.getApp().text("You won and escaped to Brazil!", 240, 400);
			
		}
		
//		ToolKit.getApp().image(marx[0], 400-17*2, 400, 	17*4, 42*4);
		
//		System.out.println(test.getSize());
		
//		for (int i = 0; i < 2; i++) {
//			MoveSet[] moves = new MoveSet[] {new EightDirectionalMove(new Rect((float)Math.random()*(bck1.width-28*3), (float)Math.random()*(bck1.height-28*3), 28, 28), 3, 3), new PlatformerSimpleMove()};
//			int[] EnemyColorTints = {255, 111, 111, 111, 111, 255, 255, 200, 0};
//			for (int j = 0; j < EnemyColorTints.length; j++) {EnemyColorTints[j] = (int) (Math.random() * 256);}
//			test.add(new Enemy(test, tesla, moves, abilities2, PlayerSpriteLayers, EnemyColorTints));
//		}
		
		textSize(36); text(Math.round(this.frameRate)+"fps", 10, 30);
		
	}
	
	@Override
	public void keyPressed() {
		ToolKit.setKey(this.keyCode, true);
	}
	
	@Override
	public void keyReleased() {
		if (this.keyCode == 90 && Room.pause) {
			Room.encounter++; 
		}
		if (this.keyCode == 90 && hp <= 0) {
			Room.encounter = 5;
			hp = 100;
		}
//		System.out.println(this.keyCode);
		ToolKit.setKey(this.keyCode, false);
	}
}