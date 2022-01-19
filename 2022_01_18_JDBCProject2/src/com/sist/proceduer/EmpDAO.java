package com.sist.proceduer;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

import java.sql.*;
public class EmpDAO {
    private Connection conn;
    private CallableStatement cs;
    private final String URL = "jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public EmpDAO() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getConnection() {
        try {
            conn = DriverManager.getConnection(URL, "admin", "Gmlwnsskgus!@1208");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disConnection() {
        try {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void empAllData(){
        try {
            getConnection();
            String sql ="{CALL empAllData(?)}";
            cs=conn.prepareCall(sql);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.executeUpdate();
            ResultSet rs = (ResultSet)cs.getObject(1);
            while(rs.next()){
                System.out.println(
                                rs.getInt(1)+" " +
                                rs.getString(2)+" " +
                                rs.getString(3)+" " +
                                rs.getDate(4).toString()+" " +
                                rs.getInt(5)+" " +
                                rs.getString(6)+" " +
                                rs.getString(7)+" " +
                                rs.getInt(8)
                );
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
}
