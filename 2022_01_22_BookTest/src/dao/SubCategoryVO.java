package dao;
/*create table sub_category(
        id number,
        main_id number,
        name varchar2(100) constraint subcate_name_nn not null,
        constraint subcate_id_pk primary key (id),
        constraint subcate_mainid_fk foreign key (main_id) references main_category (id)
        );
*/
public class SubCategoryVO {
    private int id;
    private int mainid;
    private String name;
    private String link;

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

    public int getMainid() {
        return mainid;
    }

    public void setMainid(int mainid) {
        this.mainid = mainid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
