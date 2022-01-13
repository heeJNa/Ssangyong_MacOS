package com.sist.dao;
// 데이터형
/*
*       객체지향 프로그램 : 재사용
*       재사용 => 기존에 있는 기능, 추가, 수정, 데이터 보안
*       =========================================
*       객체지향의 3대 요소
*       재사용 : 상속(수정, 추가) => is-a , 포함(변경 사항이 없는 경우) => has -a
*
* */
// 책 한권에 대한 정보를 저장 ==> 클래스 (한 개, 한 명)
// 캡슐화 ==> 시큐어코딩
public class Books {
    private int no;
    private String title;
    private String poster;
    private String content;
    private String author;
    private String price;
    private String regdate;
    private String isbn;
    private String tags;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
