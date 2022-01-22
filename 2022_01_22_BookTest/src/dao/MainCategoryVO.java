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
