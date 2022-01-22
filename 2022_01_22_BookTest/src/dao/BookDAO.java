package dao;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.*;
public class BookDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public BookDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnection(){
        try {
            conn = DriverManager.getConnection(URL,"admin","Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disConnection() {
        try{
            if(ps != null) ps.close();
            if(conn != null)conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void InsertMainCate(MainCategoryVO vo){
        try {
            getConnection();
            String sql="INSERT INTO main_category VALUES " +
                    "(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getId());
            ps.setString(2,vo.getName());

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
    public int MainCateId(){
        int num = 0;
        try{
            getConnection();
            String sql ="SELECT COUNT(id) FROM main_category";
            ps = conn.prepareStatement(sql);
            num = ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return num;
    }
}
