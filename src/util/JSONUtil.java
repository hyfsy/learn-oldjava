package util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON工具类
 * 
 * @author baB_hyf
 *
 */
public class JSONUtil
{

    /**
     * 将对象转换成JSON字符串（包括时间转换）
     * 
     * @param o
     *            对象
     * @return
     */
    public static String parseJson(Object o) {
        String result = JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);

        return result;
    }

    /**
     * 向前台返回JSON字符串
     * 
     * @param response
     * @param json
     *            字符串
     */
    public static void writeJson(HttpServletResponse response, String json) {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        pw.println(json);
        pw.flush();
        pw.close();

    }

    /**
     * 向前台返回JSON字符串
     * 
     * @param response
     * @param o
     *            对象
     */
    public static void writeJson(HttpServletResponse response, Object o) {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        pw.println(parseJson(o));
        pw.flush();
        pw.close();

    }

}
