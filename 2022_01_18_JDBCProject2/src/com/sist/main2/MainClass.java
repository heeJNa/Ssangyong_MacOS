package com.sist.main2;

import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        BookDAO dao = new BookDAO();
        List<BookVO> list = dao.bookListData();
        for(BookVO vo: list){
            System.out.println(vo.getBookid()+" " +
                    vo.getBookname()+" " +
                    vo.getPublisher()+" " +
                    vo.getStrPrice());
        }
    }
}
