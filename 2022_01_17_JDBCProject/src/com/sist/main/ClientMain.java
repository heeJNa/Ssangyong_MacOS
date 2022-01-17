package com.sist.main;

import java.util.List;

public class ClientMain {
    public static void main(String[] args) {
        EmpDAO dao = new EmpDAO();
        // 1. 사원 정보 전체 출력
        System.out.println("========== 사원 목록 ==========");
        List<EmpVO> eList = dao.empListData();
        // 1-2. 출력
        for(EmpVO vo:eList){
            System.out.println(vo.getEmpno()+"\t" +
                    vo.getEname()+"\t"+
                    vo.getJob()+"\t"+
                    vo.getMgr()+"\t"+
                    vo.getHiredate().toString()+"\t"+
                    vo.getSal()+"\t"+
                    vo.getComm()+"\t"+
                    vo.getDeptno());
        }
        // 2. 부서 목록 출력
        System.out.println("\n========== 부서 목록 ==========");
        List<DeptVO> dList = dao.deptListData();
        for (DeptVO vo : dList){
            System.out.println(vo.getDeptno() + "\t" +
                    vo.getDname()+"\t" +
                    vo.getLoc());
        }
        System.out.println("\n========== 급여 정보 목록 ==========");
        List<SalGradeVO> sList = dao.salGradeListData();
        for (SalGradeVO vo : sList){
            System.out.println(vo.getGrade() +"\t" +
                    vo.getHisal()+"\t" +
                    vo.getLosal());
        }
        System.out.println("\n========== Emp와 Dept 조인 목록 ==========");
        List<EmpVO> list = dao.empDeptJoinData();
        for(EmpVO vo : list){
            System.out.println(vo.getEmpno()+ " " +
                    vo.getEname()+" " +
                    vo.getJob()+" " +
                    vo.getHiredate().toString()+" " +
                    vo.getSal()+" " +
                    vo.getDvo().getDname()+" " +
                    vo.getDvo().getLoc()
            );
        }
        System.out.println("\n========= Emp와 Salgrade 조인 목록 =========");
        List<EmpVO> esList = dao.empSalgradeJoinData();
        for (EmpVO vo : esList){
            System.out.println(vo.getEmpno()+" " +
                    vo.getEname()+" " +
                    vo.getJob()+" " +
                    vo.getHiredate().toString()+" " +
                    vo.getSal()+" " +
                    vo.getSvo().getGrade());
        }
    }
}
