package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlFileUtil {
    private static final String NOT_SQL = "请输入正确的sql文件路径";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "scott";
    private static final String PWD = "tiger";
    private static final Integer NUM = 50;//批处理执行条数

    private static void isSqlFile(String path) throws Exception {//判断文件
        File file = new File(path);
        if (!file.isFile() || !file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("sql"))
            throw new RuntimeException(NOT_SQL);
    }


    public static List<String> getSqlArray(String path) throws Exception {//将sql文件转换为数组
        isSqlFile(path);//判断是否为sql文件

        List<String> sqlList = new ArrayList<>();
        String line;
        StringBuffer str = new StringBuffer();
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        while ((line = br.readLine()) != null) {//文件行行读取
            str.append(line);
            if (str.indexOf(";") != -1) {//判断sql语句是否结束
                sqlList.add(str.substring(0, str.indexOf(";")));//将完整语句放入list中
                str.delete(0, str.indexOf(";") + 1);//删除完整sql语句
            }
        }
        br.close();
        return sqlList;
    }

    public static Connection getConn() {//获取连接
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void executeSql(Connection conn, List<String> sqlArray) {//执行数组sql
        Statement statement = null;
        try {
            statement = conn.createStatement();
            int num = 0;
            for (int i = 0; i < sqlArray.size(); i++) {
                statement.addBatch(sqlArray.get(i));
                num++;
                if (num == NUM) {
                    statement.executeBatch();
                    statement.clearBatch();
                    num=0;
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void executeSql(Connection conn, String sql) {//执行单条sql
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
