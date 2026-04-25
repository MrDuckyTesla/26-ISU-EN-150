package game.util;

import java.util.ArrayList;
import java.util.HashMap;

import game.Room;
import game.entity.Entity;
import processing.core.PApplet;
import processing.core.PImage;

public final class ToolKit {  // "USED TO BE THE MEDIA CLASS, CHANGED DUE TO IT BEING EXPANDED TO MORE OF A GAME ENGINE" - Old Nico <= I also took out all the animation functions and made it into its own class - Nico. yeah so its more of a toolkit, renamed
	
	/**
	 * Stores all keys that are being held down
	 */
	private static HashMap<Integer, Boolean> keys = new HashMap<>();
	/**
	 * Stores the PApplet being used by all classes
	 */
	private static PApplet app = null;
	
	/**
	 * Precompiles the layers and colors of an image into a list
	 * @param app PApplet instance that is being used
	 * @param image The (presumably greyscale) spritesheet being compiled
	 * @param layerList An array separated by layer holding greyscale colors (EX: [ [a, b, c], [d, e], [f] ], 
	 * @return Returns a 1d ArrayList thats formatted with the index of color, the layer it belongs to, and the color
	 */
	public static ArrayList<Integer> PreCompile(PApplet app, PImage image, int[][] layerList) {  // PreCompile to avoid lag
		HashMap<Integer, Integer> layer = new HashMap<>(layerList.length);
		for (int i = 0; i < layerList.length; i++) {  // Iterate through color layers before image loop
			for (int j = 0; j < layerList[i].length; j++) {layer.put(layerList[i][j], i);}
		} ArrayList<Integer> colorList = new ArrayList<>();  // List of colors and indexes to return
		image.loadPixels();  // Load pixels for scanning
		for (int i = 0; i < image.pixels.length; i++) {  // Iterate through pixels
			if (app.alpha(image.pixels[i]) != 0){   // Check if pixel is transparent
				colorList.add(i);  // Push index
				colorList.add(layer.get((int)app.red(image.pixels[i])));  // Push layer
				colorList.add(image.pixels[i]);  // Push color
			}
		} return colorList;  // Return ArrayList
	}
	
	/**
	 * Given the list that is returned from PreCompile(), this function changes the color of input image
	 * @param app PApplet instance that is being used
	 * @param image The (presumably greyscale) spritesheet being recolored
	 * @param colorList List returned from PreCompile()
	 * @param tintList The color you want to tint the image with in [R, G, B] format
	 */
	public static void changeColor(PApplet app, PImage image, ArrayList<Integer> colorList, int[] tintList) {
		image.loadPixels();  // Load pixels for changing
	    for (int i = 0; i < colorList.size(); i += 3) {  // Apply formula:  NR = (3 * (G + r) - (g + b)) / 4
	    	int G = (int) app.red(colorList.get(i+2));
	    	image.pixels[colorList.get(i)] = app.color(
	    		(3 * (G + tintList[    3 * colorList.get(i + 1)]) - (tintList[1 + 3 * colorList.get(i + 1)] + tintList[2 + 3 * colorList.get(i + 1)])) / 4, 
	    		(3 * (G + tintList[1 + 3 * colorList.get(i + 1)]) - (tintList[2 + 3 * colorList.get(i + 1)] + tintList[    3 * colorList.get(i + 1)])) / 4, 
	    		(3 * (G + tintList[2 + 3 * colorList.get(i + 1)]) - (tintList[    3 * colorList.get(i + 1)] + tintList[1 + 3 * colorList.get(i + 1)])) / 4
	    	);
	    } image.updatePixels();  // Update the pixels
	}
	
	/**
	 * Takes in an image along with a color from the image and a new color. Replaces the old color with a new one
	 * @param app PApplet instance that is being used
	 * @param image The image that is getting changed
	 * @param ogColorRGB
	 * @param newColorRGB
	 */
	public static void changeSingleColor(PApplet app, PImage image, int[] ogColorRGB, int[] newColorRGB) {
		image.loadPixels();  // Load pixels for changing
		for (int i = 0; i < image.pixels.length; i++) {  // Iterate through pixels
			if (app.alpha(image.pixels[i]) != 0) {  // Check if pixel is transparent
				if (app.color(ogColorRGB[0], ogColorRGB[1], ogColorRGB[2]) == app.color(image.pixels[i])) {
					image.pixels[i] = app.color(newColorRGB[0], newColorRGB[1], newColorRGB[2]);
				}
			}
		} image.updatePixels();  // Update the pixels
	}
	
	/**
	 * Takes in an image that has been recompiled and resets it back to its original colors
	 * @param app PApplet instance that is being used
	 * @param image The image that is getting changed
	 * @param colorList List returned from PreCompile()
	 */
	public static void resetColor(PApplet app, PImage image, ArrayList<Integer> colorList) {  // Resets all pixels in image based on pre-compile list
		image.loadPixels();  // Load pixels for changing
		for (int i = 0; i < colorList.size(); i+= 3) {
			image.pixels[colorList.get(i)] = app.color(colorList.get(i + 2), colorList.get(i + 2), colorList.get(i + 2));
		} image.updatePixels();  // Update the pixels
	}
	
	/**
	 * Takes an image and sets its alpha value to 0, making the image completely transparent
	 * @param app PApplet instance that is being used
	 * @param image The image that is getting changed
	 */
	public static void clearImage(PApplet app, PImage image) {
		image.loadPixels();  // Load pixels for changing
		for (int i = 0; i < image.pixels.length; i++) {image.pixels[i] = app.color(app.red(image.pixels[i]), app.green(image.pixels[i]), app.blue(image.pixels[i]), 0);}
		image.updatePixels();  // Update the pixels
	}
	
	/**
	 * Takes a chunk of the PApplet and pixelates it, automatically drawing it over the given area of PApplet
	 * @param app PApplet instance that is being used
	 * @param res Resolution of pixelated area
	 * @param x X coordinate indicating where to pixelate
	 * @param y Y coordinate indicating where to pixelate
	 * @param w Width of pixelated area
	 * @param h Height of pixelated area
	 */
	public static void pixelate(PApplet app, int res, float x, float y, float w, float h) {
		PImage image = app.get(); // Get canvas
		image.resize(app.width/res, app.height/res);  // Resize canvas to wanted size
		app.image(image, x, y, w, h, (int) (x / res), (int) (y / res), (int) (x / res + w / res), (int) (y / res + h / res));
	}
	
	/**
	 * Takes a chunk of an image and pixelates it, automatically drawing it over the given area of the image
	 * @param app PApplet instance that is being used
	 * @param image The image that is getting pixelated
	 * @param res Resolution of pixelated area
	 * @param x X coordinate indicating where to pixelate
	 * @param y Y coordinate indicating where to pixelate
	 * @param w Width of pixelated area
	 * @param h Height of pixelated area
	 */
	public static void pixelate(PApplet app, PImage image, int res, float x, float y, float w, float h) {
		image.resize(app.width/res, app.height/res);  // Resize image to wanted size
		app.image(image, x, y, w, h, (int) (x / res), (int) (y / res), (int) (x / res + w / res), (int) (y / res + h / res));
	}
	
