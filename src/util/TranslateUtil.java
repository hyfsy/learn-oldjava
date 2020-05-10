package util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class TranslateUtil
{

    /**
     * 翻译，出错返回查询字符串
     * 
     * @param query
     *            查询字符串
     * @param resultType
     *            查询出的结果类型
     * @return 你想要的结果
     */
    public static String getResult(String query, Result resultType) {
        String translate = translate(query);

        if (translate == null) {
            return query;
        }

        Map resultMap = JSON.parseObject(translate);

        if (0 != (Integer) resultMap.get("errorCode")) {
            return query;
        }
        String result = "";

        switch (resultType) {
            case FANYI:
                result = ((List<String>) resultMap.get("translation")).get(0);
                break;
            case PINYIN:
                result = (String) ((Map) resultMap.get("basic")).get("phonetic");
                break;
        }

        return result;
    }

    /**
     * 返回翻译后的JSON字符串
     * 
     * @param query
     *            查询字符串
     * @return JSON字符串
     */
    public static String translate(String query) {

        StringBuffer sb = null;
        BufferedInputStream bis = null;
        try {
            query = URLEncoder.encode(query, "utf-8");
            String link = "http://fanyi.youdao.com/openapi.do?keyfrom=Imnone&key=2100988292&type=data&doctype=json&version=1.1&q=" + query;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            bis = new BufferedInputStream(in);
            sb = new StringBuffer();
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bis != null) {
                try {
                    bis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 结果类型枚举
     */
    static enum Result
    {
        FANYI, PINYIN
    }

}
