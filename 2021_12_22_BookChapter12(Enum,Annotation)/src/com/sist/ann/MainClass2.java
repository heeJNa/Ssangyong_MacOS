package com.sist.ann;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
// annotation => XML을 대처한다.
// Spring 4 => XML
// Spring 5 => Annotation
class UserRequest {
    @RequestMapping("login")
    public void login() {
        System.out.println("Login 요청...");
    }
    @RequestMapping("join")
    public void join() {
        System.out.println("회원가입 요청...");
    }
    @RequestMapping("idCheck")
    public void idCheck() {
        System.out.println("ID 중복체크 요청...");
    }
    @RequestMapping("postfind")
    public void postfind() {
        System.out.println("우편번호 검색 요청...");
    }
    @RequestMapping("list")
    public void board_list() {
        System.out.println("게시판 목록 출력 요청...");
    }
    @RequestMapping("detail")
    public void board_detail() {
        System.out.println("게시판 상세 보기 요청...");
    }
}

public class MainClass2 {
    public static void main(String[] args) {
//        UserRequest u = new UserRequest()
        //새로운 메모리 할당
        Scanner scan = new Scanner(System.in);
        System.out.println("요청 : ");
        String cmd = scan.next();
        try {
            Class clsName = Class.forName("com.sist.ann.UserRequest");  //클래스 정보를 가지고 온다
            //패키지명부터 => 클래스명까지 => 클래스정보를 읽어서 제어 (리플렉션)
            //메모리 할당
            Object obj = clsName.getDeclaredConstructor().newInstance();
            UserRequest u = (UserRequest) obj;
          /*  u.login();
            u.join();
            u.board_detail();
            u.board_list();
            u.idCheck();
            u.postfind();*/
            Method[] methods = clsName.getDeclaredMethods();
            for (Method m : methods) {
//                System.out.println(m.getName());
                RequestMapping rm = m.getAnnotation(RequestMapping.class);
                if (rm.value().equals(cmd)) {
                    m.invoke(obj, null); //메소드 호출
                    // u.join()
                    // obj => UserRequest의 객체
                    // null => 매개변수가 없다
                    // 메소드명 => 몰라도 호출이 가능
                }
            }
            //웹 => 솔루션
            // 어노테이션은 이미 프레임워크 => 만들어져있다
            // 어노테이션 => 뭐하는 역할 인지 확인 (찾기할때 주로 사용한다)
        } catch (Exception e) {
            e.printStackTrace();


        }
    }
}
