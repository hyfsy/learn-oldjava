package test.music;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/04/27
 */
public class FileScanner {

    public static void main(String[] args) {
        String dir = "C:\\Users\\baB_hyf\\Desktop\\song";
        scanner(dir);
    }

    public static void scanner(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        // files = sortFiles(files);

        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    private static File[] sortFiles(File[] files) {
        return Arrays.stream(files).sorted((f1, f2) -> {
            byte[] bytes1 = f1.getName().getBytes(StandardCharsets.UTF_8);
            byte[] bytes2 = f2.getName().getBytes(StandardCharsets.UTF_8);
            return compareByteArray(bytes1, bytes2);
        }).toArray(File[]::new);
    }

    private static int compareByteArray(byte[] bs1, byte[] bs2) {

        int b1Idx = 0;
        int b2Idx = 0;
        int b1End = bs1.length - 1;
        int b2End = bs2.length - 1;

        while (b1Idx <= b1End && b2Idx <= b2End) {

            int compare = Byte.compare(bs1[b1Idx], bs2[b2Idx]);
            if (compare != 0) {
                return compare;
            }

            b1Idx++;
            b2Idx++;
        }

        return Integer.compare(b1End, b2End);
    }
}
