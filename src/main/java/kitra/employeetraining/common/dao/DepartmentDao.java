package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DepartmentDao {

    @Select("SELECT * FROM department WHERE id = #{id}")
    @Results({
            @Result(property = "manager", column = "manager", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById"))
    })
    Department getById(Integer id);

    @Select("SELECT id, name, intro FROM department WHERE id = #{id}")
    Department getByIdWithoutManager(Integer id);

    @Select("SELECT * FROM department")
    @Results({
            @Result(property = "manager", column = "manager", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById"))
    })
    List<Department> getAll();

    @Select("SELECT * FROM department WHERE name LIKE CONCAT('%', #{search}, '%')")
    @Results({
            @Result(property = "manager", column = "manager", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById"))
    })
    List<Department> searchByName(String search);

    @Update("UPDATE department SET name = #{name}, manager = #{manager}, intro = #{intro} WHERE id = #{id}")
    int update(@Param("id") Integer id, @Param("name") String name, @Param("manager") Integer manager, @Param("intro") String intro);

    @Insert("INSERT INTO department VALUES(0, #{name}, #{manager}, #{intro})")
    int insert(@Param("name") String name, @Param("manager") Integer manager, @Param("intro") String intro);

    @Delete("DELETE FROM department WHERE id = #{id}")
    int delete(Integer id);
}
