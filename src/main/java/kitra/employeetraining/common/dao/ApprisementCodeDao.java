package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.ApprisementCode;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ApprisementCodeDao {
    @Select("SELECT description FROM apprisement_code WHERE code = #{code}")
    String getApprisementDesc(int code);

    @Select("SELECT * FROM apprisement_code WHERE code = #{code}")
    ApprisementCode getById(Integer code);

    @Select("SELECT * FROM apprisement_code")
    List<ApprisementCode> getAll();
}
