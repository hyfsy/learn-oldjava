package swing.frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * [框架结构]
 * 
 * 实现MouseListener接口，进行鼠标事件监听
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月15日]
 */
public class FrameTest implements MouseListener, ActionListener
{
    // 方便用于设置监听事件等
    // Hello World用
    private JFrame frame = new JFrame("第一个测试");
    private JPanel panel = new JPanel();;
    private JButton changeColor;
    private JTextField textField;

    // 菜单例子用
    private JMenuBar menuBar;
    private JMenu menu;
    // 可创建成二维数组类型，添加更快捷
    private JMenuItem exit;
    private JMenuItem pretty;

    // 登录用
    private JLabel username = new JLabel("用户名");
    private JLabel password = new JLabel("密码");
    private JTextField enterUsername = new JTextField();
    private JPasswordField enterPassword = new JPasswordField();
    private JButton checkUser = new JButton("验证身份");
    // 三次错误限制
    private static int checkNum = 1;

    public FrameTest() {
        // 执行关闭操作时，停止当前进程
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2));
        // 设置窗口的位置及大小
        frame.setBounds(50, 50, 1000, 500);

        changeColor = new JButton("点击我改变颜色");
        // 创建一个20列的文本框
        textField = new JTextField(changeColor.getText(), 20);
        textField.requestFocus();
        // 增加监听事件
        changeColor.addMouseListener(this);

        // -----------------------------菜单功能-----------------------------------------------

        // 添加菜单
        exit = new JMenuItem("退出应用");
        pretty = new JMenuItem("查看美女", new ImageIcon("src/swing/img/meinv.jpg"));
        // 增加监听事件
        exit.addActionListener(this);
        menu = new JMenu("操作");
        menuBar = new JMenuBar();
        menu.add(pretty);
        // 分隔符
        menu.addSeparator();
        menu.add(exit);
        menuBar.add(menu);

        // ---------------------------------验证身份-------------------------------------------

        // 将表单对象添加到容器内
        panel.add(textField);
        panel.add(changeColor);
        frame.add(menuBar);
        frame.add(panel);

        checkUser.addActionListener(this);
        frame.add(username);
        frame.add(enterUsername);
        frame.add(password);
        frame.add(enterPassword);
        frame.add(checkUser);

        // 显示 默认关闭
        frame.setVisible(true);
    }

    // 鼠标点击按钮???
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == changeColor) {
            panel.setBackground(new Color(0, 234, 12));
        }
    }

    // 鼠标放在按钮上
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == changeColor) {
            panel.setBackground(Color.BLUE);
        }
    }

    // 鼠标离开按钮
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == changeColor) {
            panel.setBackground(Color.WHITE);
        }
    }

    // 点击按钮执行操作
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == changeColor) {
            panel.setBackground(Color.RED);
        }
    }

    // 鼠标点击后放开
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == changeColor) {
            // text.setText("Released");
            panel.setBackground(Color.green);
        }
    }

    // 点击菜单触发事件
    @Override
    public void actionPerformed(ActionEvent e) {
        // 退出功能
        if (e.getSource() == exit) {
            System.exit(0);
        }
        // 验证身份功能
        else if (e.getSource() == checkUser) {
            checkNum++;
            if (checkNum <= 3 && "admin".equals(enterUsername.getText()) && "admin".equals(String.valueOf(enterPassword.getPassword()))) {
                JOptionPane.showMessageDialog(null, "登录成功", "系统提醒", 3);
                checkUser.setEnabled(false);
            }
            else {
                JOptionPane.showMessageDialog(null, "登录失败", "系统提醒", 0);
                if (checkNum > 3) {
                    checkUser.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "您不能再输入了");
                }
            }
        }
    }

    public static void main(String[] args) {
        new FrameTest();
    }

}