	/**
	 * Returns a set of coordinates that keep a line within a certain radius
	 * @param centerX X coordinate of where the line begins from
	 * @param centerY Y coordinate of where the line begins from
	 * @param endX X coordinate of where the line wants to end
	 * @param endY Y coordinate of where the line wants to end
	 * @param radius Radius that line must stay within
	 * @param forceRadius Boolean that dictates if the line length will be the radius
	 * @return Set of coordinates in form [X, Y] that should be used instead of endpoint
	 */
	public static float[] lineRadius(float centerX, float centerY, float endX, float endY, float radius, boolean forceRadius) {  // Returns coords to keep a line within a radius
		if (forceRadius || PApplet.dist(centerX, centerY, endX, endY) > radius) {  // If the line is outside the radius
			if (endX == centerX) {endX += 0.1f;} 
			if (endY == centerY) {endY += 0.1f;}
			endX -= centerX; endY -= centerY;  // Make sure the x and y can never be 0
			int flip = 1; float temp = endX;  // Make a copy of x
			if (endX < 0) {flip = -1;}
			return new float[] {flip * radius * PApplet.cos(PApplet.atan(endY / endX)) + centerX, flip * radius * PApplet.sin(PApplet.atan(endY / temp)) + centerY};
		} return new float[] {endX, endY};
	}
	
	/**
	 * Returns a set of coordinates that create an arm with one joint
	 * @param centerX X coordinate of where the arm begins from
	 * @param centerY Y coordinate of where the arm begins from
	 * @param length1 length of the first line that begins on centerX/Y
	 * @param length2 length of the second line that begins on first lines end
	 * @param endX X coordinate of where the arm wants to end
	 * @param endY Y coordinate of where the arm wants to end
	 * @param bendRight Boolean that dictates if the arms joint bends to the left or right
	 * @return Set of coordinates in form [X1, Y1, X2, Y2] that should be used as such: line(centerX, centerY, X1, Y1); line(X1, Y1, X2, Y2)
	 */
	public static float[] getLimbCoords(float centerX, float centerY, float length1, float length2, float endX, float endY, boolean bendRight) {
		float dist = PApplet.dist(centerX, centerY, endX, endY);
		float[] coord = ToolKit.lineRadius(centerX, centerY, endX, endY, length1 + length2, true);
		if (dist > length1 + length2) {return new float[] {centerX, centerY, coord[0], coord[1]};} 
		else if (dist < length1 - length2) {
			coord = ToolKit.lineRadius(centerX, centerY, endX, endY, length1, true);
			return new float[] {coord[0], coord[1], coord[0], coord[1]};
		} else if (dist < length2 - length1) {
			coord = ToolKit.lineRadius(centerX, centerY, endX, endY, -length1, true);
			float[] coord2 = ToolKit.lineRadius(coord[0], coord[1], endX, endY, length2, true);
			return new float[] {coord[0], coord[1], coord2[0], coord2[1]};
		}  endX -= centerX; endY -= centerY;  // Make sure the x and y can never be 0
		float theta = PApplet.acos((float) ((Math.pow(dist, 2) + Math.pow(length1, 2) - Math.pow(length2, 2)) / (2 * dist * length1)));
		int flip = 1; if (endX < 0) {flip = -1;}  // Make line not go back 180 degrees
		if (bendRight) {theta *= -1;}  // Make theta bend right if true
		float x = flip * length1 * PApplet.cos(theta + PApplet.atan(endY / endX)) + centerX;
		float y = flip * length1 * PApplet.sin(theta + PApplet.atan(endY / endX)) + centerY;
		coord = ToolKit.lineRadius(x, y, endX + centerX, endY + centerY, length2, true);
		return new float[] {x, y, coord[0], coord[1]};
	}
	
	/**
	 * Copies image1 onto image2
	 * @param app PApplet instance that is being used
	 * @param image1 Image that is being overridden
	 * @param image2 Source image being used to copy from
	 */
	public static void copy(PApplet app, PImage image1, PImage image2) {
		image1.loadPixels(); image2.loadPixels();
		for (int i = 0; i < image1.pixels.length; i++) {
			if (app.alpha(image2.pixels[i]) != 0) {
				image1.pixels[i] = image2.pixels[i];
			}
		} image1.updatePixels();
	}
	
