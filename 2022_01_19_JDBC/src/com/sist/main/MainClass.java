package com.sist.main;

import com.sist.dao.CategoryVO;
import com.sist.dao.FoodDAO;

import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        FoodDAO dao=new FoodDAO();
        // 카테고리를 읽어서 출력
        List<CategoryVO> list=dao.categoryListData();
        for(CategoryVO vo:list) // for-each (출력용)
        {
            System.out.println(vo.getCno()+"."
                    +vo.getTitle()+" "+vo.getLink());
        }
    }
}
