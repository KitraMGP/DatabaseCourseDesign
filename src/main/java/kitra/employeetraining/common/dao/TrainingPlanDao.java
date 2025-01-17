package kitra.employeetraining.common.dao;

import kitra.employeetraining.common.datamodel.TrainingPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TrainingPlanDao {
    @Select("SELECT * FROM training_plan ORDER BY person")
    @Results({
            @Result(property = "person", column = "person", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "course", column = "course", one = @One(select = "kitra.employeetraining.common.dao.CourseDao.getById")),
            @Result(property = "apprisement", column = "apprisement", one = @One(select = "kitra.employeetraining.common.dao.ApprisementCodeDao.getById")),
            @Result(property = "examDate", column = "exam_date")
    })
    List<TrainingPlan> getAll();

    @Select("SELECT * FROM training_plan where id = #{id}")
    @Results({
            @Result(property = "person", column = "person", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "course", column = "course", one = @One(select = "kitra.employeetraining.common.dao.CourseDao.getById")),
            @Result(property = "apprisement", column = "apprisement", one = @One(select = "kitra.employeetraining.common.dao.ApprisementCodeDao.getById")),
            @Result(property = "examDate", column = "exam_date")
    })
    TrainingPlan getById(Integer id);

    @Select("SELECT * FROM training_plan where person = #{personId}")
    @Results({
            @Result(property = "person", column = "person", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "course", column = "course", one = @One(select = "kitra.employeetraining.common.dao.CourseDao.getById")),
            @Result(property = "apprisement", column = "apprisement", one = @One(select = "kitra.employeetraining.common.dao.ApprisementCodeDao.getById")),
            @Result(property = "examDate", column = "exam_date")
    })
    List<TrainingPlan> getByPerson(Integer personId);

    @Select("SELECT * FROM training_plan where person = #{personId} AND course = #{courseId}")
    @Results({
            @Result(property = "person", column = "person", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "course", column = "course", one = @One(select = "kitra.employeetraining.common.dao.CourseDao.getById")),
            @Result(property = "apprisement", column = "apprisement", one = @One(select = "kitra.employeetraining.common.dao.ApprisementCodeDao.getById")),
            @Result(property = "examDate", column = "exam_date")
    })
    TrainingPlan get(@Param("personId") Integer personId, @Param("courseId") Integer courseId);

    @Select("SELECT * FROM training_plan, person, course WHERE training_plan.person = person.id AND training_plan.course = course.id AND (person.name LIKE CONCAT('%', #{search}, '%') OR course.name LIKE CONCAT('%', #{search}, '%')) ORDER BY person")
    @Results({
            @Result(property = "person", column = "person", one = @One(select = "kitra.employeetraining.common.dao.PersonDao.getById")),
            @Result(property = "course", column = "course", one = @One(select = "kitra.employeetraining.common.dao.CourseDao.getById")),
            @Result(property = "apprisement", column = "apprisement", one = @One(select = "kitra.employeetraining.common.dao.ApprisementCodeDao.getById")),
            @Result(property = "examDate", column = "exam_date")
    })
    List<TrainingPlan> searchByName(String search);

    @Update("UPDATE training_plan SET person = #{person}, course = #{course}, score = #{score}, apprisement = #{apprisement}, exam_date = #{examDate} WHERE id = #{id}")
    int update(@Param("id") Integer id, @Param("person") Integer person, @Param("course") Integer course, @Param("score") Integer score, @Param("apprisement") Integer apprisement, @Param("examDate") String examDate);

    @Insert("INSERT INTO training_plan VALUES(0, #{person}, #{course}, #{score}, #{apprisement}, #{examDate})")
    int insert(@Param("person") Integer person, @Param("course") Integer course, @Param("score") Integer score, @Param("apprisement") Integer apprisement, @Param("examDate") String examDate);

    @Delete("DELETE FROM training_plan WHERE id = #{id}")
    int delete(Integer id);
}
