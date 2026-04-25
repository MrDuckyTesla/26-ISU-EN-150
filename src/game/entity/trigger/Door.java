package game.entity.trigger;

import game.Room;
import game.entity.Entity;

public final class Door extends Trigger {
	
	Room next;
	
	public Door(float x, float y, float w, float h, Entity i) {
		super(x, y, w, h, i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Triggers getTriggerType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDelete() {return false;}

	@Override
	public boolean isMarked() {
		// TODO Auto-generated method stub
		return false;
	}

}
