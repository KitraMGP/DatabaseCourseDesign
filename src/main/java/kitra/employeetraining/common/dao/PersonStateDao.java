package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.PersonState;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PersonStateDao {
    @Select("SELECT * FROM person_state WHERE code = ${code}")
    PersonState getById(Integer code);

    @Select("SELECT * FROM person_state")
    List<PersonState> getAll();
}
