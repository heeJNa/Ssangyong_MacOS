package manager;

import dao.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Date;
import java.util.*;

/* 출처 : www.yes24.com */
public class MainCateManager {
    public static void main(String[] args) {
        MainCateManager mcm = new MainCateManager();
        mcm.getDataManager();
    }

    public void getDataManager(){
            Scanner scan = new Scanner(System.in);
        outer: while (true) {
            System.out.println("1.메인 카테고리   2.서브 카테고리   3.세부 카테고리   4.책 정보  5.종료");
            System.out.print("데이터베이스에 저장 할 데이터의 번호를 입력하시오 >> ");
            int num = scan.nextInt();
            switch (num) {
                case 1:
                    mainCate();
                    break;
                case 2:
                    subCate();
                    break;
                case 3:
                    detailCate();
                    break;
                case 4:
                    getBookInfo();
                    break;
                default:
                    System.out.println("종료합니다!!");
                    break outer;
            }
        }
    }

    private void mainCate() {
        try {
            BookDAO dao = new BookDAO();
            Document doc = Jsoup.connect("http://www.yes24.com/main/default.aspx").get();
            Elements maincate = doc.select("li.quickCate_dept1 a.qTit");
            Elements link = doc.select("div.quickCateSub dt a");
            String[] cate = maincate.text().split(" ");
            MainCategoryVO vo = new MainCategoryVO();
            for (int i = 0; i < cate.length; i++) { // ebook까지만 긁어올꺼면 i<3으로 설정
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

    private void subCate() { // 국내도서, 외국도서,ebook 카테고리 받아오기
        try {
            BookDAO dao = new BookDAO();
            List<MainCategoryVO> list = dao.MainCateList();
            SubCategoryVO vo = new SubCategoryVO();
            // for(int i=0;i< list.size();i++) => 전체 다 긁어 올 시
            for (int i = 0; i < 3; i++) { //  i<2 : 외국도서까지 / i<3 : eBook까지
                Document doc = Jsoup.connect(list.get(i).getLink()).get();
                Elements subcate = doc.select("div.CateLiArea li.cate2d a.lnk_cate2d");

                for (int j = 0; j < subcate.size(); j++) {
                    if (subcate.get(j).text().contains("대량/법인")
                            ||subcate.get(j).text().contains("크레마")) // 필요없는 카테고리 페이지 추가 가능
                        continue;
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


    /* subCategory에서 링크를 받아서 주간베스트 링크를 뿌려준다
    *  세부카테고리로 나누면 사용하지 않아도 됨.
    *  1차 프로젝트는 서브카테고리까지 사용해서 주간베스트를 활용해서 긁어오기로 정함.
    * */
    private List<String> weekBestLink() {
        List<String> weekBestLink = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        try {
            BookDAO dao = new BookDAO();
            List<SubCategoryVO> list = new ArrayList<>();
            System.out.println("삽입 할 카테고리 번호를 고르시오!!");
            System.out.print("1. 국내도서   2.외국도서   3.eBook  4.전체\n>> ");
            int choose = scan.nextInt();
            if(choose>=4){
                list = dao.SubCateList();
            }else
                list = dao.SubCateList(choose);
            int no =0; // 출력용 증감 변수
            System.out.println("========== 주간베스트 링크 출력 ==========");
            for (int i = 0; i < list.size(); i++) {
                Document subCateDoc = Jsoup.connect(list.get(i).getLink()).get(); // subCategory에서 link를 가져온다
                Element bestLink = subCateDoc.selectFirst("div.cateTit_sub span a"); // 주간베스트 들어가기위한 링크
                int endPageNo = getLastPage(bestLink);

                if(!(bestLink.text().equals("주간베스트")) || endPageNo<=6) { // 주간베스트가 없는 sub_Category일 경우 || 주간베스트 page가 5이하인 경우
                    /* eBook 해결해야함 */
                    System.out.println("주간베스트 없거나 적음 : " + list.get(i).getName());
//                    if(dao.MainNameSearch(list.get(i).getMainid()).equalsIgnoreCase("ebook")) //eBook인 경우
                        /* 논의 후 결정 */
                    for (int k = 1; k <= 1; k++) { // 20page만 가져온다. (최대 페이지로 받아올 경우 너무 많은 양이 들어옴)
                        weekBestLink.add(list.get(i).getLink() + "?PageNumber=" + k);
                        System.out.println((no+1)+". "+weekBestLink.get(no++));
                    }
                } else {
                    System.out.print(list.get(i).getName() + " : ");
                    System.out.println(bestLink.attr("href"));

                    for (int j = 0; j < 1; j++) {  // j<endPageNo 값을 이용해서 얼마나 가져올지 정할 수 있다.
                        weekBestLink.add("http://www.yes24.com" + bestLink.attr("href") + "&PageNumber=" + (j + 1));
                        System.out.println((no+1)+". "+weekBestLink.get(no++));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("========== 주간베스트 링크 추출 완료 ==========");
        return weekBestLink;
    }


    /* 주간 베스트의 마지막 페이지를 얻는 메소드 */
    private int getLastPage(Element bestLink){
        int lasgPage=0;
        try {
            Document bestDoc = Jsoup.connect("http://www.yes24.com"+
                                bestLink.attr("href")).get(); //해당 subCategory의 주간베스트
            Element endPage = bestDoc.selectFirst("div.yesUI_pagenS a.bgYUI.end"); // 마지막 페이지가 나와있는 곳
            lasgPage = Integer.parseInt(endPage.attr("href").
                        substring(endPage.attr("href").lastIndexOf('=') + 1).trim()); // 마지막 페이지 번호
//            System.out.println("마지막 페이지 : " + lasgPage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return lasgPage;
    }


    // 세부적인 카테고리에서 받아옴(commit : 119fe158e42374806851c4f4186f55cc0362c596)
    // 현재 코드 : 주간베스트로 진입하여 데이터 긁어옴 : 2022/01/26 국내, 외국도서 완료
    private void getBookInfo() {
        try {
            BookDAO dao = new BookDAO();
            BooksVO bvo = new BooksVO();
            List<String> weekBestList = weekBestLink();
            // weekBestLink()의 for문의 i 초기값을 바꾸면 일치하지 않는 문제 해결해야함. (해결완료)
            for (int j = 0; j < weekBestList.size(); j++) {
                //sub_categoryID를 얻기 위해 사용
                String categoryURL = weekBestList.get(j)
                        .substring(weekBestList.get(j).indexOf('0'),weekBestList.get(j).indexOf("?"))
                        .trim();
//                System.out.println("카테고리 id : " + weekBestList.get(j).substring(weekBestList.get(j).indexOf('0'),weekBestList.get(j).indexOf("?")).trim());
                int cateid = dao.SubIdSearch(categoryURL); //해당 페이지에 sub_category받아오기

                Document bookDoc = Jsoup.connect(weekBestList.get(j)).get();
                Elements bookLink = bookDoc.select("span.imgBdr a"); // 링크에 출력된 책 링크
                Elements imglink = bookDoc.select("span.imgBdr img"); //19세 검사를 위해 전달
                for (int k = 0; k < 5; k++) { // 20개씩 가져온다 => bookLink.size()
                    if (imglink.get(k).attr("src").contains("pd_19_L")) { // 19세 이상은 제외
                        continue; // for문은 continue사용 시 증감함
                    }

                    Document doc2 = Jsoup.connect("http://www.yes24.com" + bookLink.get(k).attr("href")).get();
                    bvo.setCateid(cateid);

                    System.out.println("카테고리 id : " + bvo.getCateid());

                    // 책 제목
                    Element title = doc2.selectFirst("div.gd_titArea h2.gd_name");
                    System.out.println("책 제목 : " + title.text());
                    bvo.setTitle(title.text());

                    // 부가 제목

                    // 저자
                    try {
                        Element author = doc2.selectFirst("span.gd_auth");
                        System.out.println("저자 : " + author.text());
                        bvo.setAuthor(author.text().trim());
                    } catch (Exception e) {
                        // ex) Me Reader &amp; 8 books Library : Peppa Pig 페파 피그 미리더 사운드북 저자는 Peppa pig 인데 실제 사이트에 입력되어 있지 않음.
                        System.out.println("작자 미상 (DB에 공백기입)"); // 출판사 저||작자 미상 둘 중 무엇을 사용?
                        bvo.setAuthor("");
                    }

                    // 출판사
                    try {
                        Element publisher = doc2.selectFirst("span.gd_pub");
                        System.out.println("출판사 : " + publisher.text());
                        bvo.setPublisher(publisher.text().trim());
                    }catch (Exception e){
                        //출판사 없는 것은 아래 예시같이 직접 입력 가능
                        /*
                        if(bvo.getName().contains("VOGUE")) {
                            System.out.println("출판사 : VOGUE");
                            bvo.setName("VOGUE");
                        }*/
                        System.out.println("출판사 없음 (DB에 공백기입)");
                        bvo.setPublisher("");
                    }

                    // 출간일(1안) Date로 바꾸기 위해 yyyy-MM-dd  형식으로 바꿈
                    Element regdate = doc2.selectFirst("span.gd_date");
                    String[] s = regdate.text().split(" ");
                    String date = s[0].substring(0, s[0].lastIndexOf('년')) + "-" +
                            s[1].substring(0, s[1].lastIndexOf('월')) + "-" +
                            s[2].substring(0, s[2].lastIndexOf('일'));
                    System.out.println("출간일 : " + date);
                    bvo.setRegdate(Date.valueOf(date));

                    // 포스터
                    Element poster = doc2.selectFirst("div.gd_imgArea img");
                    System.out.println("포스터 : " + poster.attr("src"));
                    bvo.setPoster(poster.attr("src").trim());

                    // 책 소개
                    try {
                        Element content = doc2.selectFirst("textarea.txtContentText");
                        /* HTML태그들이 포함 */
                        // <br/> 형식으로 작성되어 있음.
                        System.out.println("책 소개 : " + content.text());
                        bvo.setContent(content.text().trim());
                    } catch (Exception e) {
                        // ** 책 소개가 없는 것은 출판사 리뷰가 있는 것도 있는데 추가할지는 미정.
                        System.out.println("책소개 없음");
                        bvo.setContent("");
                    }

                    // 가격 (정가)
                    /* eBook에 [연재]로 단권/전권 가격이 따로 있는 경우가 있음 (미해결)*/
                    Element price = doc2.selectFirst("em.yes_m");
                    System.out.println("정가 : " + price.text()
                            .replace("원","")
                            .replace(",","")
                            .trim());
                    // Integer형으로 삽입
                    bvo.setPrice(Integer.parseInt(price.text()
                            .replace("원","")
                            .replace(",","")
                            .trim()));

                    // 할인율
                    try {
                        Element salesrate = doc2.selectFirst("tr.accentRow td");
                        String rate = salesrate.text()
                                .substring(salesrate.text().indexOf('(') + 1, salesrate.text().lastIndexOf('%'));
                        System.out.println("할인율 : " + Integer.parseInt(rate) + "%");

                        // Integer형으로 삽입
                        bvo.setSalerate(Integer.parseInt(rate));
                    } catch (Exception e) {
                        System.out.println("할인 없음 (DB에 0 입력)");
                        bvo.setSalerate(0);
                    }

                    // 평점
                    try {
                        Element score = doc2.selectFirst("span.gd_rating em.yes_b");
                        System.out.println("평점 : " + score.text());
                        bvo.setScore(Double.parseDouble(score.text()));
                    } catch (Exception e) {
                        // 구매리뷰가 1건도 없는 경우
                        System.out.println("첫번째 구매리뷰를 남겨주세요");
                        bvo.setScore(0);
                    }

                    // 판매 상태
                    Element status = doc2.selectFirst("p.gd_saleState");
                    System.out.println("판매 상태 : " + status.text());
                    bvo.setStatus(status.text().trim());

                    // 책의 쪽수, 무게, 크기, eBook은 파일 용량
                    try { // [연재] 열혈강호, 정령왕 엘퀴네스 => error남 => 이유가 뭐지??
                        Elements bookInfo = doc2.select("tbody.b_size td");

                        if (dao.MainNameSearch(cateid).equalsIgnoreCase("ebook")) {
                            if(bookInfo.get(3).text().contains("|") || bookInfo.get(3).text().contains("파일")) {
                                String eSize = bookInfo.get(3).text()
                                        .replace("파일/용량 안내", "")
                                        .replace(" ", "");
                                System.out.println("파일/용량 : " + eSize);
                                bvo.setSize(eSize);
                            }else{
                                String eSize = bookInfo.get(4).text()
                                        .replace("파일/용량 안내", "")
                                        .replace(" ", "");
                                System.out.println("파일/용량 : " + eSize);
                                bvo.setSize(eSize);
                            }

                        } else {
                            String bSize = bookInfo.get(1).text()
                                    .replace(" ","");
                            System.out.println("쪽수|무게|크기 : " + bSize);
                            bvo.setSize(bSize);
                        }
                        // ISBN13 /* 전집 해결해야함 */
                        Element isbn = doc2.selectFirst("tbody.b_size");

                        if(isbn.text().contains("ISBN13")) {
                            String eIsbn = isbn.text();
                            int isbnIndex = eIsbn.lastIndexOf("ISBN13") + 7;
                            eIsbn = eIsbn.substring(isbnIndex, isbnIndex + 13).trim();
                            System.out.println("isbn : " + eIsbn);
                            bvo.setIsbn(eIsbn);
                        }else{
                                System.out.println("isbn 없음");
                                bvo.setIsbn("");
                        }
                    }catch (Exception e){
                        System.out.println("error");
                    }

                    try {
                        Element tag = doc2.selectFirst("div.gd_tagArea");
                        System.out.println("태그 : " + tag.text().replace(" ",""));

                        bvo.setTag(tag.text().replace(" ",""));
                    } catch (NullPointerException e) {
                        System.out.println("태그 없음");
                    }

                    // 판매지수, 판매량
                    try{
                    Element sellCount = doc2.selectFirst("span.gd_sellNum");
                    String count = sellCount.text();
                    count = count.substring(count.indexOf("수")+1,count.lastIndexOf("판매지수"))
                            .replace(",","")
                            .trim();
                    int sCount = Integer.parseInt(count);
                    System.out.println("판매 지수 : "+ sCount);
                    bvo.setSellCount(sCount);
                    }catch (Exception e){
                        System.out.println("판매 지수 : 0");
                        bvo.setSellCount(0);
                    }




                    //dao.InsertBook(bvo); // DataBase에 삽입

                    System.out.println("===========================================================================================================================");
                    // 출고 예정일 => 대부분 javascript로 작성되어 있음

                    // 할인가 (정가와 할인율 이용해서 계산 가능)
                    /*Element salesPrice = doc2.selectFirst("div.gd_infoTb span.nor_price");
                    System.out.println("할인가 : " + salesPrice.text());*/

                    // 배송비 : 숫자형으로 입력? 출력시 숫자로 변환? - 1차 프로젝트 추가x
                    /*try {
                        Element shipprice = doc2.selectFirst(".deli_des"); // 출력결과 => 배송비 : 무료 배송비 안내
                        String ship=shipprice.text();
                        if(ship.contains(":")) {
                            ship = shipprice.text()
                                    .substring(shipprice.text().indexOf(':') + 1)
                                    .trim();
                        }
                        Element shiptrim = doc2.selectFirst(".deli_des a"); // 배송비 안내 자르기 위해 추출
                        ship = ship.replaceAll(shiptrim.text(), " ").trim();
                        System.out.println("배송비 : " + ship);

                        bvo.setShipprice(ship); //문자 삽입
                    } catch (Exception e) {
                        System.out.println("기본배송비 3000원"); // 기본 배송비 설정해야함
                        bvo.setShipprice("3000원");
                    }*/

                    //태그


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 고민 1 : eBook 테이블은 따로 만들어야 하는가? */

    // 더 세부적인 카테고리 (1차는 여기까지 x)
    private void detailCate() {
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
}
