package manager;

import dao.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainCateManager {
    public static void main(String[] args) {
        MainCateManager mcm = new MainCateManager();
//        mcm.mainCate();
//        mcm.subCate();
//        mcm.detailCate();
          mcm.getBookInfo();
    }

    public void mainCate() {
        try {
            BookDAO dao = new BookDAO();
            Document doc = Jsoup.connect("http://www.yes24.com/main/default.aspx").get();
            Elements maincate = doc.select("li.quickCate_dept1 a.qTit");
            Elements link = doc.select("div.quickCateSub dt a");
            String[] cate = maincate.text().split(" ");
            MainCategoryVO vo = new MainCategoryVO();
            for (int i = 0; i < cate.length; i++) {
                vo.setId(i + 1);
                vo.setName(cate[i]);
                vo.setLink(link.get(i).attr("href"));

                dao.InsertMainCate(vo);
            }
            System.out.println("오라클에 저장 완료!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subCate() { // 국내도서, 외국도서카테고리 받아오기
        try {
            BookDAO dao = new BookDAO();
            List<MainCategoryVO> list = dao.MainCateList();
            SubCategoryVO vo = new SubCategoryVO();
            for (int i = 0; i < 2; i++) { // for(int i=0;i< list.size();i++)
                System.out.println(list.get(i).getLink());
                Document doc = Jsoup.connect("http://www.yes24.com/Mall/Main/Book/001?CategoryNumber=001").get();
                Elements subcate = doc.select("div.CateLiArea li.cate2d a.lnk_cate2d");

                for (int j = 0; j < subcate.size(); j++) {
                    System.out.println(subcate.get(j).text());
                    vo.setMainid(list.get(i).getId());
                    vo.setName(subcate.get(j).text());
                    vo.setLink(subcate.get(j).attr("href"));

                    dao.InsertSubCate(vo);
                }
            }
            System.out.println("오라클에 저장 완료!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detailCate() {
        try {
            BookDAO dao = new BookDAO();
            List<MainCategoryVO> mList = dao.MainCateList();
            DetailCategoryVO vo = new DetailCategoryVO();
//            System.out.println(mList.get(0).getLink());

            int num = 0;  //subID 외래키를 위해 설정
            for (int i = 0; i < 2; i++) {
                Document doc = Jsoup.connect(mList.get(i).getLink()).get();
                for (int j = 0; j < dao.SubCateCount(i + 1); j++) {
                    Elements decate = doc.select("div#mCateLi_00" + j + " li a");
                    for (int k = 0; k < decate.size(); k++) {
                        System.out.println(decate.get(k).text());
                        System.out.println(decate.get(k).attr("href"));
                        if(i!=0) {
                            num += dao.SubCateCount(i);
                        }
                        System.out.println(j+num+1);
                        vo.setSubid(j+num+1);
                        vo.setName(decate.get(k).text());
                        vo.setLink(decate.get(k).attr("href"));

                        dao.InsertDetailCate(vo);
                    }
                }
            }
            System.out.println("오라클 저장 완료!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBookInfo(){
        try{
            BookDAO dao = new BookDAO();
            List<DetailCategoryVO> list = dao.DetailCateList();
            BooksVO bvo = new BooksVO();
            for(int i = 0; i< list.size();i++){
                bvo.setCateid(list.get(i).getId());
                Document doc = Jsoup.connect(list.get(i).getLink()+"?PageNumber=1").get();
                System.out.println(list.get(i).getLink()+"?PageNumber=1");
                Elements link = doc.select("span.imgBdr a");
                for(int j =0;i< link.size();j++){
                    Document doc2 = Jsoup.connect("http://www.yes24.com"+link.get(j).attr("href")).get();
                    // 포스터
                    Element poster = doc2.selectFirst("div.gd_imgArea img");
                    System.out.println(poster.attr("src"));
                    bvo.setPoster(poster.attr("src"));

                    // 책 제목
                    Element title = doc2.selectFirst("div.gd_titArea h2.gd_name");
                    System.out.println(title.text());
                    bvo.setName(title.text());

                    // 부가 제목 (추가 미정)

                    // 저자
                    Element author = doc2.selectFirst("span.gd_auth");
                    System.out.println(author.text());
                    bvo.setAuthor(author.text());

                    // 출판사
                    Element publisher = doc2.selectFirst("span.gd_pub ");
                    System.out.println(publisher.text());
                    bvo.setPublisher(publisher.text());

                    // 출간일(1안)
                    Element regdate = doc2.selectFirst("span.gd_date");
                    String[] s = regdate.text().split(" ");
                    String date = s[0].substring(0,s[0].lastIndexOf('년'))+"-"+
                            s[1].substring(0,s[1].lastIndexOf('월'))+"-"+
                            s[2].substring(0,s[2].lastIndexOf('일'));
                    System.out.println(date);
                    bvo.setRegdate(Date.valueOf(date));

                    // 평점
                    Element score = doc2.selectFirst("span.gd_rating em.yes_b");
                    System.out.println(score.text());
                    bvo.setScore(Double.parseDouble(score.text()));

                    //가격 (정가
                    Element price = doc2.selectFirst("div.gd_infoTb span");
                    System.out.println(price.text());
                    bvo.setPrice(price.text());

                    // 할인율
                    Element salesrate =doc2.selectFirst("tr.accentRow td");
                    String rate = salesrate.text().substring(salesrate.text().indexOf('(')+1,salesrate.text().lastIndexOf('%'));
                    System.out.println(rate);
                    bvo.setSalerate(Integer.parseInt(rate));

                    // 배송비 (1안)
                    Element shipprice = doc2.selectFirst("li.deli_des");
                    String ship = shipprice.text().substring(shipprice.text().indexOf(':')).trim();
                    Element shiptrim = doc2.selectFirst("li.deli_des  a");
//                    System.out.println(shiptrim.text());
                    ship = ship.replaceAll(shiptrim.text()," ").trim();
//                    System.out.println(ship);
                    bvo.setShipprice(ship);

                    // 출고 예정일(어려움)

                    // 책 쪽수, 무게, 크기 (추가 미정)

                    // 출간일, 책(쪽수,무게,크기) , ISBN13, 배송비
                    Elements bookinfo = doc2.select("table.tb_nor td.lastcol");
                    // 출간일
                    String[] s2 = bookinfo.get(0).text().split(" ");
                    String date2 = s2[0].substring(0,s2[0].lastIndexOf('년'))+"-"+
                            s2[1].substring(0,s2[1].lastIndexOf('월'))+"-"+
                            s2[2].substring(0,s2[2].lastIndexOf('일'));
                    System.out.println(date2);
//                    bvo.setRegdate(Date.valueOf(date2));

                    // 책(쪽수,무게,크기)
                    String booksize = bookinfo.get(1).text().substring(bookinfo.get(1).text().indexOf('기')+1).trim();
                    String [] size = booksize.split("\\|");
                    for (String z : size )
                        System.out.println(z.trim());

                    // ISBN13
                    String isbn = bookinfo.get(2).text().replaceAll("ISBN13","").trim();
                    System.out.println(isbn);
                    bvo.setIsbn(isbn);

                    // 배송비
                    String shipprice2 = bookinfo.get(4).text().substring(bookinfo.get(4).text().lastIndexOf(':')+1).trim();
                    System.out.println(shipprice2);
//                    bvo.setShipprice(shipprice2);

                    //태그 (추가 미정)
                    try {

                    Element tag = doc2.selectFirst("div.gd_tagArea");
                    System.out.println(tag.text());
                    }catch (NullPointerException e){
                        System.out.println("태그 없음");

                    }

                    // 책 소개
                    try {
                        Element content = doc2.selectFirst("textarea.txtContentText");
                        System.out.println(content.text().replaceAll("<.*[^가-힣]*.>", "").replace("\n", "").trim());
                        bvo.setContent(content.text().replaceAll("<.*[^가-힣]*.>", "").replace("\n", "").trim());
                    }catch (Exception e){
                        System.out.println("");
                        bvo.setContent("");
                    }
                    // 목차 (추가 미정)

//                    dao.InsertBook(bvo);
               }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
