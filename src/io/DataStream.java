package io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 与机器无关，从底层输入流中读取java基本类型的数据
 *  [数据操作流] 
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
public class DataStream
{
    public static void main(String[] args){
        writeReadData();
    }

    /**
     * 
     *  [写入并读取数据]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void writeReadData() {
        File file=new File("e:/data.txt");
        try {
            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            //可能浪费空间
            dos.writeInt(10);//写入4个字节
            dos.writeUTF("测试");//以utf形式写入
            dos.close();
            
            DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //必须按照写入的顺序读取
            int i = dis.readInt();
            String s = dis.readUTF();
            dis.close();
            System.out.println(i+" "+s);
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
