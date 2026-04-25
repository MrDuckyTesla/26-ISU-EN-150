package game.util;


import processing.core.PApplet;

public class Rect extends Point {
	
	private float w, h;
	
	public Rect() {super(); this.instantiate(0, 0);}
	public Rect(Point p) {super(p.getX(), p.getY()); this.instantiate(w, h);}
	public Rect(float x, float y) {super(x, y); this.instantiate(w, h);}
	public Rect(Point p, Point q) {super(p.compareTo(q) < 0? p : q); this.w = PApplet.abs(p.getX() - q.getX()); this.h = PApplet.abs(p.getY() - q.getY());}
	public Rect(Point p, float w, float h) {super(p.getX(), p.getY()); this.instantiate(w, h);}
	public Rect(float x, float y, float w, float h) {super(x, y); this.instantiate(w, h);}
	
	public boolean isTLInside(Rect other) {return ToolKit.pointRectCollide(getX(), getY(), other.getX(), other.getY(), other.getW(), other.getH());}
	public boolean isTRInside(Rect other) {return ToolKit.pointRectCollide(getX()+w, getY(), other.getX(), other.getY(), other.getW(), other.getH());}
	public boolean isBRInside(Rect other) {return ToolKit.pointRectCollide(getX()+w, getY()+h, other.getX(), other.getY(), other.getW(), other.getH());}
	public boolean isBLInside(Rect other) {return ToolKit.pointRectCollide(getX(), getY()+h, other.getX(), other.getY(), other.getW(), other.getH());}
	public boolean isInside(Rect other) {return this.isTLInside(other) && this.isTRInside(other) && this.isBRInside(other) && this.isBLInside(other);} // Checks if this has all four corners inside other
	private void instantiate(float w, float h) {this.w = w; this.h = h;}
	
//	public void update() {this.displayRect();}  // Function for children to inherit, will probably be used for animated obstacles
	public void interact() {}  // Another function for children to inherit, will probably be used for text box
	
	public boolean isCollide(Rect o) {return ToolKit.rectRectCollide(this.getX(), this.getY(), this.w, this.h, o.getX(), o.getY(), o.getW(), o.getH());}
	
//	public boolean displayRect() {if (!Point.getHasApp()) {return false;} Point.getApp().rect(getX(), getY(), w, h); return true;}
//	public boolean displayRect(float s) {if (!Point.getHasApp()) {return false;} Point.getApp().rect(getX()*s, getY()*s, w*s, h*s); return true;}
	public boolean displayRect(PApplet app, float s) {app.rect(getX()*s, getY()*s, w*s, h*s); return true;}
	public boolean displayRect(PApplet app) {app.rect(getX(), getY(), w, h); return true;}
	public boolean displayRect(PApplet app, int[] color) {app.push(); app.fill(app.color(color[0], color[1], color[2])); app.rect(getX(), getY(), w, h); app.pop(); return true;}
	
//	public boolean appRect(float w, float h) {Point.rectApp(this.getX(), this.getY(), this.w, this.h); return true;}
	
	// Get
	public float getW() {return this.w;}
	public float getH() {return this.h;}
	public float[] getXYWH() {return new float[] {this.getX(), this.getY(), this.w, this.h};}
	public float getArea() {return this.w * this.h;}
	public float getPerimeter() {return 2*this.w + 2*this.h;}
	public Point getPoint() {return super.get();}
	public Point getCorner(boolean topSide, boolean rightSide) {return new Point(rightSide? getX()+w : getX(), topSide? getY() : getY()+h);}
	public Point[] getCorners() {return new Point[] {getCorner(true, false), getCorner(true, true), getCorner(false, true), getCorner(false, false)};}
	
	// Overridden functions
	@Override
	public Rect get() {return new Rect(this.getX(), this.getY(), this.w, this.h);}
	@Override
	public boolean equals(Object other) {return this.getX() == ((Rect) other).getX() && this.getY() == ((Rect) other).getY() && this.w == ((Rect) other).getW() && this.h == ((Rect) other).getH();}
	@Override
	public String toString() {return "("+this.getX()+", "+this.getY() + ", "+w+", "+h+")";}
	@Override
	public int compareTo(Object o) {return Float.compare(this.getY() + this.getH(), ((Rect) o).getY() + ((Rect) o).getH());}

}
