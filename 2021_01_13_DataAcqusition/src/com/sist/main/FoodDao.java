package com.sist.main;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;

public class FoodDao {
    // 연결 객체 생성
    private Connection conn;
    // sql전송 객체
    private PreparedStatement ps;
    // url주소
    private final String URL="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    // 1. 드라이버 등록
    public FoodDao(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 2. 오라클 연결
    public void getConnection(){
        try {
            conn = DriverManager.getConnection("url","admin","Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 3. 오라클 해제
    public void disConnection(){
        try {
            if(ps != null) ps.close();
            if(conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 4. 기능 => Category에 데이터 추가
    public void categoryInsert(Category c){
//        Category category = (Category) obj;
        try{
            // 1. 오라클 연결
            getConnection();
            // 2.SQL문장 작성
            String sql="INSERT INTO category VALUES (cate_cno_seq.nextval,?,?,?,?)";
            // 3. 오라클로 전송
            ps = conn.prepareStatement(sql);
            ps.setString(1,c.getTitle());
            ps.setString(2,c.getSubject());
            ps.setString(3,c.getPoster());
            ps.setString(4,c.getLink());
            // 4. 실행 명령
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
}
