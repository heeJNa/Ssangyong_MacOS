package com.sist.dao;
import com.sist.common.DataBase;

import java.sql.*;
import java.util.*;
/*
*       DB2 : 대용량 데이터베이스 : 금융권, 공기업, 학교
*       ===============
*       중형
*       Oracle : 대기업
*       Ms-SQL : 시청...(MS)
*       ===============
*       중소형 =======================> 실무에서는 가장 많이 사용 (무료)
*       MY-SQL : 오라클
*       MariaDB : MySQL에서 파생되어 새롭게 생성
*       ===============
*       소형
*       SQLITE : 핸드폰
*       ===============
* */
/*
*       클래스 : 대문자 시작
*       변수 : 소문자
*       상수 : 전체 대문자
*       메소드 : 소문자
*       괄호
*       public void display(){
*       }
* */
public class MusicDao implements DataBase {
    // 연결 객체 (오라클)
    private Connection conn;
    // 오라클로 명령어 전송
    private PreparedStatement ps;
    // 오라클 주소 => 대소문자 구분 x
    private final String URL="jdbc:oracle:thin:@oracle_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_oracle";
    // 1. 드라이버 등록 : 한번만 수행 => 생성자
    public MusicDao(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 2. 오라클 연결
    public void getConnection(){
        try {
            conn=DriverManager.getConnection(URL,"admin","Gmlwnsskgus!@1208");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 3. 오라클 해제
    public void disConnection(){
        try {
            if(ps != null)  ps.close();
            if(conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void dbInsert(Object obj){
        try {
            Music m = (Music)obj;
            // 1. 연결
            getConnection();
            // 2. 오라클 명령문 전송
            /*
            *       SQL
            *           => DDL : 데이터 선언어
            *               TABLE / VIEW / SEQUENCE / INDEX / PROCEDURE
            *               FUNCTION / TRIGGER
            *               create : 데이터베이스 걸계 (정규화)
            *               drop : 테이블 삭제
            *               alter : 테이블 수정
            *               rename
            *               truncate
            *           => DML : 데이터 조작어
            *               select : 데이터 검색
            *               insert : 데이터 삽입
            *               update : 데이터 수정
            *               delete : 데이터 삭제
            *           => DCL : 데이터 제어어
            *               grant
            *               revoke
            *           => TCL : 트랜잭션 제어어
            *               commit
            *               rollback
            *
            * */
            String sql ="INSERT INTO genie_music VALUES (?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 3. 값을 채워서 실행 명령
            ps.setInt(1,m.getNo());
            ps.setString(2,m.getTitle());
            ps.setString(2,m.getSinger());
            ps.setString(2,m.getAlbum());
            ps.setInt(2,m.getRank());
            ps.setString(2,m.getState());
            ps.setInt(2,m.getIdcrement());
            ps.setString(2,m.getKey());
            ps.setString(2,m.getPoster());

            ps.executeUpdate(); // 녹색화살표, 엔터

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 오라클 종료
            disConnection();
        }
    }
    // 4. 기능 ====> 오라클 데이터 추가

}
