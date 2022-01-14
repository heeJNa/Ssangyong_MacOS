package com.sist.main2;

import java.sql.*;
import java.util.*;

// 사용자 요청 => 오라클처리
// 사용자 대신 오라클연동을 위해서 SQL문장을 만들어서 처리
// DAO => 오라클은 연결해 주는 클래스
public class ZipcodeDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String url = "jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public ZipcodeDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnection(){
        try{
            conn =DriverManager.getConnection(url,"admin","Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void disConnection(){
        try{
            if(ps != null)ps.close();
            if(conn != null)conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int zipcodeFindCount(String dong){
        // count(*)
        int count = 0;
        try {
            getConnection();
            String sql = "SELECT COUNT(*) " +
                    "FROM zipcode " +
                    "WHERE dong LIKE '%'||?||'%'";
            ps = conn.prepareStatement(sql);
            ps.setString(1,dong);
            ResultSet rs =ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return count;
    }

    public List<Zipcode> zipcodeListData(String dong){
        List<Zipcode> list = new ArrayList<>();
        try {
            getConnection();
            String sql ="SELECT zipcode,sido,gugun,dong,NVL(bunji,' ') " +
                    "FROM zipcode " +
                    "WHERE dong LIKE '%'||?||'%'";
            // 테이블명과 컬럼명이 동일해도 무관하다.
            // 문제가 발생
            ps = conn.prepareStatement(sql);
            ps.setString(1,dong);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Zipcode zipcode = new Zipcode();
                zipcode.setZipcode(rs.getString(1));
                zipcode.setSido(rs.getString(2));
                zipcode.setGugun(rs.getString(3));
                zipcode.setDong(rs.getString(4));
                zipcode.setBunji(rs.getString(5));

                list.add(zipcode);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }


}
