package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.dao.TrainingPlanDao;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = TrainingPlanDao.class)
@Alias("training_plan")
public class TrainingPlan implements Entity {
    @KeyColumn
    @ImportantColumn
    @ColumnName("编号")
    private int id;
    @ImportantColumn
    @ColumnName("员工")
    private Person person;
    @ImportantColumn
    @ColumnName("课程")
    private Course course;
    @ImportantColumn
    @ColumnName("成绩")
    private int score;
    @ImportantColumn
    @ColumnName("评价")
    private ApprisementCode apprisement;
    @ImportantColumn
    @ColumnName("考核日期")
    private String examDate;

    public TrainingPlan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ApprisementCode getApprisement() {
        return apprisement;
    }

    public void setApprisement(ApprisementCode apprisement) {
        this.apprisement = apprisement;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }
}
