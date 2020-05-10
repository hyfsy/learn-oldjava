package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * @ClassName: MiniHelper
 * @Description: miniui传参帮助类
 * @author baB_hyf
 * @date 2019年8月24日
 *
 */
public class MiniHelper
{
    // 存放查询数据
    Map<String, String> objMap;
    String sortField;
    String sortOrder;
    String pageIndex;
    String pageSize;
    // 默认排序字段
    private static final String DEFAULT_SORT_FIELD = "addDate";
    private static final String DEFAULT_SORT_ORDER = "desc";

    public MiniHelper() {
        super();
    }

    /**
     * 通过请求对象获取参数
     * 
     * @param req
     */
    public MiniHelper(HttpServletRequest req) {
        this.sortField = req.getParameter("sortField");
        this.sortOrder = req.getParameter("sortOrder");
        this.pageIndex = req.getParameter("pageIndex");
        this.pageSize = req.getParameter("pageSize");
    }

    /**
     * 
     * @Title: putMap
     * @Description: 添加查询参数
     * @param key
     * @param value
     */
    public void putMap(String key, String value) {
        if (objMap == null) {
            objMap = new HashMap<>();
        }
        objMap.put(key, value);
    }

    /**
     * 
     * @Title: notNullOrEmpty
     * @Description: 判断变量是否为空
     * @param o
     * @return
     */
    public static boolean notNullOrEmpty(Object o) {
        return !(o == null || "".equals(o.toString().trim()));
    }

    /**
     * 
     * @Title: createPageSql
     * @Description: 创建分页sql语句,仅适用于mysql数据库
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public String createPageSql(String pageIndex, String pageSize) {
        Integer index = notNullOrEmpty(pageIndex) ? Integer.valueOf(pageIndex) : 0;
        Integer size = Integer.valueOf(pageSize);
        StringBuffer sql = new StringBuffer(" limit ");
        sql.append(index * size);
        sql.append(",");
        sql.append(size);
        return sql.toString();
    }

    /**
     * 
     * @Title: createOrderSql
     * @Description: 创建sql排序语句
     * @param sortField
     * @param sortOrder
     * @return
     */
    public String createOrderSql(String sortField, String sortOrder) {
        StringBuffer sql = new StringBuffer(" order by ");
        // 存在排序字段
        if (notNullOrEmpty(sortField)) {
            sql.append(sortField);
            sql.append(" ");
            if (notNullOrEmpty(sortOrder)) {
                sql.append(sortOrder);
            }
            else {
                // 不存在排序方式默认升序排序
                sql.append(DEFAULT_SORT_ORDER);
            }
        }
        else {
            // 默认排序
            sql.append(DEFAULT_SORT_FIELD);
            sql.append(" ");
            sql.append(DEFAULT_SORT_ORDER);
        }
        return sql.toString();
    }

    /**
     * 
     * @Title: toJsonString
     * @Description: 将对象变成json字符串，带着时间格式
     * @param o
     * @return
     */
    public static String parseJson(Object o) {
        String result = JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }

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
     * 
     * @Title: writeJson
     * @Description: 将json输出到前端
     * @param pw
     * @param json
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

    public Map<String, String> getObjMap() {
        return objMap;
    }

    public void setObjMap(Map<String, String> objMap) {
        this.objMap = objMap;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "MiniHelper [objMap=" + objMap + ", sortField=" + sortField + ", sortOrder=" + sortOrder + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + "]";
    }

}
