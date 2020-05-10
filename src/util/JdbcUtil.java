package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcUtil
{
    private static final String SQLSERVER_URL = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=myDB";
    private static final String SQLSERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    private static final String SQLSERVER_USERNAME = "";
    private static final String SQLSERVER_PASSWORD = "";
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String ORACLE_USERNAME = "scott";
    private static final String ORACLE_PASSWORD = "tiger";
    private static final String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/test?useSSL=true";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PASSWORD = "Gepoint";

    // 连接池对象
    private static DruidDataSource druidDataSource = null;

    /**
     * JDBC连接选择枚举
     */
    public enum JDBC
    {
        MYSQL, ORACLE, SQLSERVER, RESOURCE
    }

    /**
     * 获取连接池对象
     * 
     * @return
     */
    private static DruidDataSource getDataSource() {
        if (druidDataSource != null) {
            return druidDataSource;
        }
        ResourceBundle bundle = ResourceBundle.getBundle("db");
        String driverClass = bundle.getString("jdbc.driverClass");
        String url = bundle.getString("jdbc.url");
        String username = bundle.getString("jdbc.username");
        String password = bundle.getString("jdbc.password");
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(driverClass);
        dds.setUrl(url);
        dds.setUsername(username);
        dds.setPassword(password);
        druidDataSource = dds;
        return dds;
    }

    /**
     * 获取对应JDBC连接
     */
    public static Connection getConnection(JDBC jdbc) {
        Connection conn = null;
        try {
            switch (jdbc) {
                case RESOURCE:
                    conn = getDataSource().getConnection();
                    break;
                case MYSQL:
                    Class.forName(MYSQL_DRIVER);
                    conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
                    break;
                case ORACLE:
                    Class.forName(ORACLE_DRIVER);
                    conn = DriverManager.getConnection(ORACLE_URL, ORACLE_USERNAME, ORACLE_PASSWORD);
                    break;
                case SQLSERVER:
                    Class.forName(SQLSERVER_DRIVER);
                    conn = DriverManager.getConnection(SQLSERVER_URL, SQLSERVER_USERNAME, SQLSERVER_PASSWORD);
                    break;
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }

    /**
     * 关闭连接
     */
    public static void close(Connection conn, PreparedStatement psmt) {
        close(conn, psmt, null);
    }

    /**
     * 关闭连接
     */
    public static void close(Connection conn, PreparedStatement psmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (psmt != null) {
            try {
                psmt.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将resultSet对象转换成对应类型的集合
     * 
     * @param rs
     * @param clazz
     * @return
     */
    public static <T> List<T> getResultSetList(ResultSet rs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            // 获取数据库行信息
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            while (rs.next()) {
                // 生成实例对象
                T instance = clazz.getConstructor().newInstance();
                for (int i = 0; i < count; i++) {
                    // 获取变量名
                    String fieldName = metaData.getColumnLabel(i + 1);
                    // 获取对应数据库字段的变量
                    Field field = getFieldByColumnName(fieldName, clazz);
                    // 类中无该数据库中字段变量
                    if (field == null) {
                        continue;
                    }
                    String fName = field.getName();
                    // 获取set方法
                    Method method = clazz.getMethod("set" + fName.substring(0, 1).toUpperCase() + fName.substring(1), field.getType());
                    // 执行set方法
                    method.invoke(instance, rs.getObject(fieldName));
                }
                list.add(instance);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取数据库中对应的类成员变量(包括本类的所有父类变量/不包括object)
     * 
     * @param name
     * @param clazz
     * @return
     */
    private static <T> Field getFieldByColumnName(String name, Class<? super T> clazz) {

        // 判断是否有非object的父类，获取父类成员变量
        while (!"java.lang.object".equals(clazz.getName().toLowerCase())) {
            // 获取所有变量
            Field[] fields = clazz.getDeclaredFields();
            // 遍历判断是否有和数据库中字段匹配的变量
            for (Field field : fields) {
                if (field.getName().toLowerCase().equals(name.toLowerCase())) {
                    return field;
                }
            }
            // 循环获取父类
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 将对象中的所有变量都添加到stmt对象中
     * 
     * @param stmt
     * @param instance
     */
    public static <T> void setPreparedStatement(PreparedStatement stmt, T instance) {
        try {
            Class<? extends Object> clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            // 遍历类中每个变量
            for (int i = 0; i < fields.length; i++) {
                // 获取变量的类型
                Class<?> type = fields[i].getType();
                // 获取get方法
                String fName = fields[i].getName();
                Method method = clazz.getMethod("get" + fName.substring(0, 1).toUpperCase() + fName.substring(1));
                // 获取变量的值
                Object o = method.invoke(instance);
                // 判断date类型转换为时间戳格式存入数据库
                if (type == java.util.Date.class) {
                    Date d = (Date) o;
                    stmt.setTimestamp(i + 1, new java.sql.Timestamp(d.getTime()));
                    continue;
                }
                stmt.setObject(i + 1, o);
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
