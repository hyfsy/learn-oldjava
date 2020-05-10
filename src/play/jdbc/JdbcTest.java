package play.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.JdbcUtil;
import util.JdbcUtil.JDBC;

public class JdbcTest
{

    public static int testInsert(JdbcEntity je) {
        Connection conn = JdbcUtil.getConnection(JDBC.MYSQL);
        String sql = "insert into jdbcEntity (sno,sname) values(?,?)";
        PreparedStatement psmt = null;
        int count = 0;
        try {
            psmt = conn.prepareStatement(sql);

            psmt.setInt(1, je.getSno());
            psmt.setString(2, je.getSname());

            count = psmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(conn, psmt);
        }
        return count;
    }

    public static int testInsert2() {
        Connection conn = JdbcUtil.getConnection(JDBC.MYSQL);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "insert into jdbcEntity (sno,sname) values(%d,'张三%c')";
        for (int i = 0; i < 10; i++) {
            String format = String.format(sql, i, i);
            try {
                System.out.println(format);
                stmt.execute(format);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int testDelete(JdbcEntity je) {
        Connection conn = JdbcUtil.getConnection(JDBC.MYSQL);
        String sql = "delete from jdbcEntity where sno=?";
        PreparedStatement psmt = null;
        int count = 0;
        try {
            psmt = conn.prepareStatement(sql);

            psmt.setInt(1, je.getSno());

            count = psmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(conn, psmt);
        }
        return count;
    }

    public static int testUpdate(JdbcEntity je) {
        Connection conn = JdbcUtil.getConnection(JDBC.MYSQL);
        String sql = "update jdbcEntity set sname=? where sno=?";
        PreparedStatement psmt = null;
        int count = 0;
        try {
            psmt = conn.prepareStatement(sql);

            psmt.setString(1, je.getSname());
            psmt.setInt(2, je.getSno());

            count = psmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(conn, psmt);
        }
        return count;
    }

    public static List<JdbcEntity> testSelect() {
        Connection conn = JdbcUtil.getConnection(JDBC.MYSQL);
        String sql = "select * from jdbcEntity";
        PreparedStatement psmt = null;
        ResultSet rs = null;
        List<JdbcEntity> jeList = new ArrayList<>();
        try {
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                JdbcEntity je = new JdbcEntity();
                je.setSno(rs.getInt(1));
                je.setSname(rs.getString("sname"));
                jeList.add(je);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcUtil.close(conn, psmt, rs);
        }
        return jeList;
    }

}
