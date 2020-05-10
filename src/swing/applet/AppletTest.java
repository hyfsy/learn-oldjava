package swing.applet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * [applet测试计算器]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public class AppletTest extends Applet implements ActionListener
{

    TextField t1, t2, t3;
    Button b1, b2, b3, b4, b5, b6;

    // Applet程序开始方法
    @Override
    public void init() {
        t1 = new TextField(10);
        t2 = new TextField(10);
        t3 = new TextField(10);
        b1 = new Button("+");
        b2 = new Button("-");
        b3 = new Button("*");
        b4 = new Button("/");
        b5 = new Button("清除");
        b6 = new Button("退出");
        setLayout(new GridLayout(3, 3));
        add(t1);
        add(t2);
        add(t3);
        add(b1);
        add(b2);
        add(b5);
        add(b3);
        add(b4);
        add(b6);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * 和按钮绑定像是点击事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == b1) {
                t3.setText(String.valueOf(Double.parseDouble(t1.getText()) + Double.parseDouble(t2.getText())));
            }
            if (e.getSource() == b2) {
                t3.setText(String.valueOf(Double.parseDouble(t1.getText()) - Double.parseDouble(t2.getText())));
            }
            if (e.getSource() == b3) {
                t3.setText(String.valueOf(Double.parseDouble(t1.getText()) * Double.parseDouble(t2.getText())));
            }
            if (e.getSource() == b4) {
                t3.setText(String.valueOf(Double.parseDouble(t1.getText()) / Double.parseDouble(t2.getText())));
            }
        }
        catch (Exception a) {
            System.out.println("数字输入错误！");
        }
        if (e.getSource() == b5) {
            t1.setText("");
            t2.setText("");
            t3.setText("");
        }
        if (e.getSource() == b6) {
            stop();
        }
    }

}
