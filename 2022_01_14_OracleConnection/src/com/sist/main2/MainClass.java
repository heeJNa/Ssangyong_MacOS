package com.sist.main2;
import java.util.*;
public class MainClass {
    public static void main(String[] args) {
        ZipcodeDAO dao = new ZipcodeDAO();


        Scanner scan = new Scanner(System.in);
        System.out.print("동을 입력하시오 : ");
        String dong = scan.next();

        int count = dao.zipcodeFindCount(dong);
        List<Zipcode> list = dao.zipcodeListData(dong);

        if(count==0){
            System.out.println("★★★★★ 검색한 결과가 없습니다!! ★★★★★");
        }
        else{
            System.out.println("검색결과 : " +count+"건");
            for (Zipcode z : list){
                System.out.println(z.getZipcode() + " " +
                        z.getAddress());
            }
        }


    }
}
