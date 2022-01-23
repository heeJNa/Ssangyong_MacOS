package dao;

import java.util.Date;

/*create table books_test
       create table books_test
(
    id           number,
    category_id  number,
    name         varchar2(400) constraint book_name_nn not null ,
    content     varchar2(400) constraint book_content_nn not null ,
    author       varchar2(500) constraint book_author_nn not null ,
    publisher   varchar2(100) constraint book_publisher_nn not null,
    regdate    date constraint book_regdate_nn not null ,
    poster       varchar2(1000) constraint book_poster_nn not null ,
    price        number constraint book_price_nn not null ,
    score        number(2, 1) constraint book_score_nn not null ,
    isbn    number constraint book_isbn_nn not null,
    release_date date,
    salerate number,
    ship_price number constraint book_sp_nn not null ,
    quantity number default 1 constraint book_qt_nn not null ,
    constraint book_id_pk primary key (id),
    constraint book_cateid_fk foreign key (category_id) references detail_category (id)
);
*/
public class BooksVO {
    private int id, cateid, price, salerate, quantity, shipprice, isbn;
    private String name,content,author,publisher,poster;
    private double score;
    private Date regdate, releasedate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCateid() {
        return cateid;
    }

    public void setCateid(int cateid) {
        this.cateid = cateid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSalerate() {
        return salerate;
    }

    public void setSalerate(int salerate) {
        this.salerate = salerate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getShipprice() {
        return shipprice;
    }

    public void setShipprice(int shipprice) {
        this.shipprice = shipprice;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }
}
