package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.CourseStateCode;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseStateCodeDao {
    @Select("SELECT description FROM course_state WHERE code = #{code}")
    String getCourseStateDesc(int code);

    @Select("SELECT * FROM course_state WHERE code = #{code}")
    CourseStateCode getById(Integer code);

    @Select("SELECT * FROM course_state")
    List<CourseStateCode> getAll();
}
