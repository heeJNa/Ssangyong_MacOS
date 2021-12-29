package com.sist.network.temp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 *    네트워크 (클라이언트/서버)
 *    = 클라이언트(Front-End) => 사용자 프로그램(USER)
 *      1) 요청 => 목록보여준다 , 검색 , 로그인 처리 , 회원가입 , 글쓰기...
 *      2) 서버로부터 응답을 받아서 화면에 출력 (화면 UI)
 *      3) 윈도우(Application)
 *         웹(브라우저 => 화면 UI => HTML,XML) => 화면 디자인(CSS)
 *         모바일 (XML로 화면 UI => 자바에서 코틀린)
 *    = 서버(Back-End)
 *      1) 사용자가 요청한 내용을 받아본다
 *      2) 요청에 대한 처리
 *          => 데이터 검색
 *          => 삭제
 *          => 수정
 *          => 추가
 *          => 오라클 => DML(데이터 조작언어)  ==> CURD
 *             SELECT(데이터 검색)
 *             INSERT(데이터 추가)
 *             UPDATE(데이터 수정)
 *             DELETE(데이터 삭제)
 *      3) 처리 결과를 응답 (클라이언트로 전송)
 *      4) 여러 사용자를 공통으로 관리 (데이터가 공유)
 *
 *    *** 네트워크를 이용하는 프로그램
 *        =====================================================
 *                                 클라이언트            서버
 *        =====================================================
 *        Application              윈도우/도스       자바 서버 제작
 *                                 온라인 게임
 *                                 => 보드 / 아카이드 / RPG
 *        =====================================================
 *        Web Application          브라우저         아파치 (톰캣) =>
 *                                                서버 이미 존재
 *                                 화면 UI         요청 예상
 *                                                Spring/JSP
 *                           ==> 여러명이 접근이 가능
 *                           ==> 같은 데이터를 사용   ==> 공유 (오라클)
 *        =====================================================
 *        Mobile Application       핸드폰           웹 (http)
 *        =====================================================
 *
 *     주요 클래스
 *      = URL (웹 서버에 접근시에 사용)
 *      = ServerSocket : 기지국
 *          => 접속을 받는다
 *          => 해당 Socket을 연결
 *      = Socket : 전화 => 클라이언트
 *        ======= 반드시 쓰레드에서 연결 (각자 통신을 따로 할 수 있다)
 *
 *      1) 동작하는 과정
 *         = 서버 실행(구동) ===> 보통 생성자 (한번만 호출이 가능)
 *           ss=new ServerSocket(PORT) => 50명까지 접근이 가능
 *              bind() => 핸드폰 개통 (유심)
 *              ======
 *                전화번호(ip) , 전화선(port)
 *                *** port => 0~65535 (0~1023이미 알려진 포트)
 *                 예)
 *                      80 => http
 *                      23 => telnet
 *                      21 => ftp
 *                      25 => smtp
 *                      1521 => 오라클
 *                      ============= 다시 설치 (1522,1523...)
 *                      4000 => 메드서버
 *                      8080 => 프록시
 *               listen() : 대기상태
 *          = 클라이언트가 접속시 처리
 *            => 쓰레드를 이용한다
 *               public void run()
 *               {
 *
 *               }
 *            => accept() => 특이 메소드 : 멈춤상태 => 클라이언트가 접속을 했을때만 호출
 *            => accept() => 접속시에 사용자의 정보를 리턴한다
 *                                  ========== 발신자 전화번호
 *                                             (ip,port) => Scoket
 *               Socket accept()
 *            => Socket을 받아서 저장을 한다 (다시 응답하기 위해서 ip기억한다)
 *            => 접속시마다 클라이언트가 각자 통신을 위해서 쓰레드 생성
 *          = 쓰레드(사용자와 통신만 할 수 있는 프로그램을 제작)
 *            요청을 받아서 처리 => 응답(결과값을 보내준다)
 *
 *          서버 프로그램은 두개의 클래스
 *          ======================
 *           1. 클래스 : 접속을 받아서 정보를 저장 => 대기 소켓
 *                     ====================
 *                          | 쓰레드에서 사용
 *           2. 통신만 담당(요청=>응답) => 통신 소켓
 *           ==========================================
 *            한개의 클래스로 통합 : 내부 클래스 (inner class)
 *
 *            class Server
 *            {
 *                정보 저장 (접속자의 ip / port)
 *                ========================== 데이터
 *                서버구동
 *                접속시 처리
 *
 *                class Client extends Thread
 *                {
 *                   통신을 담당하는 소스
 *                   ==> 정보 저장 (접속자의 ip / port)를 공유
 *                }
 *            }
 *
 *         => 서버 제작하는 방식
 *            1대다
 *            P2P => 파일 전송 / 화상 ... 게임
 *
 *            => TCP / UDP ==> Socket(TCP)
 *               ===
 *               스트림을 이용하는 프로그램 (메모리에 저장 => 저장된 값을 읽는다)
 *               => 데이터 소실이 거의 없다
 *               => 연결 지향적 => 연결이 될때까지 데이터를 전송(신뢰성)
 *               => email,ftp...
 *               UDP
 *               ===
 *                속도가 빠르다
 *                데이터 소실이 있다
 *                게임 , 멀티미디어
 *
 *
 *
 */
// URL 연결  ==> https://wikibook.co.kr/list/ => 데이터 읽기
/*
 *     URL
 *     https://wikibook.co.kr:80   /list/
 *     =====   ============== ==   ======
 *      protocol  서버주소 (ip=>도메인)  URI(사용자 요청)
 *      (약속)
 */
public class MainClass {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://wikibook.co.kr/list/");
            // 연결
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            // 데이터를 메모리에 옮긴다
            if (conn != null) //사이트에 연결이 되었다면
            {
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while (true) {
                    String line = in.readLine();
                    if (line == null) { // 읽을 데이터가 없을때
                        break;
                    }
                    System.out.println(line);
                }
                in.close();
                conn.disconnect(); //종료
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
