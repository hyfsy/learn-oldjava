package swing.applet;

import java.awt.*;
import java.applet.*;

public class HelloWorldApplet extends Applet {
	public void paint(Graphics g) {
		g.setColor(new Color(255, 0, 0));
		g.drawString("Hello World!!!", 50, 50);
		g.setColor(new Color(255, 255, 0));
		g.fillRect(50, 100, 200, 50);
		g.setColor(new Color(0, 0, 0));
		g.drawOval(50, 200, 200, 200);
	}
}
