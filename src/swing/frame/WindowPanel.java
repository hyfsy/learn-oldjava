package swing.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class WindowPanel implements ActionListener, ItemListener {
	private JButton b1, b2, b3;
	private JRadioButton but1, but2, but3;
	private JLabel lab1, lab2, lab3, lab4, lab5;
	private JComboBox<String> combo;
	private ImageIcon icon;

	WindowPanel() {
		JFrame fr = new JFrame("Swing 练习");
		fr.setSize(800, 800);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		------------------------------上-------------------------------
		
		JPanel p1 = new JPanel();
		lab1 = new JLabel("请看图片变化");
		p1.add(lab1);
		
//		------------------------------左-------------------------------

		JPanel p2 = new JPanel();
		lab2 = new JLabel("球类选择：");
		b1 = new JButton("篮球");
		b2 = new JButton("排球");
		b3 = new JButton("足球");
		p2.setLayout(new GridLayout(4, 1));
		p2.add(lab2);
		p2.add(b1);
		p2.add(b2);
		p2.add(b3);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		
//		------------------------------右-------------------------------

		JPanel p3 = new JPanel();
		lab3 = new JLabel("城市选择：");
		combo = new JComboBox<String>();
		combo.addItem("常州");
		combo.addItem("大连");
		combo.addItem("苏州");
		p3.setLayout(new GridLayout(4, 1));
		p3.add(lab3);
		p3.add(combo);
		combo.addItemListener(this);
		
//		------------------------------下-------------------------------

		JPanel p4 = new JPanel();
		lab4 = new JLabel("国旗选择：");
		but1 = new JRadioButton("中国");
		but2 = new JRadioButton("美国");
		but3 = new JRadioButton("英国");
		//创建组，实现单选
		ButtonGroup gro = new ButtonGroup();
		gro.add(but1);
		gro.add(but2);
		gro.add(but3);
		p4.setLayout(new GridLayout(1, 4));
		p4.add(lab4);
		p4.add(but1);
		p4.add(but2);
		p4.add(but3);
		but1.addActionListener(this);
		but2.addActionListener(this);
		but3.addActionListener(this);
		
//		------------------------------中-------------------------------

		JPanel p5 = new JPanel();
		icon = new ImageIcon("src/swing/img/meinv.jpg");
		//初始化带图标的标签
		lab5 = new JLabel("图片", icon, JLabel.CENTER);
		p5.add(lab5);
		
		
		//pan面板，这是整个窗口面板
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(p1, BorderLayout.NORTH);
		pan.add(p2, "West");
		pan.add(p3, "East");
		pan.add(p4, "South");
		pan.add(p5, "Center");
		fr.add(pan);

		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			icon = new ImageIcon("src/swing/img/lanqiu.jpg");
			lab5.setIcon(icon);
		}
		if (e.getSource() == b2) {
			icon = new ImageIcon("src/swing/img/paiqiu.jpg");
			lab5.setIcon(icon);
		}
		if (e.getSource() == b3) {
			icon = new ImageIcon("src/swing/img/zuqiu.jpg");
			lab5.setIcon(icon);
		}

		if (e.getSource() == but1) {
			icon = new ImageIcon("src/swing/img/zhongg.jpg");
			lab5.setIcon(icon);
		}
		if (e.getSource() == but2) {
			icon = new ImageIcon("src/swing/img/meig.jpg");
			lab5.setIcon(icon);
		}
		if (e.getSource() == but3) {
			icon = new ImageIcon("src/swing/img/yingg.jpg");
			lab5.setIcon(icon);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == combo) {
			if (combo.getSelectedItem().toString() == "常州") {
				icon = new ImageIcon("src/swing/img/changz.jpg");
				lab5.setIcon(icon);
			}
			if (combo.getSelectedItem().toString() == "大连") {
				icon = new ImageIcon("src/swing/img/dal.jpg");
				lab5.setIcon(icon);
			}
			if (combo.getSelectedItem().toString() == "苏州") {
				icon = new ImageIcon("src/swing/img/suz.jpg");
				lab5.setIcon(icon);
			}
		}
	}
	
	public static void main(String[] args) {
		new WindowPanel();
	}

}

