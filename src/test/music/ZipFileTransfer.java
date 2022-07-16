package test.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 转换为压缩文件
 *
 * @author baB_hyf
 * @date 2022/04/27
 */
public class ZipFileTransfer {

    public static void main(String[] args) {
        String dir = "C:\\Users\\baB_hyf\\Desktop\\song1";
        transfer(dir);
    }

    public static void transfer(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        // 压缩
        for (File file : files) {
            if (file.isFile()) {
                continue;
            }

            String zipSavePath = file.getAbsolutePath() + ".zip";

            if (file.isDirectory()) {
                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipSavePath))) {
                    zip(file, zos, "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // store
        String outputFileName = "0_output";
        File output = new File(dir, outputFileName);
        if (!output.mkdirs()) {
            throw new RuntimeException("create new file failed: " + output.getAbsolutePath());
        }

        // 聚合
        files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.getName().endsWith(".zip")) {
                continue;
            }

            StringBuilder sb = new StringBuilder(file.getAbsolutePath());
            int i = file.getAbsolutePath().lastIndexOf(File.separator);
            if (i != -1) {
                sb.insert(i, File.separator + outputFileName);
                if (!file.renameTo(new File(sb.toString()))) {
                    System.out.println("rename file failed: " + sb.toString());
                }
            }
        }
    }

    private static void zip(File file, ZipOutputStream zos, String zipPath) throws IOException {
        if (file.isDirectory()) {
            if (!"".equals(zipPath)) {
                zos.putNextEntry(new ZipEntry(zipPath + file.getName() + "/")); // 添加目录，这边使用uri形式的/，不然解压会有问题
            }
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File child : files) {
                    // zip(child, zos, zipPath + file.getName() + "/");
                    zip(child, zos, zipPath);
                }
            }
        }
        else {
            ZipEntry zipEntry = new ZipEntry(zipPath + file.getName());
            zos.putNextEntry(zipEntry);
            try (FileInputStream fis = new FileInputStream(file)) {
                int len;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            }
            zos.closeEntry();
        }
    }
}
