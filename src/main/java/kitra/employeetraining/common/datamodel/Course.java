package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.dao.CourseDao;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = CourseDao.class)
@Alias("course")
public class Course implements Entity {
    @KeyColumn
    @ImportantColumn
    @ColumnName("课程编号")
    private int id;
    @ImportantColumn
    @ColumnName("课程名")
    private String name;
    @ImportantColumn
    @ColumnName("任课教师")
    private Person teacher;  /* 外键 -> Person.id */
    @ColumnName("课程简介")
    private String intro;
    @ColumnName("所用教材")
    private String book;
    @ImportantColumn
    @ColumnName("上课地点")
    private String classroom;
    @ImportantColumn
    @ColumnName("课程上限人数")
    private int number = 30;
    @ImportantColumn
    @ColumnName("开课时间")
    private String classtime;
    @ImportantColumn
    @ColumnName("课程状态")
    private CourseStateCode state;  /* 外键 -> CourseStateCode.code */

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getClasstime() {
        return classtime;
    }

    public void setClasstime(String classtime) {
        this.classtime = classtime;
    }

    public CourseStateCode getState() {
        return state;
    }

    public void setState(CourseStateCode state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
