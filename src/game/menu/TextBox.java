package game.menu;

import game.util.ToolKit;

public class TextBox {

	public TextBox() {
		// TODO Auto-generated constructor stub
	}
	
	public static void display(String text) {
		ToolKit.pushApp();
		ToolKit.fillApp(255, 255, 255);
		ToolKit.rectApp(25, 525, 750, 250);
		ToolKit.fillApp(0, 0, 0);
		ToolKit.rectApp(30, 530, 740, 240);
		ToolKit.popApp();
		ToolKit.getApp().text(text, 50, 575);
	}

}
