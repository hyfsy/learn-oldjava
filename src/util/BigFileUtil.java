package util;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author baB_hyf
 * @date 2020/03/03
 */
public class BigFileUtil {

    public static final long MB = 1L << 20;
    public static final long GB = 1L << 30;

    /**
     * 我的电脑上能创建的数组的最大大小
     * byte[] bytes = new byte[1402470384];
     */
    public static final int MAX_ARRAY_INIT_SIZE_MY = 1402470384;

    /**
     * 固定的缓冲数组大小，不可更改
     */
    private static final int BUFFER_SIZE = (Integer.MAX_VALUE >> 1) + 1;

    /**
     * 切割文件生成的默认后缀分隔符
     */
    private static final String DEFAULT_SPLIT = "-";


    public void splitFile(String file, String dir, long splitSize) throws Exception {

        if (isBlank(file) || isBlank(dir)) {
            throw new IllegalArgumentException("input args is empty");
        }

        // 处理文件夹路径
        legalDirectory(dir);

        // 文件输入流通道
        FileChannel inChannel = FileChannel.open(Paths.get(file), StandardOpenOption.READ);


        long pos                        = 0;                        // 读通道定位指针，贯穿整个文件操作
        long size                       = inChannel.size();         // 指定的处理文件的大小
        long remainder                  = size % splitSize;         // 切割剩余字节数
        long splitNum                   = size / splitSize;         // 切割文件数，不包括剩余
        String fileName                 = new File(file).getName(); // 文件名[分割新文件时使用]
        int suffix                      = (int) splitNum;           // 文件名称的索引数标识[分割新文件时使用]
        MappedByteBuffer inMappedBuf    = null;                     // 读通道缓冲区
        MappedByteBuffer outMappedBuf   = null;                     // 写通道缓冲区
        FileChannel outChannel          = null;                     // 文件输出流通道
        byte[] bytes                    = null;                     // 内存映射缓冲数组


        // 开始循环切割指定大小的文件
        while (splitNum >= 0) {

            // 单个切割文件大小，由于需要做减少处理，在此又定义一次
            long everySize = splitSize;

            // 是否需要处理总文件最后的剩余文件
            if (splitNum == 0) {
                if (remainder <= 0) {
                    // 结束处理，退出主要的外层循环
                    break;
                } else {
                    // 将当前切割文件大小置为剩余文件的字节大小
                    everySize = remainder;
                }
            }

            // 写通道定位指针
            long offset = 0;
            // 单个切割文件需要循环切则创建该缓存大数组
            if (everySize > BUFFER_SIZE) {
                bytes = new byte[BUFFER_SIZE];
            }

            // 获取切割出的文件全路径
            String targetPath = getTargetPath(dir, fileName, suffix - splitNum);
            // 初始化输出流通道，仅当前切割的文件使用，用完即关闭
            outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            // 由于 Integer.MAX_VALUE 长度限制，所以需要内部while循环（+1作用于此）
            while (everySize > BUFFER_SIZE) {

                // 获取映射字节缓冲区
                inMappedBuf = inChannel.map(MapMode.READ_ONLY, pos, BUFFER_SIZE);
                outMappedBuf = outChannel.map(MapMode.READ_WRITE, offset, BUFFER_SIZE);

                // 核心的处理执行，先写后读
                long midT2 = System.currentTimeMillis();
                inMappedBuf.get(bytes);
                long midT3 = System.currentTimeMillis();
                outMappedBuf.put(bytes);
                long midT4 = System.currentTimeMillis();

                System.out.println("*循环写：" + (midT3 - midT2));
                System.out.println("循环读：" + (midT4 - midT3));

                // 切割文件引用大小减少固定块大小
                everySize -= BUFFER_SIZE;
                // 读通道定位指针向后偏移
                pos += BUFFER_SIZE;
                // 写通道定位指针向后偏移
                offset += BUFFER_SIZE;

            }

            // 方便GC，否则OOM
            bytes = null;

            // 处理单次切割的文件最后的剩余
            if (everySize > 0) {

                // 获取映射字节缓冲区
                inMappedBuf = inChannel.map(MapMode.READ_ONLY, pos, everySize);
                outMappedBuf = outChannel.map(MapMode.READ_WRITE, offset, everySize);

                // 重设缓冲数组为剩余文件大小
                bytes = new byte[inMappedBuf.limit()];

                // 核心的处理执行，先写后读
                long midT5 = System.currentTimeMillis();
                inMappedBuf.get(bytes);
                long midT6 = System.currentTimeMillis();
                outMappedBuf.put(bytes);
                long midT7 = System.currentTimeMillis();

                System.out.println("*写：" + (midT6 - midT5));
                System.out.println("读：" + (midT7 - midT6));

                // 读通道定位指针 向后偏移 当前处理文件大小
                pos += everySize;
            }

            // 每次切割完毕都关闭当前文件流输出通道
            outChannel.close();

            // 切割的总文件数减少
            splitNum--;
        }

        // 关闭文件输入流通道
        inChannel.close();
    }

    /**
     * 自动为目录添加 /
     * 目录不存在则自动创建该目录
     *
     * @param dir 目录路径
     * @throws IOException 不存在该目录并且目录创建失败
     */
    private void legalDirectory(String dir) throws IOException {
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }

        File directory = new File(dir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("创建目录失败:" + dir);
            }
        }
    }

    /**
     * 获取默认的文件名称
     *
     * @param dir  目录名称
     * @param name 文件名称
     * @param num  后缀
     * @return 保存文件全路径
     */
    private String getTargetPath(String dir, String name, long num) {
        StringBuilder sb = new StringBuilder(dir).append(name);
        int index = sb.lastIndexOf(".");
        String suffix = DEFAULT_SPLIT + String.valueOf(num);
        if (index == -1) {
            sb.append(suffix);
        } else {
            sb.insert(sb.lastIndexOf("."), suffix);
        }
        return sb.toString();
    }

    /**
     * 校验字符串是否为null和空
     *
     * @param str 校验的字符串
     * @return 如果为null或空的，则返回true，否则返回false
     */
    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

}
