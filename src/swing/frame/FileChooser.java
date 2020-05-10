package swing.frame;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooser
{

    public static void main(String[] args) {
        go();
    }

    public static void go() {
        JFrame frame = new JFrame("文件选择");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(frame);
        File selectedFile = chooser.getSelectedFile();
        System.out.println(selectedFile.getName());
    }

}
