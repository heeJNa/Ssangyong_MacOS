package com.sist.main;
// 오라클 연동
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpDao {
    private Connection conn;
    private PreparedStatement ps;
    private final String url="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public EmpDao(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getConnection(){
        try{
            conn=DriverManager.getConnection(url,"admin","Gmlwnsskgus!@1208");
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

    // 4. 기능
    // 4-1 사원 전체 출력
    public List<Emp> empListData(){
        List<Emp> list = new ArrayList<>();
        try{
            // 1. 연결 (오라클) => 데이터 가지고 올 준비
            getConnection();
            // 2. SQL문장을 만든다
            String sql ="SELECT empno,ename,job,hiredate,sal " +
                    "FROM emp";
            // 오라클 문장이 끝나면 ;을 사용한다 => 자바에서는 ;을 사용하지 않는다.
            // 3. SQL문장을 오라클 보낸다
            ps = conn.prepareStatement(sql);
            // 4. 필요한 데이터가 없는 경우 (?) => 실행요청을 한다
            // 실행한 결과를 받아 본다
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                // 처음위치에 커서 위치 => 한줄씩 읽어서 다음줄로 내려간다
                // => 데이터가 없는 경우에는 false, 있는 경우 => true
                Emp e = new Emp();
                e.setEmpno(rs.getInt(1));
                e.setEname(rs.getString(2));
                e.setJob(rs.getString(3));
                e.setHiredate(rs.getDate(4));
                e.setSal(rs.getInt(5));

                list.add(e);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }
    // 4-2 사원 정보 보기 (한명에 대한)
    public Emp empDetailData(int empno){
        Emp emp = new Emp();
        try {
            getConnection();
            String sql ="SELECT empno,ename,job,hiredate,sal,comm " +
                    "FROM emp " +
                    "WHERE empno=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,empno); // 데이터형을 설정
            ResultSet rs=ps.executeQuery();
            rs.next();
            emp.setEmpno(rs.getInt(1));
            emp.setEname(rs.getString(2));
            emp.setJob(rs.getString(3));
            emp.setHiredate(rs.getDate(4));
            emp.setSal(rs.getInt(5));
            emp.setComm(rs.getInt(6));
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return emp;
    }
    // 4-3 사원 찾기
    public List<Emp> empFindData(String ename){
        List<Emp> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT empno,ename,job,hiredate,sal,comm " +
                    "FROM emp " +
                    "WHERE ename LIKE '%'||?||'%'";
            // 자바에서 LIKE문장을 작성할때 => '%'||?||'%'
            ps = conn.prepareStatement(sql);
            ps.setString(1,ename.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Emp e = new Emp();
                e.setEmpno(rs.getInt(1));
                e.setEname(rs.getString(2));
                e.setJob(rs.getString(3));
                e.setHiredate(rs.getDate(4));
                e.setSal(rs.getInt(5));
                e.setComm(rs.getInt(6));

                list.add(e);
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
