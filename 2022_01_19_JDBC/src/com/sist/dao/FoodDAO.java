package com.sist.dao;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FoodDAO {
    // 연결 객체 선언(SQL)
    private Connection conn;
    private PreparedStatement ps;
    /*
    *       Statement   : 값 + SQL동시에 전송 => 단순한 문장 , 전송값이 없는 SQL
    *       PreparedStatement : 미리 SQL문장을 전송하고 나중에 값을 채운 후에 실행
    *                           => Default
    *       CallableStatement : 프로시저 호출
    * */
    // 오라클 연결 주소
    private final String URL="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    // 1. 드라이버 등록 (한번만 등록) => 생성자
    public FoodDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 2. 오라클 연결
    public void getConnection(){
        try{
            conn = DriverManager.getConnection(URL,"admin","Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 3. 오라클 해제
    public void disConnection() {
        try{
            if(ps != null) ps.close();
            if(conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 4. 맛집과 관련된 기능 => DAO는 재사용 (기능별로 따로 만든다)
    /*
    *       게시판 : BoardVO, BoardDAO
    *       회원가입 : MemberVO , MemberDAO
    *       영화 : MovieVO, MovieDAO
    *       추천 : ReserveVO, ReserveDAO
    *       기본 => 테이블 (14~20)
    *
    * */

    // 4-1 카테고리 읽기 => SELECT
    public List<CategoryVO> categoryListData(){
        List<CategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql="SELECT cno,title,link " +
                    "FROM category";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                CategoryVO vo = new CategoryVO();
                vo.setCno(rs.getInt(1));
                vo.setTitle(rs.getString(2));
                vo.setLink(rs.getString(3));

                list.add(vo);
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }

    public void foodInsert(FoodHouseVO vo)
    {
        try
        {
            //1. 연결
            getConnection();
            //2. SQL문장 제작
            String sql="INSERT INTO food_house VALUES("
                    +"(SELECT NVL(MAX(no)+1,1) FROM food_House),"
                    +"?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		   /*
		    *   String sql="INSERT INTO foodHouse VALUES("
				     +"(SELECT NVL(MAX(no)+1,1) FROM foodHouse),"
				     +vo.getCno()+",'"+vo.getPoster()+"','"+?,?,?,?,?,?,?,?,?,?,?,?)";
		    */
            //3. SQL문장 오라클로 전송
            ps=conn.prepareStatement(sql);
            //4. ?에 값을 채운다
            ps.setInt(1, vo.getCno());
            ps.setString(2, vo.getPoster()); // => 'aaa'
            ps.setString(3, vo.getName());
            ps.setDouble(4, vo.getScore());
            ps.setString(5, vo.getAddress());
            ps.setString(6, vo.getTel());
            ps.setString(7, vo.getType());
            ps.setString(8, vo.getPrice());
            ps.setString(9, vo.getTime());
            ps.setString(10, vo.getMenu());
            ps.setInt(11, vo.getGood());
            ps.setInt(12, vo.getSoso());
            ps.setInt(13, vo.getBad());
            ps.setString(14, vo.getParking());

            // 실행 명령
            ps.executeUpdate(); // commit() => 자동으로 저장
        }catch(Exception ex)
        {
            // 오류 확인
            ex.printStackTrace();
        }
        finally
        {
            //  닫기
            disConnection();
        }
    }
}