	/**
	 * Calculates if and where two lines collide
	 * @param x1 One of the two X coordinates belonging to line1
	 * @param y1 One of the two Y coordinates belonging to line1
	 * @param x2 One of the two X coordinates belonging to line1
	 * @param y2 One of the two Y coordinates belonging to line1
	 * @param x3 One of the two X coordinates belonging to line2
	 * @param y3 One of the two Y coordinates belonging to line2
	 * @param x4 One of the two X coordinates belonging to line2
	 * @param y4 One of the two Y coordinates belonging to line2
	 * @return Returns the set of coordinates in form [X, Y] if there is a collision. If there is no collision, returns empty array
	 */
	public static float[] lineLineCollide(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {  // Collision for two lines, very important function
		float d1 = PApplet.dist(x1, y1, x2, y2), d2 = PApplet.dist(x3, y3, x4, y4);  // Get lengths of line segments
		if (d1 == 0 || d2 == 0 || (x1 - x2 == 0 && x3 - x4 == 0) || (y1 - y2 == 0 && y3 - y4 == 0)) {return new float[] {};}
		float m1, m2, b1, b2, iX, iY;
		m1 = (y2 - y1) / (x2 - x1); m2 = (y4 - y3) / (x4 - x3);  // Find Slopes
		b1 = y1 - m1 * x1; b2 = y3 - m2 * x3; // Find y intercepts
		if (x4 - x3 == 0) {iX = x3;}  // Find X coordinate while also dealing with vertical and horizontal slopes
	    else if (x2 - x1 == 0) {iX = x1;}
	    else {iX = (b1 - b2) / (m2 - m1);}
		iY = (x4 - x3) == 0? m1 * iX + b1: m2 * iX + b2;  // Get Y coordinate while also dealing with horizontal slope
		// Check if X and Y coordinates is within line segments
		if (PApplet.dist(iX, iY, x2, y2) > d1 || PApplet.dist(iX,  iY,  x1,  y1) > d1 || PApplet.dist(iX, iY, x3, y3) > d2 || PApplet.dist(iX, iY, x4, y4) > d2) {return new float[] {};}
		return new float[] {iX, iY};
	}
	
	/**
	 * Finds the closest spot on a line from a point
	 * @param px X coordinate of point
	 * @param py Y coordinate of point
	 * @param x1 One of the two X coordinates belonging to line
	 * @param y1 One of the two Y coordinates belonging to line
	 * @param x2 One of the two X coordinates belonging to line
	 * @param y2 One of the two Y coordinates belonging to line
	 * @param rounded boolean that dictates if points towards the end of a line form sharp corners or not
	 * @return Returns the set of coordinates in form [X, Y] of the nearest point on the line
	 */
	public static float[] closestPointLine(float px, float py, float x1, float y1, float x2, float y2, boolean rounded) {
		float m1, m2, b1, b2, iX, iY;
		float maxX = PApplet.max(x1, x2), maxY = PApplet.max(y1, y2);
		float minX = PApplet.min(x1, x2), minY = PApplet.min(y1, y2);
	    m1 = (y2 - y1) / (x2 - x1);  // Find slopes
	    m2 = -1/m1;
	    b1 = y1 - m1 * x1;  // Find y intercepts
	    b2 = py - m2 * px;
	    if (y2 - y1 == 0) {iX = px;}  // Find X and Y coords while also dealing with evil slopes
	    else {iX = x1 - x2 == 0? x1 : (b1 - b2) / (m2 - m1);}
	    iY = x2 - x1 == 0? py : m1 * iX + b1;
	    if (iX > maxX) {iX = rounded? maxX : minX;}  // Check if point is within line
	    else if (iX < minX) {iX = rounded? minX : maxX;}
	    if (iY > maxY) {iY = rounded? maxY : minY;}
	    else if (iY < minY) {iY = rounded? minY : maxY;}
	    return new float[] {iX, iY};  // Return coords
	}
	
	/**
	 * Returns an image of a line with varying thickness, resolution, and color (pixelated line)
	 * @param app PApplet instance that is being used
	 * @param x1 One of the two X coordinates belonging to line
	 * @param y1 One of the two Y coordinates belonging to line
	 * @param x2 One of the two X coordinates belonging to line
	 * @param y2 One of the two Y coordinates belonging to line
	 * @param res Resolution of line in pixels
	 * @param thickness How thick the line should be
	 * @param sizeDisplay Size to display line at
	 * @param color Color of line in [R, G, B, A] format
	 * @return image that contains pixelated line
	 */
	public static PImage lineImage(PApplet app, float x1, float y1, float x2, float y2, int res, float thickness, float sizeDisplay, int[] color) {  //creates an image of a pixelated line
		PImage image = app.createImage(res, res, PApplet.ARGB);
		if (x1 - x2 == 0 && y1 - y2 == 0) {return image;}
		float scaleWidth = sizeDisplay / res, halfScaleWidth = scaleWidth / 2, thickWidth = halfScaleWidth + thickness, c1, c2, minX, minY;
		float[] cline;
		int row = 0;
		image.loadPixels();
		for (int i = 0; i < image.pixels.length; i++) {  // Iterate through pixel list
			if (i % res == 0 && i != 0) {row += 1;}  // If we reach the next row of pixels, add to the Y
		    c1 = i % res * scaleWidth + halfScaleWidth;  // calculate center X and Y coordinate of each Pixel
		    c2 = row * scaleWidth + halfScaleWidth;
		    minX = PApplet.min(x1, x2) - thickWidth; minY = PApplet.min(y1, y2) - thickWidth;  // Check if within bounding box
		    if (ToolKit.pointRectCollide(c1, c2, minX, minY, PApplet.max(x1, x2) + thickWidth - minX, PApplet.max(y1, y2) + thickWidth - minY)) {
		    	cline = ToolKit.closestPointLine(c1, c2, x1, y1, x2, y2);  // Check for closest point on line
		        if (PApplet.dist(cline[0], cline[1], c1, c2) <= thickness) {  // Check if pixel is within the thickness of the line
		        	image.pixels[i] = app.color(color[0], color[1], color[2], color[3]);  // Change colors
		        }
		    }
		} image.updatePixels();  // Update image
		return image;
	}
	
	/**
	 * simple version of pixelated line that draws line instead of returning it
	 * @param app PApplet instance that is being used
	 * @param x1 One of the two X coordinates belonging to line
	 * @param y1 One of the two Y coordinates belonging to line
	 * @param x2 One of the two X coordinates belonging to line
	 * @param y2 One of the two Y coordinates belonging to line
	 */
	public static void lineDraw(PApplet app, float x1, float y1, float x2, float y2) {
		app.image(ToolKit.lineImage(app, x1, y1, x2, y2), 0, 0, app.width, app.height);
	}
	
	/**
	 * Returns an image of a circle with varying thickness, resolution, and color (pixelated circle). Can have gradient too
	 * @param app PApplet instance that is being used
	 * @param x Center X coordinate of circle
	 * @param y Center Y coordinate of circle
	 * @param res Resolution of circle in pixels
	 * @param thickness Radius of circle
	 * @param sizeDisplay Size to display circle at
	 * @param color1 Color of circle in [R, G, B, A] format
	 * @param gradient Boolean that dictated if there is a gradient
	 * @param color2 Color of circle gradient in [R, G, B, A] format
	 * @return image that contains pixelated circle
	 */
	public static PImage circleImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1, boolean gradient, int[] color2) {
		float scaleWidth = sizeDisplay / res, halfScaleWidth = scaleWidth / 2, c1, c2; int row = 0;
		PImage image = app.createImage(res, res, PApplet.ARGB);
		image.loadPixels();
		for (int i = 0; i < image.pixels.length; i++) {  // Iterate through pixel list
			if (i % res == 0 && i != 0) {row += 1;}  // If we reach the next row of pixels, add to the Y
		    c1 = i % res * scaleWidth + halfScaleWidth;  // calculate center X and Y coordinate of each Pixel
		    c2 = row * scaleWidth + halfScaleWidth;
		    float dist = PApplet.dist(x, y, c1, c2);  // Check if within bounding box
		    if (dist <= thickness) {
		    	float ratio = dist/thickness;  // Calculate ratio of radius to color
		    	image.pixels[i] = app.color(  // Change colors
		    		gradient? color1[0] : PApplet.lerp(color1[0], color2[0], ratio), 
		    		gradient? color1[1] : PApplet.lerp(color1[1], color2[1], ratio), 
		    		gradient? color1[2] : PApplet.lerp(color1[2], color2[2], ratio), 
		    		gradient? color1[3] : PApplet.lerp(color1[3], color2[3], ratio)
		    	);
		    }
		} image.updatePixels();  // Update image
		return image;
	}
	
