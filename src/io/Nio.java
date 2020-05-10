package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;


/**
 * 
 *  [以块的形式传输数据，提高效率] 
 *  
 *  map > nio > bis > fis
 *  
 * @author baB_hyf
 * @version [版本号, 2019年9月9日]
 */
public class Nio
{
    
    private static File file1=new File("E:\\ga\\snkll\\SuoNiKe LiLiang-3.bin");
    private static File file2=new File("E:\\3.bin");
    
    public static void main(String[] args) {
        try {
            long t1 = System.currentTimeMillis();
            nioCopyFile();
            long t2 = System.currentTimeMillis();
            System.out.println("nio共花时间："+(t2-t1)/1000+"秒");
            
            long t3 = System.currentTimeMillis();
            mapCopyFile();
            long t4 = System.currentTimeMillis();
            System.out.println("map共花时间："+(t4-t3)/1000+"秒");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     *  [nio复制文件] 
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void nioCopyFile() throws Exception {
        //数据通道
        FileChannel fcin = new FileInputStream(file1).getChannel();
        FileChannel fcout = new FileOutputStream(file2).getChannel();
        //设置缓冲区初始大小
        //不止这一种，存在七种基本数据类型缓冲区类
        ByteBuffer buf=ByteBuffer.allocate(1024);
        while(fcin.read(buf)!=-1) {
            /**
             * buf.flip():翻转缓冲区
             * 
             * 4条数据：
             * buf.capacity()==>4
             * buf.position()==>1024
             * buf.limit()==>1024
             * 反转后：
             * buf.capacity()==>0
             * buf.position()==>4
             * buf.limit()==>1024
             * 
             */
            
            buf.flip();
            fcout.write(buf);
            buf.clear();
        }
        fcout.close();
        fcin.close();
        
    }
    
    /**
     * 
     *  [nio内存映射复制文件]     
     * @throws Exception 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void mapCopyFile() throws Exception {
        //被nio的内存映射替代
        RandomAccessFile rafin=new RandomAccessFile(file1, "r");
        RandomAccessFile rafout=new RandomAccessFile(file2, "rw");
        //获取通道
        FileChannel fcin = rafin.getChannel();
        FileChannel fcout = rafout.getChannel();
        //获取数据总大小
        long size = fcin.size();
        
        //映射缓冲区
        MappedByteBuffer bufin = fcin.map(MapMode.READ_ONLY, 0, size);
        MappedByteBuffer bufout = fcout.map(MapMode.READ_WRITE, 0, size);
        
        for(int i=0;i<size;i++) {
            //读取该缓冲区当前位置的字节，然后增加位置
            bufout.put(bufin.get());
        }
        
        //关闭时会写入块数据
        fcin.close();
        fcout.close();
        rafin.close();
        rafout.close();
    }


}
