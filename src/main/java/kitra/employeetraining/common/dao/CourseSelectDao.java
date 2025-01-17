package kitra.employeetraining.common.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CourseSelectDao {
    @Select("SELECT course FROM course_select WHERE person = #{personId}")
    List<Integer> getSelectedCourses(Integer personId);

    @Select("SELECT COUNT(person) FROM course_select WHERE course = #{courseId}")
    int getSelectedCount(Integer courseId);

    @Update("INSERT INTO course_select VALUES(#{course}, #{person})")
    int selectCourse(@Param("person") Integer personId, @Param("course") Integer courseId);

    @Delete("DELETE FROM course_select WHERE course = #{course} AND person = #{person}")
    int unselectCourse(@Param("person") Integer personId, @Param("course") Integer courseId);
}
