package test.music;

import java.io.File;

/**
 * 文件夹层级归类
 *
 * @author baB_hyf
 * @date 2022/04/27
 */
public class FileClassifier {

    public static void main(String[] args) {
        String dir = "C:\\Users\\baB_hyf\\Desktop\\song";
        classifier(dir);
    }

    public static void classifier(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) { // only directory
                File[] children = file.listFiles();
                if (children.length == 1 && children[0].isDirectory()) { // only one child directory
                    File childDir = children[0];
                    File[] childFiles = childDir.listFiles();
                    if (childFiles != null) {
                        for (File childFile : childFiles) {
                            childFile.renameTo(new File(file, childFile.getName()));
                        }
                        if (!childDir.delete()) {
                            System.out.println("dir delete failed: " + childDir);
                        }
                    }
                }
            }
        }
    }
}
