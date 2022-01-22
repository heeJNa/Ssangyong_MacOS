package manager;

import dao.BookDAO;
import dao.MainCategoryVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainCateManager {
    public static void main(String[] args) {
        MainCateManager mcm = new MainCateManager();
//        mcm.mainCate();
        mcm.subCate();

    }

    public void mainCate(){
        try {
            BookDAO dao = new BookDAO();
            Document doc = Jsoup.connect("http://www.yes24.com/main/default.aspx").get();
            Elements maincate = doc.select("div.quickCateSub dt a");
//            System.out.println(maincate.text());
            String[] cate = maincate.text().split(" ");
            MainCategoryVO vo = new MainCategoryVO();
            for(int i = 0; i<cate.length;i++){
//                System.out.println(s.substring(0,s.lastIndexOf('하')));
                vo.setId(i+1);
                vo.setName(cate[i].substring(0,cate[i].lastIndexOf('하')));

                dao.InsertMainCate(vo);
            }
            System.out.println("오라클에 저장 완료!!");
//                vo.setId(i+1);
//                vo.setName(maincate.text().substring(0,maincate.text().lastIndexOf("하")));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void subCate(){
        try{
            BookDAO dao = new BookDAO();
            Document doc = Jsoup.connect("http://www.yes24.com/main/default.aspx").get();
            for(int i =0; i<)
            Elements subcate = doc.select("div.quickCateZone li.quickcate_dept1");
            System.out.println(subcate.text().substring(0,subcate.text().indexOf('')));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
