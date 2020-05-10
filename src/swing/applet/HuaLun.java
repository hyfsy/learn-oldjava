package swing.applet;

import java.applet.*;

import java.awt.Graphics;

import java.awt.event.*;

public class HuaLun extends Applet implements MouseListener, MouseMotionListener {

	int x1, y1, x2, y2, x3, y3, x4, y4, l1, l2;

	int mx1, my1, mx2, my2;

	int selection = 0;

	public void init() {

		x1 = 100;
		y1 = 320;
		x3 = 300;
		y3 = 320;
		l1 = 100;
		l2 = 200;

		this.addMouseListener(this);

		this.addMouseMotionListener(this);

	}

	public void paint(Graphics g) {

		g.fillRect(100, 100, 200, 20);
		g.drawLine(200, 120, 200, 320);

		g.fillOval(190, 310, 20, 20);
		g.drawOval(100, 220, 200, 200);

		if (l1 <= 0)
			l1 = 0;
		if (l1 >= 300)
			l1 = 300;

		x2 = x1;
		y2 = y1 + l1;
		g.drawLine(x1, y1, x2, y2);

		if (l2 <= 0)
			l2 = 0;
		if (l2 >= 300)
			l2 = 300;

		x4 = x3;
		y4 = y3 + l2;
		g.drawLine(x3, y3, x4, y4);

		g.fillRect(x2 - 40, y2, 80, 40);
		g.drawRect(x4 - 40, y4, 80, 40);

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

		mx1 = e.getX();
		my1 = e.getY();

		if (mx1 >= x2 && mx1 <= x2 + 80 && my1 >= y2 && my1 <= y2 + 40) {

			selection = 1;

		}

		if (mx1 >= x4 && mx1 <= x4 + 80 && my1 >= y4 && my1 <= y4 + 40) {

			selection = 2;

		}

	}

	public void mouseReleased(MouseEvent e) {
		selection = 0;
	}

	public void mouseDragged(MouseEvent e) {

		if (selection == 1) {
			l1 = e.getY() - y1;
			l2 = 300 - l1;
			repaint();
		}

		if (selection == 2) {
			l2 = e.getY() - y3;
			l1 = 300 - l2;
			repaint();
		}

	}

	public void mouseMoved(MouseEvent e) {
	}

}
