package dao;
import com.sun.tools.javac.Main;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.*;
public class BookDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL="jdbc:oracle:thin:@211.63.89.131:1521:XE";

    public BookDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnection(){
        try {
            conn = DriverManager.getConnection(URL,"hr","happy");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disConnection() {
        try{
            if(ps != null) ps.close();
            if(conn != null)conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void InsertMainCate(MainCategoryVO vo){
        try {
            getConnection();
            String sql="INSERT INTO main_category_3 VALUES " +
                    "(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getId());
            ps.setString(2,vo.getName());
            ps.setString(3,vo.getLink());

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }

    public void InsertSubCate(SubCategoryVO vo){
        try {
            getConnection();
            String sql="INSERT INTO sub_category_3 VALUES " +
                    "((select nvl(max(id)+1,1)FROM sub_category_3),?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getMainid());
            ps.setString(2,vo.getName());
            ps.setString(3,vo.getLink());

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }

    public void InsertDetailCate(DetailCategoryVO vo){
        try {
            getConnection();
            String sql="INSERT INTO detail_category VALUES " +
                    "(decate_id_seq.nextval,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getSubid());
            ps.setString(2,vo.getName());
            ps.setString(3,vo.getLink());


            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }

    public void InsertBook(BooksVO vo){
        try{
            getConnection();
            String sql = "INSERT INTO books_3 VALUES((SELECT NVL(MAX(id),0)+1 FROM books_3),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,vo.getCateid()); // ?????? ???????????? ?????????
            ps.setString(2,vo.getTitle()); //??? ??????
            ps.setString(3,vo.getAuthor()); // ??? ??????
            ps.setString(4,vo.getPublisher()); // ??? ?????????
            ps.setDate(5,vo.getRegdate()); // ?????????
            ps.setString(6,vo.getPoster()); // ??? ?????????
            ps.setString(7,vo.getContent()); // ??? ??????
            ps.setInt(8,vo.getPrice()); // ??? ??????
            ps.setInt(9,vo.getSalerate()); // ??? ?????????
            ps.setDouble(10,vo.getScore()); // ??? ??????
            ps.setString(11,vo.getIsbn()); // isbn13
            ps.setString(12,vo.getSize()); // ??? ??????,??????,??????(????????????)
            ps.setString(13,vo.getStatus()); // ????????????
            ps.setString(14,vo.getTag()); // ??????
            ps.setInt(15,vo.getSellCount());// ?????????


            ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }

    public List<MainCategoryVO> MainCateList(){ //MainCategory??? ????????????
        List<MainCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT id,name,link FROM main_category_3";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                MainCategoryVO vo = new MainCategoryVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setLink(rs.getString(3));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }



    public List<SubCategoryVO> SubCateList(){ //subCategory??? ????????????
        List<SubCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT id,main_id,name,link FROM sub_category_3 ORDER BY id";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SubCategoryVO vo = new SubCategoryVO();
                vo.setId(rs.getInt(1));
                vo.setMainid(rs.getInt(2));
                vo.setName(rs.getString(3));
                vo.setLink(rs.getString(4));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }


    public List<SubCategoryVO> SubCateList(int main_id){ //subCategory??? ????????????
        List<SubCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT id,main_id,name,link FROM sub_category_3 WHERE main_id=? ORDER BY id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,main_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                SubCategoryVO vo = new SubCategoryVO();
                vo.setId(rs.getInt(1));
                vo.setMainid(rs.getInt(2));
                vo.setName(rs.getString(3));
                vo.setLink(rs.getString(4));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }

    public int SubCateCount(int mainCateId){
        int subCateCount = 0;
        try{
            getConnection();
            String sql="SELECT count(*) FROM sub_category_3 WHERE main_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,mainCateId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            subCateCount = rs.getInt(1);
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            disConnection();
        }
        return subCateCount;
    }



    // main??? sub??????????????? join??????.
    /*public List<MainCategoryVO> MainSubCateList(){
        List<MainCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT main.id, main.name, sub.name, sub.id, main_id, sub.link FROM main_category main,sub_category sub WHERE main.id=main_id order by sub.id";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                MainCategoryVO vo = new MainCategoryVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.getSvo().setName(rs.getString(3));
                vo.getSvo().setId(rs.getInt(4));
                vo.getSvo().setMainid(rs.getInt(5));
                vo.getSvo().setLink(rs.getString(6));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }*/

    public String MainNameSearch(int sub_id){
        String mainName = "";
        try{
            getConnection();
            String sql = "SELECT name FROM main_category_3 WHERE id=(SELECT main_id FROM sub_category_3 where id=?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,sub_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            mainName = rs.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return mainName;
    }

    public int SubIdSearch(String link){
        int subID = 0;
        try{
            getConnection();
            String sql = "SELECT id FROM sub_category_3 WHERE link LIKE '%'||?||'%'";
            ps = conn.prepareStatement(sql);
            ps.setString(1,link);
            ResultSet rs = ps.executeQuery();
            rs.next();
            subID = rs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return subID;
    }
}

/*public List<DetailCategoryVO> DetailCateList(){
        List<DetailCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT id,sub_id,name,link FROM detail_category ORDER BY id";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                DetailCategoryVO vo = new DetailCategoryVO();
                vo.setId(rs.getInt(1));
                vo.setSubid(rs.getInt(2));
                vo.setName(rs.getString(3));
                vo.setLink(rs.getString(4));

                list.add(vo);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return list;
    }*/