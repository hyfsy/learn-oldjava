package swing.applet;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Hual extends Applet implements MouseListener, MouseMotionListener {
	int x1 = 100, y1 = 620, x2 = 300, y2 = 670;
	int l1 = 200, l2 = 250;
	int sel = 0;

	public void init() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void paint(Graphics g) {
		y1 = 420 + l1;
		y2 = 420 + l2;
		g.setColor(new Color(255, 0, 0));
		g.fillRect(100, 100, 200, 20);
		g.drawLine(200, 120, 200, 420);
		g.drawOval(100, 320, 200, 200);
		g.fillOval(180, 400, 40, 40);

		g.drawLine(100, 420, x1, y1);
		g.drawLine(300, 420, x2, y2);
		g.fillRect(x1 - 25, y1, 50, 30);
		g.drawRect(x2 - 25, y2, 50, 30);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		int mx, my;
		mx = e.getX();
		my = e.getY();
		if (mx >= x1 - 25 && mx <= x1 + 25 && my >= y1 && my <= y1 + 30)
			sel = 1;
		if (mx >= x2 - 25 && mx <= x2 + 25 && my >= y2 && my <= y2 + 30)
			sel = 2;
	}

	public void mouseReleased(MouseEvent e) {
		sel = 0;
	}

	public void mouseDragged(MouseEvent e) {
		int mx, my;
		if (sel == 1) {
			mx = e.getX();
			my = e.getY();
			l1 = my - 420;
			if (l1 <= 0)
				l1 = 0;
			else if (l1 >= 450)
				l1 = 450;
			l2 = 450 - l1;
			repaint();
		}
		if (sel == 2) {
			mx = e.getX();
			my = e.getY();
			l2 = my - 420;
			if (l2 <= 0)
				l2 = 0;
			else if (l2 >= 450)
				l2 = 450;
			l1 = 450 - l2;
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {

	}
}
