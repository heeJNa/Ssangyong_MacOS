package com.sist.manager;

import com.sist.dao.Books;
import com.sist.dao.BooksDAO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BooksManager {
    // 오라클에 연결 => BooksDAO
    private BooksDAO dao = new BooksDAO(); // 데이터를 오라클에 추가

    public void booksGetData() {
        // try~catch (예외복구), throws(예외회피 => 선언)
        // 사전에 에러발생을 차단하는 프로그램 => 에러를 잡아주는 것은 아니고
        // 에러난 부분을 점프해서 => 다음문장으로 이동
        // 에러난 부분을 확인 => getMessage(), printStackTrace()
        try {
            // 파일 읽기 => 저장
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream("/Users/kimheejun/Desktop/test/books.txt");
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(fis));
            // 파일 전체를 읽어서 => 임시저장
            while (true) {
                String data = br.readLine();
                if (data == null) break;
                sb.append(data);
            }
//            System.out.println(sb.toString()); // 메모리에 저장된 데이터를 출력
            // <> => 태그 별 데이터 수집
            // <div id="a"> => div#a  => div:eq(0)
            // <div class="b"> => div.b => div:eq(1)
            // <div> => HTML
            // Jsoup => HTML parser => 필요한 데이터를 찾아주는 역할
            Document doc = Jsoup.parse(sb.toString());
            /*
             *       connect() => 실제 URL을 이용해서 웹서버에 접근
             *       parse() => HTML문자열, 파일
             */
            Elements title = doc.select("h4.main-title");
            Elements poster = doc.select("div.book-list-image img");
            Elements content = doc.select("div.sub-title");
            Elements author = doc.select("div.book-info span.author");
            Elements price = doc.select("div.book-info");
            Elements regdate = doc.select("div.book-info span.pub-date");
            Elements isbn = doc.select("div.book-info span.isbn");
            Elements tags = doc.select("div.book-info span.tag");
            /*
            *       <div>값</div> ==> text()
            *       <img src ="값"> ==> attr("src")
            * */
            for (int i = 0; i < title.size(); i++) {
                String title_data = title.get(i).text();
                String poster_data = poster.get(i).attr("src");
                String author_data = author.get(i).text();
                String regdate_data = regdate.get(i).text().replace("|","");
                String isbn_data = isbn.get(i).text();
                String tags_data = tags.get(i).text();

                String cd =""; // sub-title이 없는 데이터를 ""으로 설정
                try {
                    String content_data = content.get(i).text();
                    cd = content_data;
                }catch (Exception e){}

                String price_data = price.get(i).text();
                String p = "";
                StringTokenizer st = new StringTokenizer(price_data,"|");
                int count = st.countTokens();
                if(count==4){
                    String[] ss = price_data.split("\\|");
                    p=ss[1];
                }else{
                    String[] ss = price_data.split("\\|");
                    p=ss[2];
                }
                String au = author_data.substring(0,author_data.lastIndexOf("지")).trim();
                String is = isbn_data.substring(isbn_data.indexOf(":")+1).trim();

                System.out.println("번호 : " + (i+1));
                System.out.println("제목 : " + title_data);
                System.out.println("저자 : " + au);
//                System.out.println("그림 : " + poster_data);
//                System.out.println("소개 : " + cd);
//                System.out.println("가격 : " + p);
//                System.out.println("등록일 : " + regdate_data);
//                System.out.println("ISBN : " + isbn_data);
//                System.out.println("태그 : " + tags_data);

                // 오라클에 데이터 첨부
                Books book = new Books();
                book.setNo(i+1);
                book.setTitle(title_data);
                book.setPoster(poster_data);
                book.setContent(cd);
                book.setAuthor(au);
                book.setPrice(p);
                book.setRegdate(regdate_data);
                book.setIsbn(isbn_data);
                book.setTags(tags_data);

                dao.dbInsert(book);

                // 읽은 속도 != 오라클 저장
                Thread.sleep(300);
            }
            System.out.println("********* 오라클 저장 완료!! *********");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BooksManager bm = new BooksManager();
        bm.booksGetData();
    }
}
