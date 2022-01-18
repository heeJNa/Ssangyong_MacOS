package com.sist.main;
import java.util.*;
import java.sql.*;
import java.util.concurrent.ExecutionException;

public class StudentDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL = "jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public StudentDAO(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getConnection(){
        try{
            conn = DriverManager.getConnection(URL, "admin", "Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void disConnection(){
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 기능 1 => 데이터 추가 INSERT
    public void studentInsert(StudentVO vo){
        // 한명의 학생 정보를 추가
        try{
            getConnection();
            String sql = "INSERT INTO student VALUES(" +
                    "(SELECT NVL(MAX(hakbun),1)+1 FROM  student)," +
                    "?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,vo.getName());
            ps.setInt(2,vo.getKor());
            ps.setInt(3,vo.getEng());
            ps.setInt(4,vo.getMath());

            ps.executeUpdate(); // commit 포함

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
    // 기능 2 => 전체 출력  SELECT
    public List<StudentVO> studentListData(){
        List<StudentVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "SELECT * FROM student";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                StudentVO vo = new StudentVO();
                vo.setHakbun(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setKor(rs.getInt(3));
                vo.setEng(rs.getInt(4));
                vo.setMath(rs.getInt(5));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }
    // 기능 3 수정 (점수)   UPDATE
    public void studentUpdate(StudentVO vo){
        try {
            getConnection();
            String sql= "UPDATE student SET " +
                    "kor=?, eng=?, math=? " +
                    "WHERE hakbun=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getKor());
            ps.setInt(2,vo.getEng());
            ps.setInt(3,vo.getMath());
            ps.setInt(4,vo.getHakbun());

            ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
    // 기능 4 삭제         DELETE
    public void studentDelete(int hakbun){
        try{
            getConnection();
            String sql ="DELETE FROM student " +
                    "WHERE hakbun=?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1,hakbun);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }
}
