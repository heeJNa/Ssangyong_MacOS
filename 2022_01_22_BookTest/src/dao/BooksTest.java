package dao;

import java.util.Date;

/*create table books_test
        (
        id           number,
        sub_id number,
        name         varchar2(400) constraint book_name_nn not null ,
        content     varchar2(400) constraint book_content_nn not null ,
        author       varchar2(500) constraint book_author_nn not null ,
        publisher   varchar2(100) constraint book_publisher_nn not null,
        regdate    date constraint book_regdate_nn not null ,
        poster       varchar2(1000) constraint book_poster_nn not null ,
        price        number constraint book_price_nn not null ,
        score        number(2, 1) constraint book_score_nn not null ,
        release_date date,
        salerate number,
        ship_price number constraint book_sp_nn not null ,
        quantity number default 0 constraint book_qt_nn not null ,
        constraint book_id_pk primary key (id),
        constraint book_subid_fk foreign key (sub_id) references sub_category (id)
        );
*/
public class BooksTest {
    private int id, subid, price, salerate, quantity, shipprice;
    private String name,content,author,publisher,poster;
    private double score;
    private Date regdate, releasedate;

}
