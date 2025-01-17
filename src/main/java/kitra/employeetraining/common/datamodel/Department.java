package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.dao.DepartmentDao;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = DepartmentDao.class)
@Alias("department")
public class Department implements Entity {
    @KeyColumn
    @ImportantColumn
    @ColumnName("部门编号")
    private int id;
    @ImportantColumn
    @ColumnName("部门名称")
    private String name;
    @ImportantColumn
    @ColumnName("部门经理")
    private Person manager;
    @ColumnName("简介")
    private String intro;

    public Department() {
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(Person manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