	/**
	 * Returns an image of squared dimensions with a nice gradient (pixelated circle)
	 * @param app PApplet instance that is being used
	 * @param x Center X coordinate of square
	 * @param y Center Y coordinate of square
	 * @param res Resolution of square in pixels
	 * @param thickness Radius of square
	 * @param sizeDisplay Size to display square at
	 * @param color1 Color of square in [R, G, B, A] format
	 * @param gradient Boolean that dictated if there is a gradient
	 * @param color2 Color of square gradient in [R, G, B, A] format
	 * @return image of squared dimensions with a nice gradient
	 */
	public static PImage squareImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1, boolean gradient, int[] color2) {
		float scaleWidth = sizeDisplay / res, halfScaleWidth = scaleWidth / 2, c1, c2; int row = 0;
		PImage image = app.createImage(res, res, PApplet.ARGB);
		image.loadPixels();
		for (int i = 0; i < image.pixels.length; i++) {  // Iterate through pixel list
			if (i % res == 0 && i != 0) {row += 1;}  // If we reach the next row of pixels, add to the Y
		    c1 = i % res * scaleWidth + halfScaleWidth;  // calculate center X and Y coordinate of each Pixel
		    c2 = row * scaleWidth + halfScaleWidth;
		    float dist = PApplet.dist(x, y, c1, c2);  // Check if within bounding box
		    float ratio = dist/thickness;  // Calculate ratio of radius to color
		    image.pixels[i] = app.color(  // Change colors
		    	gradient? color1[0] : PApplet.lerp(color1[0], color2[0], ratio), 
		    	gradient? color1[1] : PApplet.lerp(color1[1], color2[1], ratio), 
		    	gradient? color1[2] : PApplet.lerp(color1[2], color2[2], ratio), 
		    	gradient? color1[3] : PApplet.lerp(color1[3], color2[3], ratio)
		    );
		} image.updatePixels();  // Update image
		return image;
	}
	
	/**
	 * Returns the image given but with usually a single pixel outline around it (some cases may get 2 pixels, faster than outlineThin tho)
	 * @param app PApplet instance that is being used
	 * @param image Image that is getting outlined
	 * @param color1 Color of background in [R, G, B, A] format
	 * @param color2 Color of outline in [R, G, B, A] format
	 * @return Image with outline
	 */
	public static PImage outline(PApplet app, PImage image, int[] color1, int[] color2) {
		image.loadPixels();  // Load pixels
		for (int i = 1; i < image.pixels.length - 1; i++) {
			if (app.alpha(image.pixels[i]) != 0 && !ToolKit.compareProcessingColorList(app, image.pixels[i], color2)) {  // Don't check if transparent pixel
				// Check if there is a blank pixel to the left, right, above or below the normal pixel
				if (ToolKit.compareProcessingColorList(app,  image.pixels[i - 1],  color1) && i % image.width != 0) {image.pixels[i - 1] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				else if (ToolKit.compareProcessingColorList(app,  image.pixels[i + 1],  color1) && (i + 1) % image.width != 0) {image.pixels[i + 1] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				if (i - image.width >= 0 && ToolKit.compareProcessingColorList(app, image.pixels[i - image.width], color1)) {image.pixels[i - image.width] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				else if (i + image.width < image.pixels.length && ToolKit.compareProcessingColorList(app, image.pixels[i + image.width], color1)) {image.pixels[i + image.width] = app.color(color2[0], color2[1], color2[2], color2[3]);}
			}   // Update image
		} image.updatePixels(); return image;
	}
	
	/**
	 * Returns the image given but with a single pixel outline around it
	 * @param app PApplet instance that is being used
	 * @param image Image that is getting outlined
	 * @param color1 Color of background in [R, G, B, A] format
	 * @param color2 Color of outline in [R, G, B, A] format
	 * @return Image with outline
	 */
	public static PImage outlineThin(PApplet app, PImage image, int[] color1, int[] color2) {
		image.loadPixels();  // Load pixels
		for (int i = 1; i < image.pixels.length - 1; i++) {
			if (app.alpha(image.pixels[i]) != 0 && !ToolKit.compareProcessingColorList(app, image.pixels[i], color2)) {  // Don't check if transparent pixel
				// Check if there is a blank pixel to the left, right, above or below the normal pixel
				if (ToolKit.compareProcessingColorList(app,  image.pixels[i - 1],  color1) && i % image.width != 0) {image.pixels[i - 1] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				if (ToolKit.compareProcessingColorList(app,  image.pixels[i + 1],  color1) && (i + 1) % image.width != 0) {image.pixels[i + 1] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				if (i - image.width >= 0 && ToolKit.compareProcessingColorList(app, image.pixels[i - image.width], color1)) {image.pixels[i - image.width] = app.color(color2[0], color2[1], color2[2], color2[3]);}
				if (i + image.width < image.pixels.length && ToolKit.compareProcessingColorList(app, image.pixels[i + image.width], color1)) {image.pixels[i + image.width] = app.color(color2[0], color2[1], color2[2], color2[3]);}
			}  // Update image
		} image.updatePixels(); return image;
	}
	
	/**
	 * ngl gang i forgot what this one does, I think it was supposed to replace colors with more range
	 * @param app PApplet instance that is being used
	 * @param image Image that is getting changed
	 * @param backgroundColor Color of background in [R, G, B, A] format
	 * @param replacementColor Color of replacement in [R, G, B, A] format
	 */
	public static void sharpen(PApplet app, PImage image, int[] backgroundColor, int[] replacementColor) {  // "Change in order to allow less colors, and more replacement colors" - Old Nico
		image.loadPixels();  // Load pixels
		for (int i = 0; i < image.pixels.length; i++) {
			if (app.red(image.pixels[i]) != backgroundColor[0] || app.green(image.pixels[i]) != backgroundColor[1] || app.blue(image.pixels[i]) != backgroundColor[2]) {
				image.pixels[i] = app.color(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
			}
		} image.updatePixels();  // Update image
	}
	
	/**
	 * Checks if two rectangles collide and returns the EXACT coordinates that the moving rectangle should be in
	 * @param px Previous X coordinate of moving rectangle
	 * @param py Previous Y coordinate of moving rectangle
	 * @param x1 Current X coordinate of moving rectangle
	 * @param y1 Current X coordinate of moving rectangle
	 * @param w1 Width of moving rectangle
	 * @param h1 Height of moving rectangle
	 * @param x2 Current X coordinate of non-moving rectangle
	 * @param y2 Current Y coordinate of non-moving rectangle
	 * @param w2 Width of non-moving rectangle
	 * @param h2 Height of non-moving rectangle
	 * @return Coordinates in form of [X, Y, SIDE] with SIDE being which side the moving rectangle collided with (0 is top, 1 is right, 2 is bottom, 3 is left)
	 */
	public static float[] rectRectCollideCoords(float px, float py, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
	    float[][] coordCollide = ToolKit.lineRectCollide(px+w1/2, py+h1/2, x1+w1/2, y1+h1/2, x2-w1/2, y2-h1/2, w2+w1, h2+h1);  // Find coordinates where moving rectangle touches the base rectangle
	    for (int i = 0; i < coordCollide.length; i++) {  // Iterate to find the closest coordinate
	    	if (coordCollide[i].length > 0) {return new float[] {coordCollide[i][0]-w1/2, coordCollide[i][1]-h1/2, i};}
	    } return new float[] {};
	}
	
	/**
	 * Checks if two rectangles collide and returns the EXACT coordinates that the moving rectangle should be in, except moving rectangle is INSIDE non-moving rectangle
	 * @param px Previous X coordinate of moving rectangle
	 * @param py Previous Y coordinate of moving rectangle
	 * @param x1 Current X coordinate of moving rectangle
	 * @param y1 Current X coordinate of moving rectangle
	 * @param w1 Width of moving rectangle
	 * @param h1 Height of moving rectangle
	 * @param x2 Current X coordinate of non-moving rectangle
	 * @param y2 Current Y coordinate of non-moving rectangle
	 * @param w2 Width of non-moving rectangle
	 * @param h2 Height of non-moving rectangle
	 * @return Coordinates in form of [X, Y, SIDE] with SIDE being which side the moving rectangle collided with (0 is top, 1 is right, 2 is bottom, 3 is left)
	 */
	public static float[] nRectRectCollideCoords(float px, float py, float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		float[][] coordCollide = ToolKit.lineRectCollide(px+w1/2, py+h1/2, x1+w1/2, y1+h1/2, x2+w1/2, y2+h1/2, w2-w1, h2-h1);  // Find coordinates where moving rectangle touches the base rectangle
	    for (int i = 0; i < coordCollide.length; i++) {  // Iterate to find the closest coordinate
	    	if (coordCollide[i].length > 0) {return new float[] {coordCollide[i][0]-w1/2, coordCollide[i][1]-h1/2, i};}
	    } return new float[] {};
	}
	
	/**
	 * Checks if rectangle is fully inside another rectangle
	 * @param r1x Smaller rectangles X coordinate
	 * @param r1y Smaller rectangles Y coordinate
	 * @param r1w Smaller rectangles Width
	 * @param r1h Smaller rectangles Height
	 * @param r2x Larger rectangles X coordinate
	 * @param r2y Larger rectangles Y coordinate
	 * @param r2w Larger rectangles Width
	 * @param r2h Larger rectangles Height
	 * @return Boolean that represents if rectangle is fully inside another rectangle
	 */
	public static boolean nRectRectCollide(float r1x, float r1y, float r1w, float r1h, float r2x, float r2y, float r2w, float r2h) {  // opposite of rectRectCollide
	    if (r1w * r1h > r2w * r2h) {return r1x >= r2x || r1x+r1w  <= r2x+r2w || r1y >= r2y || r1y+r1h <= r2y+r2h;}  // Smaller rectangle goes FIRST
	    return r1x <= r2x || r1x+r1w >= r2x+r2w || r1y <= r2y || r1y+r1h >= r2y+r2h;
	}
	
	public static boolean pLineCollide(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {  // Parallel Lines
	    if (x1 - x2 == 0 && x3 - x4 == 0 && PApplet.dist(x1, 0, x3, 0) == 0) {return true;}  // Deal with double vertical or double horizontal lines
	    else if (y1 - y2 == 0 && y3 - y4 == 0 && PApplet.dist(0, y1, 0, y3) == 0) {return true;}
	    return false;
	}
	
	public static boolean compareListList(float[] list1, float[] list2) {
		// Checks if two lists are identical
	    if (list1.length != list2.length) return false;
	    for (int i = 0; i < list1.length; i ++) {
	      if (list1[i] != (list2[i])) return false;
	    } return true;
	}
	
	public static Point unstick(float[] c) {  // Takes rectRectCollideCoords() list as input
		switch ((int) c[2]) {
			case 0:  // Down
				return new Point(c[0], c[1]-0.0001f);
			case 1:  // Left
				return new Point(c[0]+0.0001f, c[1]);
			case 2:  // Up
				return new Point(c[0], c[1]+0.0001f);
			case 3:  // Right
				return new Point(c[0]-0.0001f, c[1]);
			default:
				return new Point(c);
		}
	}
	
	// Basic collision utilizing other collision methods 
	// =======================================================================================================================================================================================================
	
	public static float[][] lineRectCollide(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {
		return new float[][] {  // Rectangle is just four lines, so we return a list of line vs line collisions
			ToolKit.lineLineCollide(x1, y1, x2, y2, rx, ry, rx+rw, ry), 
			ToolKit.lineLineCollide(x1, y1, x2, y2, rx+rw, ry, rx+rw, ry+rh), 
			ToolKit.lineLineCollide(x1, y1, x2, y2, rx+rw, ry+rh, rx, ry+rh), 
			ToolKit.lineLineCollide(x1, y1, x2, y2, rx, ry+rh, rx, ry)
		};
	}  
	
	public static boolean[] lRectRectCollide(float rx1, float ry1, float rw1, float rh1, float rx2, float ry2, float rw2, float rh2) {
		return new boolean[] {  // Used to find which sides two rectangles are touching.. A rectangle is just four lines, so we return a list of line vs line collisions
			ToolKit.pLineCollide(rx1+rw1, ry1, rx1+rw1, ry1+rh1, rx2+rw2, ry2, rx2+rw2, ry2+rh2), 
			ToolKit.pLineCollide(rx1+rw1, ry1+rh1, rx1, ry1+rh1, rx2+rw2, ry2+rh2, rx2, ry2+rh2), 
			ToolKit.pLineCollide(rx1, ry1+rh1, rx1, ry1, rx2, ry2+rh2, rx2, ry2), 
			ToolKit.pLineCollide(rx1, ry1, rx1+rw1, ry1, rx2, ry2, rx2+rw2, ry2)
		};
	} 
	
	public static boolean rectRectCollide(float r1x, float r1y, float r1w, float r1h, float r2x, float r2y, float r2w, float r2h) {return r1x + r1w >= r2x && r1x <= r2x+r2w && r1y + r1h >= r2y && r1y <= r2y+r2h;}
	
	public static boolean rectRectCollideNotExact(float r1x, float r1y, float r1w, float r1h, float r2x, float r2y, float r2w, float r2h) {return r1x + r1w > r2x && r1x < r2x+r2w && r1y + r1h > r2y && r1y < r2y+r2h;}
	
	public static boolean circRectCollide(float cx, float cy, float cr, float rx, float ry, float rw, float rh) {return cx + cr/2 >= rx && rx + rw >= cx - cr/2 && cy + cr/2 >= ry && ry + rh >= cy - cr/2;}
	
	public static boolean pointRectCollide(float px, float py, float rx, float ry, float rw, float rh) {return px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;}
	
	public static boolean pointRectCollideNotExact(float px, float py, float rx, float ry, float rw, float rh) {return px > rx && px < rx + rw && py > ry && py < ry + rh;}
	
	public static boolean pointCircCollide(float px, float py, float cx, float cy, float cr) {return (PApplet.dist(px, py, cx, cy) <= cr);}
	
	// Basic color and list methods
	// =======================================================================================================================================================================================================
	
	public static boolean compareProcessingColorList(PApplet app, int color1, int[] color2) {return app.red(color1) == color2[0] && app.green(color1) == color2[1] && app.blue(color1) == color2[2] && app.alpha(color1) == color2[3];}
	public static boolean compareColorList(int r, int g, int b, int a, int[] col) {return r == col[0] && g == col[1] && b == col[2] && a == col[3];}  // Checks if two colors are not different
	public static boolean compareColorColor(int[] col1, int[] col2) {return col1[0] == col2[0] && col1[1] == col2[1] && col1[2] == col2[2] && col1[3] == col2[3];}  // Checks if two colors are the same
	public static Point getImageWH(PImage p) {return new Point(p.width, p.height);}
	
	// Alternate versions of functions (I really like default parameters, so sad that java doesn't have those)
	// =======================================================================================================================================================================================================
	
	public static void pixelate(PApplet app, int res) {ToolKit.pixelate(app, res, 0, 0, app.width, app.height);}
	
	public static void pixelate(PApplet app, PImage image, int res) {ToolKit.pixelate(app, image, res, 0, 0, app.width, app.height);}
	
	public static float[] lineRadius(float centerX, float centerY, float endX, float endY, float radius) {return ToolKit.lineRadius(centerX, centerY, endX, endY, radius, true);}
	
	public static float[] getLimbCoords(float centerX, float centerY, float length1, float length2, float endX, float endY) {return ToolKit.getLimbCoords(centerX, centerY, length1, length2, endX, endY, false);}
	
	public static float[] closestPointLine(float px, float py, float x1, float y1, float x2, float y2) {return ToolKit.closestPointLine(px, py, x1, y1, x2, y2, true);}
	
	public static PImage lineImage(PApplet app, float x1, float y1, float x2, float y2, int res, float thickness, float sizeDisplay) {return ToolKit.lineImage(app, x1, y1, x2, y2, res, thickness, sizeDisplay, new int[] {0, 0, 0, 255});}
	
	public static PImage lineImage(PApplet app, float x1, float y1, float x2, float y2) {return ToolKit.lineImage(app, x1, y1, x2, y2, app.width/4, 4, app.width, new int[] {0, 0, 0, 255});}
	
	public static PImage circleImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1, boolean gradient) {return ToolKit.circleImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, gradient, color1);}
	
	public static PImage circleImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1) {return ToolKit.circleImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, false, color1);}
	
	public static PImage circleImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay) {return ToolKit.circleImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, false, new int[] {111, 111, 255, 255});}
	
	public static PImage squareImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1, boolean gradient) {return ToolKit.squareImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, gradient, color1);}
	
	public static PImage squareImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay, int[] color1) {return ToolKit.squareImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, false, color1);}
	
	public static PImage squareImage(PApplet app, float x, float y, int res, float thickness, float sizeDisplay) {return ToolKit.squareImage(app, x, y, res, thickness, sizeDisplay, new int[] {255, 111, 111, 255}, false, new int[] {111, 111, 255, 255});}
	
	public static PImage outline(PApplet app, PImage image, int[] color1) {return ToolKit.outline(app, image, color1, new int[] {255, 255, 255, 255});}
	
	public static PImage outline(PApplet app, PImage image) {return ToolKit.outline(app, image, new int[] {0, 0, 0, 0}, new int[] {255, 255, 255, 255});}
	
	public static PImage outlineThin(PApplet app, PImage image, int[] color1) {return ToolKit.outline(app, image, color1, new int[] {255, 255, 255, 255});}
	
	public static PImage outlineThin(PApplet app, PImage image) {return ToolKit.outline(app, image, new int[] {0, 0, 0, 0}, new int[] {255, 255, 255, 255});}
	
	// Random util functions
	// =======================================================================================================================================================================================================
	
	public static <T> ArrayList<T> removeAll(T val, ArrayList<T> arr) {for (int i = 0; i < arr.size(); i++) {if (arr.get(i).equals(val)) {arr.remove(i); i--;}} return arr;}
	
	public static boolean keyIsDown(int key) {if (keys.containsKey(key)) {return keys.get(key);} keys.put(key, false); return false;}
	
	public static void setKey(int key, boolean state) {keys.put(key, state);}
	
	public static <T> T[] pushBack(T[] a, T b) {for (int i = a.length-1; i > 0; i--) {a[i] = a[i-1];} a[0] = b; return a;}
	
	public static int[] pushBack(int[] a, int b) {for (int i = a.length-1; i > 0; i--) {a[i] = a[i-1];} a[0] = b; return a;}
	
	public static boolean notInArray(ArrayList<Integer> a, int[] b) {for (int i = 0; i < a.size(); i++) {for (int j = 0; j < b.length; j++) {if (a.get(i) == b[j]) {return false;}}} return true;}
	
	public static ArrayList<Integer> addNotInArray(ArrayList<Integer> a, int[] b) {for (int i = 0; i < b.length; i++) {if (!a.contains(b[i])) {a.add(b[i]);}} return a;}
	
	public static ArrayList<Integer> add(ArrayList<Integer> a, int[] b) {for (int i = 0; i < b.length; i++) {a.add(b[i]);} return a;}
	
	public static int hash(int x, int y) {return 31*x+y;} // Apparently 31 is a magic number in hashing
	
	public static <E extends Entity> ArrayList<E> getNeighbors(E obj, HashMap<Integer, ArrayList<E>> h, int wid, int hgt, int size) {
		ArrayList<E> neighbors = new ArrayList<>(); int key;
		int x = (int) (obj.getRX()/Room.CHUNK_SIZE), y = (int) (obj.getRY()/Room.CHUNK_SIZE);
		for (int i = -size; i < 1+size; i++) {
			for (int j = -size; j < 1+size; j++) {
				int nX = x+i, nY = y+j;
				if (nX <= wid && nY <= hgt && nX >= 0 && nY >= 0) {
					key = hash(nX, nY);
					if (h.containsKey(key)) {neighbors.addAll(h.get(key));}
				}
			}
		} return neighbors;
	}
	
	public static <E extends Entity> ArrayList<E> getNeighborsRender(E obj, HashMap<Integer, ArrayList<E>> h, int wid, int hgt, int size) {
		ArrayList<E> neighbors = new ArrayList<>(); int key;
		int x = (int) (obj.getRX()/Room.CHUNK_SIZE), y = (int) (obj.getRY()/Room.CHUNK_SIZE);
		if (x - size < 0) {x = size;} else if (x + size > wid) {x = wid-size;}
		if (y - size < 0) {y = size;} else if (y + size > hgt) {y = hgt-size;}
		for (int i = -size; i < 1+size; i++) {
			for (int j = -size; j < 1+size; j++) {
				int nX = x+i, nY = y+j;
				if (nX <= wid && nY <= hgt && nX >= 0 && nY >= 0) {
					key = hash(nX, nY);
					if (h.containsKey(key)) {neighbors.addAll(h.get(key));}
				}
			}
		} return neighbors;
	}
		
	// PApplet methods
	public static PApplet getApp() {if (ToolKit.app == null) {return new PApplet();} return ToolKit.app;}
	public static int getAppWidth() {if (ToolKit.app == null) {return -1;} return ToolKit.app.width;}
	public static int getAppHeight() {if (ToolKit.app == null) {return -1;} return ToolKit.app.height;}
	public static boolean getHasApp() {return ToolKit.app == null;}
	public static boolean setApp(PApplet app) {if (ToolKit.app != null) {return false;} ToolKit.app = app; return true;}
	public static boolean pushApp() {if (ToolKit.app == null) {return false;} ToolKit.app.push(); return true;}
	public static boolean popApp() {if (ToolKit.app == null) {return false;} ToolKit.app.pop(); return true;}
	public static boolean fillApp(int r, int g, int b) {if (ToolKit.app == null) {return false;} ToolKit.app.fill(ToolKit.app.color(r, g, b)); return true;}
	public static boolean rectApp(float x, float y, float w, float h) {if (ToolKit.app == null) {return false;} ToolKit.app.rect(x, y, w, h); return true;}
	
}





