package test.music;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author baB_hyf
 * @date 2022/04/28
 */
public class FileDiscriminator {

    public static void main(String[] args) {
        String dir = "C:\\Users\\baB_hyf\\Desktop\\song";

        String fileNames = "";

        discriminate(dir, fileNames);
    }

    public static void discriminate(String dirPath, String fileNames) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        StringTokenizer sk = new StringTokenizer(fileNames, "\n");
        List<String> fileNameList = new ArrayList<>();
        while (sk.hasMoreTokens()) {
            fileNameList.add(sk.nextToken());
        }

        // File output = new File(dir, "0_output");
        // if (!output.mkdirs()) {
        //     throw new RuntimeException("failed to mkdirs:" + output);
        // }

        for (File file : files) {
            // if (fileNameList.contains(file.getName())) {
            //     if (!file.renameTo(new File(dir, "0_output"  + File.separator + file.getName()))) {
            //         System.out.println("failed to rename file: " + file);
            //     }
            // }

            if (!fileNameList.contains(file.getName())) {

                List<File> processed = new ArrayList<>();

                Stack<File> fileStack = new Stack<>();
                fileStack.push(file);

                while (!fileStack.isEmpty()) {
                    File f = fileStack.peek();
                    if (f.isDirectory() && !processed.contains(f)) {
                        processed.add(f);
                        File[] cs = f.listFiles();
                        if (cs != null) {
                            for (File c : cs) {
                                fileStack.push(c);
                            }
                            continue;
                        }
                    }
                    fileStack.pop();
                    if (!f.delete()) {
                        System.out.println("failed to delete file: " + f);
                    }
                }
            }
        }
    }
}
