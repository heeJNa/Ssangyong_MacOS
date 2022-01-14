package com.sist.main;

import java.util.List;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        // 오라클 연동하는 클래스를 메모리에 저장
        EmpDao dao = new EmpDao();
        // 요청후 데이터를 얻어 온다 => empListData()
        /*List<Emp> list = dao.empListData();
        // 사용자 => 데이터 출력
        for (Emp e:list){
            System.out.println(e.getEmpno() +" "
                    +e.getEname()+" "
                    +e.getJob()+" "
                    +e.getHiredate().toString()+" "
                    +e.getSal());

        }*/
        /*Scanner scan = new Scanner(System.in);
        System.out.print("사번입력 : ");
        int empno = scan.nextInt();

        Emp emp = dao.empDetailData(empno);

        System.out.println("---------------------------- 실행 결과 ----------------------------");
        System.out.println("사번 : "+emp.getEmpno());
        System.out.println("이름 : "+emp.getEname());
        System.out.println("직위 : "+emp.getJob());
        System.out.println("입사일 : "+emp.getHiredate().toString());
        System.out.println("금여 : "+emp.getSal());
        System.out.println("성과급 : "+(emp.getComm()==0?"없음":emp.getComm()));
        System.out.println("-----------------------------------------------------------------");*/

        Scanner scan = new Scanner(System.in);
        System.out.print("검색할 사원의 이름 입력 : ");
        String ename = scan.next();
        List<Emp> list = dao.empFindData(ename);
        for (Emp emp:list) {
            System.out.println("사번 : "+emp.getEmpno());
            System.out.println("이름 : "+emp.getEname());
            System.out.println("직위 : "+emp.getJob());
            System.out.println("입사일 : "+emp.getHiredate().toString());
            System.out.println("금여 : "+emp.getSal());
            System.out.println("성과급 : "+(emp.getComm()==0?"없음":emp.getComm()));
            System.out.println("----------------------------------------------");
        }
    }
}