//TODO IMPLEMENT PATHFINDING
// =======================================================================================================================================================================================================

//public static ArrayList<Point> pathfind(float tx, float ty, float cx, float cy, float cw, float ch, Obstacle[] objects, float bx, float by, float bw, float bh) {
//	ArrayList<Point> criticalPoints = ToolKit.findCritPoints(tx, ty, cx, cy, cw, ch, objects, bx, by, bw, bh);
//	if (criticalPoints.size() == 0) {return criticalPoints;}
//	
////	float[] coordsC = criticalPoints.remove(0).getXY();
////	float[] indexIJ = criticalPoints.remove(0).getXY();
////	ArrayList<Path> paths = Engine.exploreObjects(tx, ty, coordsC[0], coordsC[1], cw, ch, objects, bx, by, bw, bh, indexIJ, new ArrayList<Path>());
//	
//	return criticalPoints;
//}
//
//
//@SuppressWarnings("unused")
//private static ArrayList<Path> exploreObjects(float tx, float ty, float cx, float cy, float cw, float ch, Obstacle[] objects, float bx, float by, float bw, float bh, float[] indexIJ, ArrayList<Path> paths) {
//	for (int i = 0; i < 2; i++) {
//		float[] collision = new float[] {};
//		do {
//			
//		} 
//		while(collision.length != 0);
//	}
//	
//	
//	return new ArrayList<Path>();
//}
//
//@SuppressWarnings("unused")
//private static CObstacle[] preCompileGroups() {
//	
//	
//	return new CObstacle[] {};
//}
//
//private static ArrayList<Point> findCritPoints(float tx, float ty, float cx, float cy, float cw, float ch, Obstacle[] objects, float bx, float by, float bw, float bh) {
//	ArrayList<Point> criticalPoints = new ArrayList<Point>();
//	if (!ToolKit.pointRectCollide(tx, ty, bx+cw/2, by+ch/2, bw-cw, bh-ch)) {return criticalPoints;} // Return if target is in invalid spot
//	float[] farList, nearList = new float[] {Float.NEGATIVE_INFINITY};
//	for (int i = 0; i < objects.length; i++) {  // Iterate through each object
//		farList = new float[] {Float.POSITIVE_INFINITY};  // Reset distance farList is checking
//		float[][] collide = ToolKit.lineRectCollide(tx, ty, cx+cw/2, cy+ch/2, objects[i].getX()-cw/2, objects[i].getY()-ch/2, objects[i].getW()+cw, objects[i].getH()+ch);  // Cast line
//		for (int j = 0; j < collide.length; j++) {  // Iterate through each side of the object
//			if (collide[j].length != 0) {
//				float dist = PApplet.dist(tx, ty, collide[j][0], collide[j][1]);
//				if (farList[0] > dist) {farList = new float[] {dist, collide[j][0], collide[j][1], j};}  // Get the farthest points and push it to the farList
//				if (nearList[0] < dist) {nearList = new float[] {dist, collide[j][0], collide[j][1], i, j};}  // Get the nearest points and push it to the nearList
//			}
//		} 
//		Obstacle ObjHitbox = new Obstacle(objects[i].getX()-cw/2, objects[i].getY()-ch/2, objects[i].getW()+cw, objects[i].getH()+ch);
//		if (farList.length != 1) {  // Check if the list has any points
////			criticalPoints.add(ObjHitbox.getTRCorner());
////			criticalPoints.add(ObjHitbox.getTLCorner());
////			criticalPoints.add(ObjHitbox.getBRCorner());
////			criticalPoints.add(ObjHitbox.getBLCorner());
//			
//			// getCorner(boolean topSide, boolean rightSide)
//			switch((int) farList[3]) {  // Get the critical points and push it to the list
//				case 0:  // Top of object
//					criticalPoints.add(ObjHitbox.getCorner(true, true));
//					criticalPoints.add(ObjHitbox.getCorner(true, false));
//					break;
//				case 1:  // Right side of object
//					criticalPoints.add(ObjHitbox.getCorner(true, true));
//					criticalPoints.add(ObjHitbox.getCorner(false, true));
//					break;
//				case 2:  // Bottom of object
//					criticalPoints.add(ObjHitbox.getCorner(false, true));
//					criticalPoints.add(ObjHitbox.getCorner(false, false));
//					break;
//				case 3:  // Left side of object
//					criticalPoints.add(ObjHitbox.getCorner(true, false));
//					criticalPoints.add(ObjHitbox.getCorner(false, false));
//					break;
//			}
//		}
//	} 
//	if (nearList.length == 1) {return new ArrayList<Point>();}
//	boolean ignorePoint;
//	for (int j = 0; j < criticalPoints.size(); j++) {  // Iterate through all crit points
//		ignorePoint = false;  // variable to keep track of if we should skip current point
//		for (int k = 0; k < objects.length; k++) {  // Iterate through the object list
//			if (ToolKit.pointRectCollideNotExact(criticalPoints.get(j).getX(), criticalPoints.get(j).getY(), objects[k].getX()-cw/2, objects[k].getY()-ch/2, objects[k].getW()+cw, objects[k].getH()+ch)) {  
//				ignorePoint = true;  // If yes then we set the ignore variable to true...
//				k = objects.length;  // ..and end the loop
//			}  // Check if the coords are inside 2 objects
//		} if (ignorePoint || !ToolKit.pointRectCollide(criticalPoints.get(j).getX(), criticalPoints.get(j).getY(),  bx+cw/2, by+ch/2, bw-cw, bh-ch)) {criticalPoints.remove(j); j--;}
//	} 
////	criticalPoints.add(0, new Point(nearList[3], nearList[4]));  // Add the indexes to the front
////	criticalPoints.add(0, new Point(nearList[1], nearList[2]));  // Add the closest point to the front
//	return criticalPoints;  // Return critical points with the closest point in the front
//}
//
////private static ArrayList<Point> findCritPoints(float tx, float ty, float cx, float cy, float cw, float ch, Obstacle[] objects, float bx, float by, float bw, float bh) {
////	ArrayList<Point> criticalPoints = new ArrayList<Point>();
////	if (!Engine.pointRectCollide(tx, ty, bx+cw/2, by+ch/2, bw-cw, bh-ch)) {return criticalPoints;} // Return if target is in invalid spot
////	for (int i = 0; i < objects.length; i++) {  // Iterate through each object
////		Obstacle ObjHitbox = new Obstacle(objects[i].getX()-cw/2, objects[i].getY()-ch/2, objects[i].getW()+cw, objects[i].getH()+ch);
////		criticalPoints.add(ObjHitbox.getTRCorner());
////		criticalPoints.add(ObjHitbox.getTLCorner());
////		criticalPoints.add(ObjHitbox.getBRCorner());
////		criticalPoints.add(ObjHitbox.getBLCorner());
////	} boolean ignorePoint;
////	for (int i = 0; i < criticalPoints.size(); i++) {  // Iterate through all crit points
////		ignorePoint = false;  // variable to keep track of if we should skip current point
////		for (int k = 0; k < objects.length; k++) {  // Iterate through the object list
////			if (Engine.pointRectCollideNotExact(criticalPoints.get(i).getX(), criticalPoints.get(i).getY(), objects[k].getX()-cw/2, objects[k].getY()-ch/2, objects[k].getW()+cw, objects[k].getH()+ch)) {  
////				ignorePoint = true;  // If yes then we set the ignore variable to true...
////				k = objects.length;  // ..and end the loop
////			}  // Check if the coords are inside 2 objects
////		} if (ignorePoint || !Engine.pointRectCollide(criticalPoints.get(i).getX(), criticalPoints.get(i).getY(),  bx+cw/2, by+ch/2, bw-cw, bh-ch)) {criticalPoints.remove(i); i--;}
////	} return criticalPoints;  // Return critical points with the closest point in the front
////}

