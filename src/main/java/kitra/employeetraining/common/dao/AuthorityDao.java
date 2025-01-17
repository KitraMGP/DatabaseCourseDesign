package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.Authority;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthorityDao {
    @Select("SELECT * FROM authority_code WHERE code = #{code}")
    Authority getById(Integer code);

    @Select("SELECT * FROM authority_code")
    List<Authority> getAll();
}
