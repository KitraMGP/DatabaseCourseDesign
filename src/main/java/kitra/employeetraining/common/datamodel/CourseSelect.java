package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.EntityClass;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = CourseSelect.class)
@Alias("course_select")
public class CourseSelect implements Entity {
    private Course course;
    private Person person;

    public CourseSelect() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
