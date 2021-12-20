package com.sist.collection;
// => 614
// 1.Iterator, ListIterator
// => 컬렉션 클래스의 데이터에 접근이 쉽게 만들어주는 인터페이스
// ArrayList, LinkedList, Vector => 순서(인덱스)
// Set, Map => 순서가 없다 (루프를 사용하면 => Iterator를 이용해서 데이터 순차적으로 출력
// Iterator => hasNext() : 데이터가 존재할때까지
// 데이터 읽기 => next() => 순차적으로 데이터 읽기가 가능 (빅데이터)
import java.util.*;

public class MainClass5 {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("사과");     //0
        list.add("배");      //1
        list.add("참외");     //2
        list.add("딸기");     //3
        list.add("복숭아");   //4
        //출력
        for(int i =0;i< list.size();i++){
            System.out.println(i+"."+list.get(i));
        }
        /*System.out.println("====== while ======");
        int i = 0;
        while(!list.isEmpty()){
            System.out.println(list.get(i));
            i++;
        }*/
        System.out.println("====== Iterator ======");
        Iterator<String> it = list.iterator(); //Set, Map (인덱스가 없는 컬렉션)
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("====== ListIterator ======");
        ListIterator<String> lit = list.listIterator();
        while(lit.hasNext()){   //위 => 아래
            System.out.println(lit.next());
        }
        System.out.println("====== 역순으로 출력 ======");
        while (lit.hasPrevious()) { // 아래 => 위
            System.out.println(lit.previous());
        }
    }
}
