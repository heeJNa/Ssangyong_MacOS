package dao;
/*create table main_category (
        id number,
        name varchar2(100) constraint maincate_name_nn not null,
        constraint maincate_id_pk primary key (id)
        );
*/
public class MainCategoryVO {
    private int id;
    private String name;
    private String link;

    private SubCategoryVO svo = new SubCategoryVO();
    private DetailCategoryVO dvo = new DetailCategoryVO();

    public SubCategoryVO getSvo() {
        return svo;
    }

    public void setSvo(SubCategoryVO svo) {
        this.svo = svo;
    }

    public DetailCategoryVO getDvo() {
        return dvo;
    }

    public void setDvo(DetailCategoryVO dvo) {
        this.dvo = dvo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
