package kitra.employeetraining.common.datamodel;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.dao.PersonDao;
import org.apache.ibatis.type.Alias;

@EntityClass(daoClass = PersonDao.class)
@Alias("person")
public class Person implements Entity {
    @KeyColumn
    @ImportantColumn
    @ColumnName("员工号")
    private int id = -1;
    @ColumnName("登录用户名(必填)")
    private String username;
    @ColumnName("密码")
    private String password;
    @ColumnName("权限")
    private Authority authority;
    @ImportantColumn
    @ColumnName("姓名")
    private String name;
    @ImportantColumn
    @ColumnName("性别")
    private String sex;
    @ColumnName("出生日期")
    private String birthday;
    @ImportantColumn
    @ColumnName("部门")
    private Department department;
    @ImportantColumn
    @ColumnName("职务")
    private String job;
    @ColumnName("受教育程度")
    private String eduLevel;
    @ColumnName("专业技能")
    private String speciaty;
    @ColumnName("家庭住址")
    private String address;
    @ColumnName("联系电话")
    private String tel;
    @ColumnName("电子信箱")
    private String email;
    @ColumnName("当前状态")
    private PersonState state;   /* T-员工 F-非员工 */
    @ColumnName("备注")
    private String remark;

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(String eduLevel) {
        this.eduLevel = eduLevel;
    }

    public String getSpeciaty() {
        return speciaty;
    }

    public void setSpeciaty(String speciaty) {
        this.speciaty = speciaty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PersonState getState() {
        return state;
    }

    public void setState(PersonState state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
