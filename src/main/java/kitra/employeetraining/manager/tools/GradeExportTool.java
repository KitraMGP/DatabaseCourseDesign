package kitra.employeetraining.manager.tools;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import kitra.employeetraining.common.dao.TrainingPlanDao;
import kitra.employeetraining.common.datamodel.TrainingPlan;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class GradeExportTool {
    /**
     * 弹出文件保存窗口，让用户保存报表文件
     * @param parent 父窗口
     */
    public static void exportData(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showSaveDialog(parent);
        // 如果用户确认保存文件
        if(option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // 获取文件名
            String fileName = fileChooser.getName(file);
            // 如果文件名中不含有扩展名，则为其加上扩展名
            if(!fileName.contains(".xlsx")) file = new File(fileChooser.getCurrentDirectory(), fileName + ".xlsx");
            EasyExcel.write(file, GradeData.class)
                    .sheet("成绩")
                    .doWrite(getAllGradeData());
        }
    }

    private static List<GradeData> getAllGradeData() {
        List<GradeData> dataList = new LinkedList<>();
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            List<TrainingPlan> trainingPlans = session.getMapper(TrainingPlanDao.class).getAll();
            for(TrainingPlan plan : trainingPlans) {
                GradeData data = new GradeData();
                data.setId(plan.getId());
                data.setPerson(plan.getPerson().toString());
                data.setCourse(plan.getCourse().toString());
                data.setScore(plan.getScore());
                data.setApprisement(plan.getApprisement().toString());
                data.setExamDate(plan.getExamDate());
                dataList.add(data);
            }
        }
        return dataList;
    }

    private static class GradeData {
        @ExcelProperty("记录编号")
        private int id;
        @ExcelProperty("员工")
        private String person;
        @ExcelProperty("课程")
        private String course;
        @ExcelProperty("成绩")
        private int score;
        @ExcelProperty("评价")
        private String apprisement;
        @ExcelProperty("考核日期")
        private String examDate;

        public GradeData() {
        }

        public String getApprisement() {
            return apprisement;
        }

        public void setApprisement(String apprisement) {
            this.apprisement = apprisement;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getExamDate() {
            return examDate;
        }

        public void setExamDate(String examDate) {
            this.examDate = examDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
