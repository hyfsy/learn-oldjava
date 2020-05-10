package swing.applet;

import java.awt.*;

import java.applet.*;

import java.awt.Event.*;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import java.awt.event.MouseMotionListener;

import java.awt.geom.*;

public class AppletRectangle extends Applet implements MouseListener, MouseMotionListener {

	Rectangle2D[] p = new Rectangle2D.Double[3];// 三角形的三个调节点

	Image buff;

	Graphics2D ga, gb;// Applet与buffer的Graphics对象

	double dx, dy;// 用鼠标按下调节点时,鼠标的位置与调节点的(x,y)的距离

	int selection = -1;// 用鼠标选中的调节点的编号(0-2),若未选中,则返回-1

	public void init() {

		buff = createImage(getWidth(), getHeight());// 创建buffer对象

		gb = (Graphics2D) buff.getGraphics();// 获取buffer的Grapghics对象

		p[0] = new Rectangle2D.Double(150, 50, 5, 5);// 创建调节点对象

		p[1] = new Rectangle2D.Double(60, 200, 5, 5);

		p[2] = new Rectangle2D.Double(240, 200, 5, 5);

		addMouseMotionListener((MouseMotionListener) this);// 处理鼠标移动事件

		addMouseListener(this);// 处理鼠标事件

	}

	public void paint(Graphics g) {

		if (ga == null)

			ga = (Graphics2D) getGraphics();// 获取Applet的Graphics对象

		drawScreen();// 绘制画面

	}

	public void drawScreen() {// 绘制画面的方法

		gb.clearRect(0, 0, getWidth(), getHeight());// 擦除buffer图像

		GeneralPath triangle = new GeneralPath();// 三角形对象

		// 确定三角形各顶点的坐标,然后画线

		triangle.moveTo((float) p[0].getX(), (float) p[0].getY());

		triangle.lineTo((float) p[1].getX(), (float) p[1].getY());

		triangle.lineTo((float) p[2].getX(), (float) p[2].getY());

		triangle.closePath();// 关闭三角形路径

		// 向buffer绘制图形

		gb.setPaint(new Color(204, 205, 255));// 使用指定的颜色填充三角形

		gb.fill(triangle);

		gb.setPaint(new Color(0, 0, 0));// 使用黑色绘制三角形的轮廓线

		gb.draw(triangle);

		gb.setPaint(Color.red);// 设定Paint为红色

		for (int i = 0; i < p.length; i++) {// 绘制实心调节点

			gb.fill(p[i]);

		}

		ga.drawImage(buff, 0, 0, this); // 向Applet绘制buffer的图像

	}

	public void mousePressed(MouseEvent me) {// 按下鼠标时被调用

		int mx = me.getX(), my = me.getY();// 获取鼠标坐标

		// 查找被鼠标选中的调节点

		for (int i = 0; i < p.length; i++)

			if (p[i].contains(mx, my)) {// 若鼠标位于第i调节点内

				// 计算鼠标与调节点的距离

				dx = mx - (int) p[i].getX();

				dy = my - (int) p[i].getY();

				selection = i;// 保存被选中的调节点编号

				break;

			}

	}

	public void mouseDragged(MouseEvent me) {// 拖动鼠标时被调用

		if (selection >= 0 && selection <= p.length) {// 若选中的调节点存在

			int mx = me.getX(), my = me.getY();// 获取鼠标坐标

			p[selection].setFrame(mx - dx, my - dy, 5, 5);// 移动调节点坐标

			drawScreen();// 绘制画面

		}

	}

	public void mouseReleased(MouseEvent me) {// 释放鼠标时被调用

		selection = -1;// 未选中调节点

	}

	public void mouseMoved(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {
	}

}