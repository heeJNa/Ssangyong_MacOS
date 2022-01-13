package com.sist.dao;

import com.sist.common.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class BooksDAO implements DataBase {
    // 필요한 클래스 가지고 온다 => 포함 클래스
    private Connection conn; // 오라클 연결
    private PreparedStatement ps; // SQL문장을 전송
    private final String url = "jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622"; //오라클 주소
    /*
    *       드라이버 => thin => 연결만 하는 역할 (무료)
    *                 oci  => 드라이버 안에 오라클 정보를 다 가지고 있다 (유료)
    *
    *       자바, thin, MySQL(MariaDB)
    *                  ============= Front(Spring-Boot => RestAPI)
    *                                VueJS/ReactJS
    *       aws => JSP (1차), Spring (2차)
    *
    *       데이터베이스 / 테이블
    *       --------   -----
    *         폴더       파일일
    *
    *       ==> 데이터를 모아서 저장 (데이터형 클래스 ==> 캠슐화)
    *       ==> 데이터베이스 연결 처리
    *       ==> 외부에서 데이터 읽어오기
   * */
    // 드라이버 연결 ==> 한번만 설정 => 생성자
    public BooksDAO(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // => 클래스 명으로 메모리 할당을 한다.
        }catch (Exception e){
            e.printStackTrace();
            // getMessage() : 에러 메세지만 출력,
            // printStackTrace => 실행하는 과정을 출력 => 에러 위치 확인
            // 에러 => 소스비교 (x) , Output창을 확인한다
        }
    }
    // 반복이 많다 => 메소드화
    /*
    *       한개의 기능을 수행 => 재사용이 가능
    *       반복을 제거
    *       구조화된 프로그램 (단락, 가독성)
    *
    * */
    // 오라클 연결
    public void getConnection() {
        // 오라클 연결, 파일 연결, 네트워크서버 연결 => CheckException
        try {
            conn= DriverManager.getConnection(url,"admin","Gmlwnsskgus!@1208");
        }catch (Exception e){
            e.printStackTrace(); // username , password
        }
    }
    // 오라클 연결 해제
    public void disConnection(){
        try {
            // 입출력 => PreparedStatement => OutputStream, BufferedReader
            // 소켓 => Connection => Socket
            // 기억 => 전송할때 ==> 오라클 문법을 사용
            if (ps != null) ps.close(); //통신
            if (conn != null) conn.close(); //전화기 닫기
            // ps != null ==> ps가 사용중이면
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /////////////////////////
    /*
    *       메소드
    *       ====
    *       형식)
    *           =====================
    *               선언부
    *           =====================
    *               구현부
    *           =====================
    *
    *           1. 선언부 => 리턴형 / 매개변수
    *           2. 구현부 => {}
    * */
    @Override
    public void dbInsert(Object obj){
        try {
            // => 형변환
            Books b = (Books) obj;
            // 1. 오라클 연결
            getConnection();
            // 2. 오라클에 명령어 전송
            String sql ="INSERT INTO books VALUES (?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql); // 전송객체를 생성
            // 3. ?에 값을 채운다. ==> ?의 순서가 1번부터 시작
            ps.setInt(1,b.getNo());
            ps.setString(2,b.getTitle());
            ps.setString(3,b.getPoster());
            ps.setString(4,b.getContent());
            ps.setString(5,b.getAuthor());
            ps.setString(6,b.getPrice());
            ps.setString(7,b.getRegdate());
            ps.setString(8,b.getIsbn());
            ps.setString(9,b.getTags());
            // 4. 오라클 => 실행 명령을 전송한다.
            ps.executeUpdate(); // commit이 포함 => autocommit => 자동 저장


        }catch (Exception e){
            e.printStackTrace();
        } finally {
            // 닫기
            disConnection();
        }
    }
}