// =======================================================================================================================================================================================================





//// WIP
//public static float[] getLimbNJointCoords(float centerX, float centerY, float[] lengths, float endX, float endY, boolean bendRight) {
////	if (lengths.length <= 3) {return lengths.length == 3? Engine.getLimbCoords(centerX, centerY, lengths[0] + lengths[1], lengths[2], endX, endY, bendRight) : Engine.getLimbCoords(centerX, centerY, lengths[0], lengths[1], endX, endY, bendRight);}
////	float totalLength = 0; float dist = PApplet.dist(centerX, centerY, endX, endY);
////	for (int i = 0; i < lengths.length; i++) {totalLength += lengths[i];}
////	float[] coord = Engine.lineRadius(centerX, centerY, endX, endY, totalLength, true);
////	if (dist > totalLength) {
////		float[] finalC = new float[lengths.length*2];
////		for (int i = 0; i < lengths.length*2; i++) {finalC[i] = i % 2 == 0? coord[0] : coord[1];}
////		return finalC;
////	} 
////	float lengthHalf1 = 0, lengthHalf2 = 0;
////	float[] half1 = new float[lengths.length/2 + lengths.length%2];
////	float[] half2 = new float[lengths.length/2];
////	for (int i = 0; i < lengths.length; i++) {
////		if (i < half1.length) {
////			half1[i] = lengths[i];
////			lengthHalf1 += lengths[i];
////		} else {
////			half2[i - half1.length] = lengths[i];
////			lengthHalf2 += lengths[i];
////		}
////	}
////	float[] coordsBig = Engine.getLimbCoords(centerX, centerY, lengthHalf1, lengthHalf2, coord[0], coord[1], bendRight);
////	half1 = Engine.getArmCoords(centerX, centerY, half1, coordsBig[0], coordsBig[1], bendRight);
////	half2 = Engine.getArmCoords(coordsBig[0], coordsBig[1], half2, endX, endY, bendRight);
////	coord = new float[half1.length + half2.length];
////	for (int i = 0; i < coord.length; i++) {
////		if (i < half1.length) {coord[i] = half1[i];}
////		else {coord[i] = half2[i - half1.length];}
////	}
////	return coord;
//	return new float[] {};
//}





