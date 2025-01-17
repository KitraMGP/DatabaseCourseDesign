package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PersonDao {
    @Select("SELECT * FROM person WHERE id = #{id}")
    @Results({
            @Result(property = "password", column = "passwd"),
            @Result(property = "eduLevel", column = "edu_level"),
            @Result(property = "department", column = "department", one = @One(select = "kitra.employeetraining.common.dao.DepartmentDao.getByIdWithoutManager")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.PersonStateDao.getById")),
            @Result(property = "authority", column = "authority", one = @One(select = "kitra.employeetraining.common.dao.AuthorityDao.getById"))
    })
    Person getById(Integer id);

    @Select("SELECT * FROM person")
    @Results({
            @Result(property = "password", column = "passwd"),
            @Result(property = "eduLevel", column = "edu_level"),
            @Result(property = "department", column = "department", one = @One(select = "kitra.employeetraining.common.dao.DepartmentDao.getByIdWithoutManager")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.PersonStateDao.getById")),
            @Result(property = "authority", column = "authority", one = @One(select = "kitra.employeetraining.common.dao.AuthorityDao.getById"))
    })
    List<Person> getAll();

    @Results({
            @Result(property = "password", column = "passwd"),
            @Result(property = "eduLevel", column = "edu_level"),
            @Result(property = "department", column = "department", one = @One(select = "kitra.employeetraining.common.dao.DepartmentDao.getByIdWithoutManager")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.PersonStateDao.getById")),
            @Result(property = "authority", column = "authority", one = @One(select = "kitra.employeetraining.common.dao.AuthorityDao.getById"))
    })
    @Select("SELECT * FROM person WHERE authority != 0")
    List<Person> getAllExceptAdmins();

    @Results({
            @Result(property = "password", column = "passwd"),
            @Result(property = "eduLevel", column = "edu_level"),
            @Result(property = "department", column = "department", one = @One(select = "kitra.employeetraining.common.dao.DepartmentDao.getByIdWithoutManager")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.PersonStateDao.getById")),
            @Result(property = "authority", column = "authority", one = @One(select = "kitra.employeetraining.common.dao.AuthorityDao.getById"))
    })
    @Select("SELECT * FROM person WHERE department = #{department}")
    List<Person> getPersonsInDepartment(Integer department);

    @Select("SELECT * FROM person WHERE username = #{username}")
    Person getByUserName(String username);

    @Results({
            @Result(property = "password", column = "passwd"),
            @Result(property = "eduLevel", column = "edu_level"),
            @Result(property = "department", column = "department", one = @One(select = "kitra.employeetraining.common.dao.DepartmentDao.getByIdWithoutManager")),
            @Result(property = "state", column = "state", one = @One(select = "kitra.employeetraining.common.dao.PersonStateDao.getById")),
            @Result(property = "authority", column = "authority", one = @One(select = "kitra.employeetraining.common.dao.AuthorityDao.getById"))
    })
    @Select("SELECT * FROM person WHERE name LIKE CONCAT('%', #{search}, '%') OR username LIKE CONCAT('%', #{search}, '%') OR EXISTS(SELECT * FROM authority_code WHERE description LIKE CONCAT('%', #{search}, '%') AND person.authority = authority_code.code)")
    List<Person> searchByName(String search);

    @Update("UPDATE person SET username = #{username}, passwd = #{password}, authority = #{authority}, name = #{name}, sex = #{sex}, birthday = #{birthday}, department = #{department}, job = #{job}, edu_level = #{eduLevel}, speciaty = #{speciaty}, address = #{address}, tel = #{tel}, email = #{email}, state = #{state}, remark = #{remark} WHERE id = #{id}")
    int update(@Param("id") Integer id, @Param("username") String username, @Param("password") String password, @Param("authority") Integer authority, @Param("name") String name, @Param("sex") String sex, @Param("birthday") String birthday, @Param("department") Integer department, @Param("job") String job, @Param("eduLevel") String eduLevel, @Param("speciaty") String speciaty, @Param("address") String address, @Param("tel") String tel, @Param("email") String email, @Param("state") Integer state, @Param("remark") String remark);

    @Insert("INSERT INTO person VALUES(0, #{username}, #{password}, #{authority}, #{name}, #{sex}, #{birthday}, #{department}, #{job}, #{eduLevel}, #{speciaty}, #{address}, #{tel}, #{email}, #{state}, #{remark})")
    int insert(@Param("username") String username, @Param("password") String password, @Param("authority") Integer authority, @Param("name") String name, @Param("sex") String sex, @Param("birthday") String birthday, @Param("department") Integer department, @Param("job") String job, @Param("eduLevel") String eduLevel, @Param("speciaty") String speciaty, @Param("address") String address, @Param("tel") String tel, @Param("email") String email, @Param("state") Integer state, @Param("remark") String remark);

    @Delete("DELETE FROM person WHERE id = #{id}")
    int delete(Integer id);

    @Select("SELECT passwd FROM person WHERE id = #{id}")
    String getPasswordHash(int userId);

    @Update("UPDATE person SET passwd = #{password} where id = #{id}")
    int setPassword(@Param("id") Integer id, @Param("password") String password);
}
