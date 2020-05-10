package io;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * 
 * 字符串流
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月8日]
 */
public class StringStream
{

    public static void main(String[] args) {
        countWorld("good good study day day up");
    }

    /**
     * 
     * [统计字符串中单词的个数]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void countWorld(String str) {
        // 获取字符流
        StringReader sr = new StringReader(str);
        // 获取流标记器
        StreamTokenizer st = new StreamTokenizer(sr);
        // 计数用
        int count = 0;

        try {
            // 判断当前的标记是否为字符流的结尾
            while (st.ttype != StreamTokenizer.TT_EOF) {
                //判断标记是否为单词
                if (st.nextToken() == StreamTokenizer.TT_WORD) {
                    count++;
                }
            }
            //关闭字符串流
            sr.close();
            System.out.println("字符个数为："+count);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
