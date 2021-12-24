package com.sist.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
*       보조 스트림
*       ========
*       BufferedReader(읽기) / BufferedWriter(쓰기)
*       BufferedInputStream / BufferedOutputStream
*       ======== 데이터를 메모리에 올려놓고 고속 복 (속도가 향상 => 대용량)
*       ======== 대용량 : 빅데이터 (하둡, 스파크) : ChatBot (트위터 : 데이터 수집)
*       => 바이트 스트림 ==> 문자 스트림으로 변경 (InputStreamReader)
*
*       읽기
*        동영상, 사진, 이미지 => FileInputStream
*        한글이 포함된 파일(text, data) => FileReader
*        대용량(웹, 트위터) => BufferedReader
* */
public class MainClass4 {
    public static void main(String[] args) {
        ArrayList<Member> list = new ArrayList<>();
        try {
            // 파일을 읽는다
            FileInputStream fis = new FileInputStream("/Users/kimheejun/Desktop/test/addr.txt");
            // 메모리에 저장
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            // 한줄씩 읽어서 출력
            while (true){
                String data = br.readLine(); // \n이 한줄
                if(data == null) break; // 읽을 데이터가 없는 경우 => null
                System.out.println(data);
                StringTokenizer st = new StringTokenizer(data, "|");
                // split(), replaceAll() => 정규식 사용
                Member m = new Member(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
                list.add(m);
            }
            fis.close();
            br.close();

            for (Member m : list){
                System.out.println(m.getNo()+" "
                            +m.getName()+" "
                            +m.getSex()+" "
                            +m.getAddr()+" "
                            +m.getTel());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
