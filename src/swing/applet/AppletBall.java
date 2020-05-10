package swing.applet;

import java.awt.*;

import java.applet.*;

import java.awt.event.*;

public class AppletBall extends Applet implements Runnable, ActionListener {

	private Image imgBall, buff; // 红球图片与缓冲变量

	private Scrollbar vCon;// 调整球初始速度的滚动条

	private Thread ballThread;// 移动球的线程

	private double S, V, T;// S=球的垂直位移,V=初始速度,T=时间

	private static final double G = 9.8;// 重力加速度

	private Graphics2D ga, gb;// Applet与buffer的Graphics对象

	private int width, height;// Applet的宽度与高度

	private Button b = new Button("上抛");// 上抛按钮

	private Paint skyPaint = new GradientPaint(0, 0, Color.WHITE, 0, -500, Color.BLUE, false);// 表现天空的Paint对象

	public void init() {

		width = getWidth();// 获取Applet的宽度和高度

		height = getHeight();

		imgBall = getImage(getDocumentBase(), "images/Red_Ball.gif");// 获取红球图片

		getToolkit().prepareImage(imgBall, -1, -1, this);// 准备图片

		buff = createImage(width, height);// 创建Applet大小 的buff

		vCon = new Scrollbar(Scrollbar.HORIZONTAL, 50, 1, 0, 100);// 创建并添加组件

		setLayout(new BorderLayout());

		Panel p = new Panel();

		p.setLayout(new BorderLayout());

		p.add(vCon, "North");

		p.add(b, "Center");

		add(p, "South");

		b.addActionListener(this);// 注册动作事件监听器

	}

	public void paint(Graphics g) {

		if (ga == null)

			ga = (Graphics2D) getGraphics();// 获取Applet的Graphics对象

		if (gb == null) {

			gb = (Graphics2D) buff.getGraphics();// 获取buff的Graphics对象

			gb.translate(width / 2, height / 2);// 将buff的原点移至中央

		}

		drawScreen(); // 绘制画面

	}

	// 点击上抛按钮,调用下面方法

	public void actionPerformed(ActionEvent ae) {

		if (ballThread == null || !ballThread.isAlive()) {// 若线程 为null或死亡

			S = 0;// 初始化变量

			V = vCon.getValue();

			T = 0;

			ballThread = new Thread(this);// 创建线程

			ballThread.start();// 启动线程

		}

	}

	public void drawScreen() { // 绘制画面

		gb.setPaint(new Color(200, 200, 50));// 表现地面的Paint

		gb.fillRect(-width / 2, 0, width, height / 2);// 绘制地面

		gb.setPaint(skyPaint);

		gb.fillRect(-width / 2, -height / 2, width, height / 2);// 绘制天空

		gb.drawImage(imgBall, 0, -(int) S, null);// 将球绘制至(0,-S)位置

		gb.drawString("T= " + String.valueOf(T), -100, 50);// 将时间显示于画面中

		gb.drawString("S= " + String.valueOf(S), -100, 65);// 显示球的位置

		ga.drawImage(buff, 0, 0, this);// 将 buff中的图像绘制至Applet中

	}

	public void run() {

		while (true) {

			T = T + 0.1; // 时间增加0.1秒

			S = V * T - 0.5 * G * T * T;// 计算球的位置

			drawScreen();// 绘制画面

			if ((int) S <= 0)
				break;// 若S<=0,则终止线程

			try {

				Thread.sleep(100);// 间隔0.1秒

			} catch (Exception e) {
			}

		}

	}

}
