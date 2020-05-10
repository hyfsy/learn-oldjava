package swing.applet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZhuanQiu extends Applet implements Runnable, ActionListener
{

    private static final long serialVersionUID = 1L;

    int x1, y1, x2, y2;
    double alph, xx;
    Thread time;
    Button b1, b2;

    @Override
    public void init() {

        x1 = 100;
        y1 = 100;
        alph = 0;
        xx = 0.05;

        b1 = new Button("快速");
        b2 = new Button("慢速");

        add(b1);
        add(b2);

        b1.addActionListener(this);

        b2.addActionListener(this);

        time = new Thread(this);

        time.start();

    }

    @Override
    public void paint(Graphics g) {

        g.fillOval(x1, y1, 10, 10);
        x2 = (int) (x1 + 100 * Math.cos(alph));
        y2 = (int) (y1 - 100 * Math.sin(alph));
        g.fillOval(x2, y2, 30, 30);
        g.drawLine(x1 + 5, y1 + 5, x2 + 15, y2 + 15);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
            }

            alph = alph + xx;

            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b1) {
            xx = xx + 0.05;
        }

        if (e.getSource() == b2) {
            xx = xx - 0.05;
        }

    }

}
