package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CourseDao {
    @Select("SELECT * FROM course")
    @Results({
            @Result(property = "teacher", column = "teacher", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.CourseStateCodeDao.getById"))
    })
    List<Course> getAll();

    @Select("SELECT * FROM course WHERE id = #{id}")
    @Results({
            @Result(property = "teacher", column = "teacher", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.CourseStateCodeDao.getById"))
    })
    Course getById(Integer id);

    @Select("SELECT * FROM course WHERE name LIKE CONCAT('%', #{search}, '%') OR EXISTS(SELECT * FROM person WHERE person.id = course.teacher AND person.name LIKE CONCAT('%', #{search}, '%'))")
    @Results({
            @Result(property = "teacher", column = "teacher", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.CourseStateCodeDao.getById"))
    })
    List<Course> searchByName(String search);

    @Update("UPDATE course SET name = #{name}, teacher = #{teacher}, intro = #{intro}, book = #{book}, classroom = #{classroom}, number = #{number}, classtime = #{classtime}, state = #{state} where id = #{id}")
    int update(@Param("id") Integer id, @Param("name") String name, @Param("teacher") Integer teacher, @Param("intro") String intro, @Param("book") String book, @Param("classroom") String classroom, @Param("number") Integer number, @Param("classtime") String classtime, @Param("state") Integer state);

    @Insert("INSERT INTO course VALUES(0, #{name}, #{teacher}, #{intro}, #{book}, #{classroom}, #{number}, #{classtime}, #{state})")
    int insert(@Param("name") String name, @Param("teacher") Integer teacher, @Param("intro") String intro, @Param("book") String book, @Param("classroom") String classroom, @Param("number") Integer number, @Param("classtime") String classtime, @Param("state") Integer state);

    @Delete("DELETE FROM course WHERE id = #{id}")
    int delete(Integer id);
}
