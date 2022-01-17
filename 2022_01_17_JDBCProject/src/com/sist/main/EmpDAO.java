package com.sist.main;
// 오라클 연결 => 송수신 ==> VO,DAO ,Manager (변경:일반 main, JSP, Spring)
import java.util.*;
import java.sql.*;
import java.util.concurrent.ExecutionException;

// ResultSet (결과값을 저장해주는 공간)
/*
 *   DDL / DML을 사용할 수 있다
 *   DDL
 *    CREATE TABLE , DROP , RENAME , ALTER .... (오라클내에서 보통 처리)
 *    DML (SELECT , INSERT , UPDATE , DELETE)
 *    ---------- 웹,데이터베이스 프로그래머 => 사용자의 SQL을 대신 만들어서 처리
 *    ---------- 사용자에 요청 (데이터베이스에 필요한 => 데이터만 입력)
 */
public class EmpDAO {
    // 오라클 , MS-SQL , MySql .... (드라이버명을  변경하면)
    // 1. 드라이버명 , 2. URL , 3. username , 4. password
    // 여기서 사용하는 SQL문장을 표준화가 되어 있다
    // SQL문장 전송 ==> BufferedReader / OutputStream
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
            String sql="SELECT empno,ename,job,hiredate,sal,dname,loc,grade " +
                    "FROM emp,dept,salgrade " +
                    "WHERE emp.deptno = dept.deptno " +
                    "AND sal BETWEEN losal AND hisal";
            /*String sql="SELECT empno,ename,job,hiredate,sal,"
                    +"dname,loc,"
                    +"grade "
                    +"FROM emp JOIN dept "
                    +"ON emp.deptno=dept.deptno "
                    +"JOIN salgrade "
                    +"ON sal BETWEEN losal AND hisal";*/
            //3. SQL문장 => 오라클로 전송
            ps=conn.prepareStatement(sql);
            //4. 실행후에 메모리에 저장 요청
            ResultSet rs=ps.executeQuery();
            //5. 메모리에 있는 데이터를 List로 이동
            while(rs.next())
            {
                EmpVO vo=new EmpVO();
                vo.setEmpno(rs.getInt(1));
                vo.setEname(rs.getString(2));
                vo.setJob(rs.getString(3));
                vo.setHiredate(rs.getDate(4));
                vo.setSal(rs.getInt(5));
                ///////////////////////////////////////
                vo.getDvo().setDname(rs.getString(6));
                vo.getDvo().setLoc(rs.getString(7));
                /////////////////// DeptVO
                vo.getSvo().setGrade(rs.getInt(8));
                /////////////////// SalGradeVO

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
    // 사용자 요청값이 있는 경우 => ?
    // 7788의 사번을 가지고 있는 사원의 사번,이름,직위,입사일,근무지,부서명,급여등급
    // 사원 1명의 데이터를 출력 => EmpVO
    /*
     *   화면 출력
     *   -------
     *   1. 목록 ==> List
     *   2. 목록에서 한개를 클릭 => 상세보기 ==> ~VO
     *   3. 일반 변수 => boolean(로그인처리, 아이디 중복) , int (총페이지)
     *   4. insert , update ,delete => 오라클 자체 처리 => void
     *   5. 제목만 , 장르만 , 가수만 , 출연만 .... List<String>
     *
     *   ------------------------------------------------------
     *   MovieVO => 영화 1개에 대한 정보를 가지고 있다
     *           => List<MovieVO> : 영화 여러개를 묶는다
     *           => 극장명 (날짜 => 시간 => 좌석)
     *   EmpVO   => 사원 1명에 대한 정보
     *           => 부서 , 등급
     *   MusicVO => 음악 1개에 대한 정보
     *   FoodVO  => 맛집 1개에 대한 정보
     *   RecipeVO => 레시피 1개에 대한 정보
     *   ------------ 해당 테이블의 컬럼명과 일치 + 추가가 가능 ---------
     *
     *   자바 <=======> 오라클
     *      1. 오라클에 있는 데이터를 자바에서 받는다
     *         오라클 (컬럼) = 자바(멤버변수)
     *      2. 여러개를 가지고 올때는 List에 묶어서 처리
     */
    public EmpVO empDetailData(int empno)
    {
        EmpVO vo=new EmpVO();
        try
        {
            // 1. 오라클 연결
            getConnection();
            // 2. SQL문장 제작 (사번,이름,직위,입사일,근무지,부서명,급여등급)
            String sql="SELECT empno,ename,job,hiredate,"
                    +"loc,dname,"
                    +"grade,sal "
                    +"FROM emp,dept,salgrade "
                    +"WHERE emp.deptno=dept.deptno "
                    +"AND sal BETWEEN losal AND hisal "
                    +"AND empno=?";
            // 3. SQL문장을 오라클로 전송
            ps=conn.prepareStatement(sql);

            // 4. ?에 값을 채워준다
            ps.setInt(1, empno); // ?는 번호가 1번부터 '7788'
            // 1번째에 있는 ?에값을 채운다
            // *** ps.setString(2,"홍길동") ==> '홍길동'로 변환한다
            // WHERE ename=홍길동 (X)
            // WHERE ename='홍길동' (O) => 날짜,문자열은 반드시 ''
            // 5. ?에 값을 채웠으면 => 실행요청 => 메모리에 저장
            ResultSet rs=ps.executeQuery();
            rs.next();// 데이터 출력 위치에 커서 이동
            // 6. EmpVO에 값을 채운다
            vo.setEmpno(rs.getInt(1));
            vo.setEname(rs.getString(2));
            vo.setJob(rs.getString(3));
            vo.setHiredate(rs.getDate(4));
            ////////////////// DeptVO => getDvo()
            vo.getDvo().setLoc(rs.getString(5));
            vo.getDvo().setDname(rs.getString(6));
            ////////////////// SalGradeVO => getSvo()
            vo.getSvo().setGrade(rs.getInt(7));
            vo.setSal(rs.getInt(8));

            rs.close();

        }catch(Exception ex)
        {
            // 오류 발생
            ex.printStackTrace();
        }
        finally
        {
            // 닫기
            disConnection();
        }
        return vo;
    }
    // 7. SubQuery

    // 급여의 평균보다 많이받는 사원의 이름, 직위, 입사일, 급여, 부서명, 근무지

    public List<EmpVO> empSubQueryData(){
        List<EmpVO> list = new ArrayList<>();
        try{
            getConnection();
            /*String sql="SELECT ROUND(AVG(sal)) " +
                    "FROM emp ";
            ps = conn.prepareStatement(sql);
            ResultSet rs =ps.executeQuery();
            rs.next();
            int avg = rs.getInt(1);
            ps.close();
            rs.close();*/
            String sql = "SELECT ename,job,hiredate,sal,dname,loc " +
                    "FROM emp,dept " +
                    "WHERE emp.deptno=dept.deptno " +
                    "AND sal>(SELECT ROUND(AVG(sal)) FROM emp) " +
                    "Order by ename";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                EmpVO vo = new EmpVO();
                vo.setEname(rs.getString(1));
                vo.setJob(rs.getString(2));
                vo.setHiredate(rs.getDate(3));
                vo.setSal(rs.getInt(4));
                vo.getDvo().setDname(rs.getString(5));
                vo.getDvo().setLoc(rs.getString(6));

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

    public List<EmpVO> empSubQueryListData(){
        List<EmpVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql="SELECT ename,job,hiredate,sal," +
                    "(SELECT denam FROM dept WHERE emp.deptno=dept.deptno)," +
                    "(SELECT loc FROM dept WHERE emp.deptno=dept.deptno) " +
                    "FROM emp";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                EmpVO vo = new EmpVO();
                vo.setEname(rs.getString(1));
                vo.setJob(rs.getString(2));
                vo.setHiredate(rs.getDate(3));
                vo.setSal(rs.getInt(4));
                vo.getDvo().setDname(rs.getString(5));
                vo.getDvo().setLoc(rs.getString(6));

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
}










