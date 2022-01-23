package dao;
import com.sun.tools.javac.Main;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;
import java.util.*;
public class BookDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL="jdbc:oracle:thin:@db202112271622_medium?TNS_ADMIN=/Users/kimheejun/Documents/Wallet_DB202112271622";

    public BookDAO(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnection(){
        try {
            conn = DriverManager.getConnection(URL,"admin","Gmlwnsskgus!@1208");
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
            String sql="INSERT INTO main_category VALUES " +
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
            String sql="INSERT INTO sub_category VALUES " +
                    "(subcate_id_seq.nextval,?,?,?)";
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
            String sql = "INSERT INTO books_test VALUES((SELECT NVL(MAX(id),1)+1 FROM book_test),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
    }

    public List<MainCategoryVO> MainCateList(){ //MainCategory를 가져온다
        List<MainCategoryVO> list = new ArrayList<>();
        try{
            getConnection();
            String sql ="SELECT id,name,link FROM main_category";
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


    public int SubCateCount(int mainCateId){
        int subCateCount = 0;
        try{
            getConnection();
            String sql="SELECT count(*) FROM sub_category WHERE main_id = ?";
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




   /* public List<MainCategoryVO> MainSubCateList(){ // SubCategory를 가져온다
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


    /*public int SubIdSearch(String name){
        int subID = 0;
        try{
            getConnection();
            String sql = "SELECT id FROM sub_category WHERE name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            subID = rs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            disConnection();
        }
        return subID;
    }*/
}