package util;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class FileUtil
{
    private static String[] sizesType = {"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };

    public static void play() {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] listRoots = File.listRoots();
        for (File file : listRoots) {

            System.out.println(fsv.getSystemDisplayName(file));
            long freeSpace = file.getFreeSpace();
            System.out.println(freeSpace);

        }
    }

    /**
     * 将字节数转换为对应的后缀
     * 
     * @param bytes
     * @return
     */
    public static String formatByte(long bytes) {

        double m = Math.log(bytes) / Math.log(1024);// 得到bytes是1024的多少次幂
        double floor = Math.floor(m);// 向下取整
        double cf = Math.pow(1024, floor);// 1024的floor次方
        String format = String.format("%.2f", bytes / cf);
        format += sizesType[(int) floor];
        return format;
    }

    /**
     * 获取路径创建文件/文件夹
     * 
     * @param path
     *            路径
     * @return 文件/文件夹对象
     */
    protected static File createFileOrDir(String path) {
        File file = null;
        try {
            file = new File(path);
            int index = path.lastIndexOf(File.separator);
            if (index == -1) {
                return null;
            }
            // 获取\后的字符串
            String sufPath = path.substring(index + 1, path.length());

            // 创建文件
            if (sufPath == "" || sufPath.contains(".")) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            // 创建文件夹
            else {
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        }
        catch (Exception e) {
            throw new RuntimeException("文件路径错误！");
        }
    }

    /**
     * 删除包含该路径的所有文件
     * 
     * @param resourcesPathFile
     *            资源路径文件
     */
    public static void deleteTempFile(File resourcesPathFile) {
        // 删除源文件
        if (resourcesPathFile != null) {
            if (resourcesPathFile.isDirectory()) {
                File[] files = resourcesPathFile.listFiles();
                for (int i = 0, len = files.length; i < len; i++) {
                    deleteTempFile(files[i]);
                }
                resourcesPathFile.delete();
            }
            else {
                resourcesPathFile.delete();
            }
        }
    }

}
