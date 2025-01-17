package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.dao.AuthorityDao;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = AuthorityDao.class)
@Alias("authority_code")
public class Authority implements Entity {
    @KeyColumn
    @ImportantColumn
    @ColumnName("代码")
    private int code;
    @ImportantColumn
    @ColumnName("描述")
    private String description;

    public Authority() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
