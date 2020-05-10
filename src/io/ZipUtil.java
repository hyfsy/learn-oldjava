package io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 
 * [压缩、解压、切割、合并]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class ZipUtil
{

    public static Integer buffer = 1024;

    public static final Integer KB = 1024;

    public static final Integer MB = 1024 * 1024;

    public static final Integer GB = 1024 * 1024 * 1024;

    public static long cutSize = 50 * KB;

    public static final String ZIP_NAME = "压缩文件";

    public static final String ZIP_SUFFIX = ".zip";

    private static final String path = "C:/Users/baB_hyf/Desktop/【工具】谱面制作工具--By蓝猫/";

    private static final String zipPath = "C:/Users/baB_hyf/Desktop/【工具】谱面制作工具--By蓝.zip";

    private static final String mvPath = "C:/Users/baB_hyf/Desktop/1.mp4";

    private static final String savePath = "C:/Users/baB_hyf/Desktop/1";

    public static void main(String[] args) {

        compress(path, zipPath);

        // decompress(zipPath, path);

        // carve(mvPath, null);
        //
        // List<String> filePathList = getFilePathList(savePath);
        // merge(filePathList, null);

    }

    /**
     * 
     * [压缩文件]
     * 
     * @param path
     * @param zipPath
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void compress(String path, String zipPath) {

        ZipOutputStream zos = null;
        BufferedOutputStream bos = null;
        try {
            File targetFile = new File(path);
            zos = new ZipOutputStream(new FileOutputStream(zipPath));
            bos = new BufferedOutputStream(zos);
            System.out.println("开始压缩...");
            // 添加两个输出流纯粹增强写能力
            zip(zos, bos, targetFile, targetFile.getName());
            System.out.println("压缩完成！");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                zos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * [压缩递归]
     * 
     * @param zos
     * @param bos
     * @param targetFile
     * @param fileName
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static void zip(ZipOutputStream zos, BufferedOutputStream bos, File targetFile, String fileName) throws IOException {
        if (targetFile.isDirectory()) {
            File[] files = targetFile.listFiles();
            if (files.length == 0) {
                // 创建空文件夹
                zos.putNextEntry(new ZipEntry(fileName + File.separator));
            }
            for (File file : files) {
                // 遍历寻找文件夹中文件
                zip(zos, bos, file, fileName + File.separator + file.getName());
            }

        }
        else {
            // 创建空文件
            zos.putNextEntry(new ZipEntry(fileName));
            System.out.println("正在压缩：" + fileName + "......");
            // 将文件写入到输出流中
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(targetFile));
            byte[] bytes = new byte[buffer];
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                bos.flush();
            }
            bis.close();
        }
    }

    /**
     * 
     * [解压文件]
     * 
     * @param zipPath
     * @param path
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void decompress(String zipPath, String path) {
        System.out.println("开始解压...");
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        try {
            zis = new ZipInputStream(new FileInputStream(zipPath));

            ZipEntry entry = null;
            File file = null;
            // 获取压缩包中的一个又一个对象
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("正在解压：" + entry.getName() + "......");

                // 创建空文件夹
                if (entry.getName().endsWith(File.separator)) {
                    file = new File(path, entry.getName());
                    file.mkdirs();
                    continue;
                }

                // 通过路径+名称生成文件对象
                file = new File(path, entry.getName());

                if (!file.exists()) {
                    new File(file.getParent()).mkdirs();
                }

                bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] bytes = new byte[buffer];
                int len = -1;
                while ((len = zis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                }
                bos.close();
            }
            System.out.println("解压成功！");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (zis != null) {
                    zis.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * [切割文件]
     * 
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void carve(String sourceFilePath, String savePath) {
        if (sourceFilePath == null) {
            return;
        }

        // 获取文件默认保存路径
        if (savePath == null) {
            savePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf(".")) + File.separator;
        }
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }

        // 获取文件后缀
        String prefix = sourceFilePath.substring(sourceFilePath.lastIndexOf("."), sourceFilePath.length());

        File file = new File(sourceFilePath);
        BufferedInputStream bis = null;

        BufferedOutputStream bos = null;

        // 获取切割的次数
        int cutNum = file.length() % cutSize == 0 ? (int) (file.length() / cutSize) : (int) (file.length() / cutSize) + 1;
        System.out.println("总共要切割" + cutNum + "个文件");

        int len = -1;

        byte[] bytes = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            // 按次数循环切割文件
            for (int i = 0; i < cutNum; i++) {
                System.out.println("正在切割第" + (i + 1) + "个文件......");

                // 创建切割文件
                String fileName = savePath + file.getName().substring(0, file.getName().lastIndexOf(".")) + "-" + (i + 1) + prefix;
                File subFile = new File(fileName);
                if (!subFile.getParentFile().exists()) {
                    subFile.getParentFile().mkdirs();
                }

                bos = new BufferedOutputStream(new FileOutputStream(subFile));

                // 读取流循环次数
                int count = 0;

                // 动态创建读取字节
                // 此处注意count
                if (cutSize < buffer) {
                    bytes = new byte[(int) cutSize];
                    count = 0;
                }
                else {
                    bytes = new byte[buffer];
                    count = (int) (cutSize / buffer);
                }

                while (count > 0 && (len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                    bos.flush();
                    count--;
                }

                // 判断文件是否没读完
                if (cutSize % buffer != 0) {
                    bytes = new byte[(int) cutSize % buffer];
                    len = bis.read(bytes);
                    bos.write(bytes, 0, len);
                }
                bos.flush();
                bos.close();
            }
            System.out.println("切割完成！！！");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 
     * [合并文件]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void merge(List<String> filePathList, String saveFilePath) {
        if (filePathList == null || filePathList.size() <= 0) {
            return;
        }

        // 获取文件默认保存路径
        if (saveFilePath == null) {
            String path = filePathList.get(0);
            saveFilePath = path.substring(0, path.lastIndexOf("-")) + path.substring(path.lastIndexOf("."), path.length());
        }

        // 可创建合并流所需元素
        Vector<InputStream> vector = new Vector<>();
        SequenceInputStream sis = null;
        BufferedOutputStream bos = null;
        try {
            System.out.println("开始合并文件......");

            for (String path : filePathList) {
                InputStream is = new FileInputStream(path);
                vector.add(is);
            }

            // 获取创建合并流所需元素
            Enumeration<InputStream> elements = vector.elements();
            // 创建合并流
            sis = new SequenceInputStream(elements);

            bos = new BufferedOutputStream(new FileOutputStream(saveFilePath));

            int len = -1;
            byte[] bytes = new byte[buffer];

            while ((len = sis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                bos.flush();
            }
            System.out.println("合并完成！");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sis != null) {
                try {
                    sis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 
     * [获取对应文件夹下的所有文件路径]
     * 
     * @param sourcesPath
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getFilePathList(String sourcesPath) {
        if (sourcesPath == null) {
            return null;
        }
        if (!sourcesPath.endsWith(File.separator)) {
            sourcesPath += File.separator;
        }

        File sources = new File(sourcesPath);
        if (sources.exists()) {
            // 获取文件夹下的所有文件
            File[] listFiles = sources.listFiles();
            if (listFiles.length > 0) {
                List<String> pathList = new ArrayList<>();
                for (File file : listFiles) {
                    // 将文件的路径添加到列表中
                    pathList.add(file.getPath());
                }
                return pathList;
            }
        }
        return null;

    }
}
