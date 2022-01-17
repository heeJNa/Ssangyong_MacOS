package com.sist.main;
import java.util.*;
import java.sql.*;
public class EmpDAO {
    private Connection conn;
    private PreparedStatement ps;
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
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 1. 전체 사원 목록
    public List<EmpVO> empListData() {
        List<EmpVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "SELECT * FROM emp";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EmpVO vo = new EmpVO();
                vo.setEmpno(rs.getInt(1));
                vo.setEname(rs.getString(2));
                vo.setJob(rs.getString(3));
                vo.setMgr(rs.getInt(4));
                vo.setHiredate(rs.getDate(5));
                vo.setSal(rs.getInt(6));
                vo.setComm(rs.getInt(7));
                vo.setDeptno(rs.getInt(8));

                list.add(vo);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disConnection();
        }
        return list;
    }
    // 2. 부서 목록 출력
    public List<DeptVO> deptListData(){
        List<DeptVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "SELECT * FROM dept";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                DeptVO vo = new DeptVO();
                vo.setDeptno(rs.getInt(1));
                vo.setDname(rs.getString(2));
                vo.setLoc(rs.getString(3));

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
    // 3. 등급 목록 출력
    public List<SalGradeVO> salGradeListData()  {
        List<SalGradeVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql="SELECT * FROM salgrade";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                SalGradeVO vo = new SalGradeVO();
                vo.setGrade(rs.getInt(1)); // 컬렴명 / 인덱스
                vo.setHisal(rs.getInt(2));
                vo.setLosal(rs.getInt(3));
                // 함수나 별칭을 사용시에는 ==> 인덱스를 이용하는 경우가 더 많다

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
    // 4. 사원, 부서
    public List<EmpVO> empDeptJoinData(){
        List<EmpVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql ="SELECT empno,ename,job,hiredate,sal," +
                    "dname,loc " +
                    "FROM emp,dept " +
                    "WHERE emp.deptno=dept.deptno";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                EmpVO vo = new EmpVO();
                vo.setEmpno(rs.getInt(1));
                vo.setEname(rs.getString(2));
                vo.setJob(rs.getString(3));
                vo.setHiredate(rs.getDate(4));
                vo.setSal(rs.getInt(5));
                vo.getDvo().setDname(rs.getString(6));
                vo.getDvo().setLoc(rs.getString(7));

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
    // 5. 사원, 긍금 => JOIN(NON-EQUI-JOIN) = 을 사용하지 않는다
    // BETWEEN ~ AND
    // INNER JOIN
    // => ANSI JOIN
    public List<EmpVO> empSalgradeJoinData(){
        List<EmpVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql="SELECT empno,ename,job,hiredate,sal,grade " +
                    "FROM emp JOIN salgrade " +
                    "ON sal BETWEEN losal AND hisal";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                EmpVO vo = new EmpVO();
                vo.setEmpno(rs.getInt(1));
                vo.setEname(rs.getString(2));
                vo.setJob(rs.getString(3));
                vo.setHiredate(rs.getDate(4));
                vo.setSal(rs.getInt(5));
                vo.getSvo().setGrade(rs.getInt(6));

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
    // 6. 사원,부서, 등급 => Join
    public List<EmpVO> empDeptSalgradeJoinData(){
        List<EmpVO> list = new ArrayList<>();
        try {
            getConnection();
            String sql="SELECT ";
            ps = conn.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }
}
