package kitra.employeetraining;

import kitra.employeetraining.common.datamodel.*;
import kitra.employeetraining.manager.ui.RecordEditDialog;

public class TestMain {
    public static void main(String[] args) {
        testDialog(ApprisementCode.class);
        testDialog(Course.class);
        testDialog(CourseStateCode.class);
        testDialog(Department.class);
        testDialog(Person.class);
        testDialog(TrainingPlan.class);
    }

    private static void testDialog(Class<? extends Entity> entityClass) {
        new RecordEditDialog(null, entityClass, null);
    }
}
