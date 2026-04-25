package game;

import java.util.ArrayList;
import game.entity.Player;

// A level holds many Rooms
public class Level {
	
	private ArrayList<Room> rooms = new ArrayList<Room>();
	
	Player p;
	
	public Level(Player p) {this.p = p;}
	
//	public void addRoom(Rect o, PImage i) {rooms.add(new Room(p, o, i));}
//	public void addRoom(Rect[] o, PImage i) {rooms.add(new Room(p, o, i));}
	
	public void addRooms(Room[] r) {
		
	}
	
	public void addRooms(ArrayList<Room> r) {
		
	}
	
	//TODO later
	public void addRooms(String file) {
		
	}

}
