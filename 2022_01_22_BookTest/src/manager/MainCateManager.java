package manager;

import dao.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
                    Element publisher = doc.selectFirst("span.gd_pub");
                    System.out.println(publisher.text());
                    bvo.setPublisher(publisher.text());

                    // 출간일
                    Element regdate = doc2.selectFirst("span.gd_date");
                    System.out.println(regdate.text());
                    bvo.setRegdate(Date.valueOf(regdate.text()));


                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
