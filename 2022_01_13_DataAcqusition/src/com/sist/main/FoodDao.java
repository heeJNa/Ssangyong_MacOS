package com.sist.main;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDao {
    // 연결 객체 생성
    private Connection conn;
    // sql전송 객체
    private PreparedStatement ps;
    // url주소
    private final String url="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

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
            conn = DriverManager.getConnection(url,"admin","Gmlwnsskgus!@1208");
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
    public void foodInsert(FoodHouse food){
        try{
            getConnection();
            String sql ="INSERT INTO food_house VALUES ("
                        +"fh_no_seq.nextval,?,?,?,?,?,?,?,?,?,?,?)";
            ps =conn.prepareStatement(sql);
            ps.setInt(1,food.getCno());
            ps.setString(2,food.getName());
            ps.setDouble(3,food.getScore());
            ps.setString(4,food.getAddress());
            ps.setString(5,food.getTel());
            ps.setString(6,food.getType());
            ps.setString(7,food.getPrice());
            ps.setString(8,food.getParking());
            ps.setString(9,food.getTime());
            ps.setString(10,food.getMenu());
            ps.setString(11,food.getPoster());

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
    // 데이터 읽기 => Category의 link,cno,title
    public List<Category> categoryListData(){
        List<Category> list = new ArrayList<>();
        try{
            // 1. 연결
            getConnection();
            // 2. SQL문장 제작
            String sql = "SELECT cno,title,link FROM category";
            // 3. 필요한 데이터를 전송(x)
            // 4. 오라클에 실행 명령을 전송
            ps = conn.prepareStatement(sql); //SQL문장을 오라클로 보낸다
            // 5. 결과값을 받는다
            ResultSet rs = ps.executeQuery(); //실행결과를 메모리에 저장
            // 6. 결과값을 list에 첨부
            while(rs.next()){ // next() : 위에서부터 아래로 데이터를 한줄씩 읽어온다.
                Category c = new Category();
                c.setCno(rs.getInt(1));
                c.setTitle(rs.getString(2));
                c.setLink(rs.getString(3));
                list.add(c);
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