//private static ArrayList<Point> findCritPoints(float tx, float ty, float cx, float cy, float cw, float ch, Obstacle[] objects, float bx, float by, float bw, float bh) {
//	ArrayList<Point> criticalPoints = new ArrayList<Point>();
//	if (!Engine.pointRectCollide(tx, ty, bx+cw/2, by+ch/2, bw-cw, bh-ch)) {return criticalPoints;} // Return if target is in invalid spot
//	float[] farList, nearList = new float[] {Float.NEGATIVE_INFINITY};
//	for (int i = 0; i < objects.length; i++) {  // Iterate through each object
//		farList = new float[] {Float.POSITIVE_INFINITY};  // Reset distance farList is checking
//		float[][] collide = Engine.lineRectCollide(tx, ty, cx+cw/2, cy+ch/2, objects[i].getX()-cw/2, objects[i].getY()-ch/2, objects[i].getW()+cw, objects[i].getH()+ch);  // Cast line
//		for (int j = 0; j < collide.length; j++) {  // Iterate through each side of the object
//			if (collide[j].length != 0) {
//				float dist = PApplet.dist(tx, ty, collide[j][0], collide[j][1]);
//				if (farList[0] > dist) {farList = new float[] {dist, collide[j][0], collide[j][1], j};}  // Get the farthest points and push it to the farList
//				if (nearList[0] < dist) {nearList = new float[] {dist, collide[j][0], collide[j][1], i, j};}  // Get the nearest points and push it to the nearList
//			}
//		} 
//		Obstacle ObjHitbox = new Obstacle(objects[i].getX()-cw/2, objects[i].getY()-ch/2, objects[i].getW()+cw, objects[i].getH()+ch);
//		if (farList.length != 1) {  // Check if the list has any points
//			criticalPoints.add(ObjHitbox.getTRCorner());
//			criticalPoints.add(ObjHitbox.getTLCorner());
//			criticalPoints.add(ObjHitbox.getBRCorner());
//			criticalPoints.add(ObjHitbox.getBLCorner());
//			switch((int) farList[3]) {  // Get the critical points and push it to the list
//				case 0:  // Top of object
//					criticalPoints.add(ObjHitbox.getTRCorner());
//					criticalPoints.add(ObjHitbox.getTLCorner());
//					break;
//				case 1:  // Right side of object
//					criticalPoints.add(ObjHitbox.getTRCorner());
//					criticalPoints.add(ObjHitbox.getBRCorner());
//					break;
//				case 2:  // Bottom of object
//					criticalPoints.add(ObjHitbox.getBRCorner());
//					criticalPoints.add(ObjHitbox.getBLCorner());
//					break;
//				case 3:  // Left side of object
//					criticalPoints.add(ObjHitbox.getTLCorner());
//					criticalPoints.add(ObjHitbox.getBLCorner());
//					break;
//			}
//		}
//	} 
//	if (nearList.length == 1) {return new ArrayList<Point>();}
//	boolean ignorePoint;
//	for (int j = 0; j < criticalPoints.size(); j++) {  // Iterate through all crit points
//		ignorePoint = false;  // variable to keep track of if we should skip current point
//		for (int k = 0; k < objects.length; k++) {  // Iterate through the object list
//			if (Engine.pointRectCollideNotExact(criticalPoints.get(j).getX(), criticalPoints.get(j).getY(), objects[k].getX()-cw/2, objects[k].getY()-ch/2, objects[k].getW()+cw, objects[k].getH()+ch)) {  
//				ignorePoint = true;  // If yes then we set the ignore variable to true...
//				k = objects.length;  // ..and end the loop
//			}  // Check if the coords are inside 2 objects
//		} if (ignorePoint || !Engine.pointRectCollide(criticalPoints.get(j).getX(), criticalPoints.get(j).getY(),  bx+cw/2, by+ch/2, bw-cw, bh-ch)) {criticalPoints.remove(j); j--;}
//	} 
//	criticalPoints.add(0, new Point(nearList[3], nearList[4]));  // Add the indexes to the front
//	criticalPoints.add(0, new Point(nearList[1], nearList[2]));  // Add the closest point to the front
//	return criticalPoints;  // Return critical points with the closest point in the front
//}
