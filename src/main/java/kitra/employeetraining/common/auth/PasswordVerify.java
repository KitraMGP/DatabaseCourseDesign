package kitra.employeetraining.common.auth;

import kitra.employeetraining.common.dao.PersonDao;
import kitra.employeetraining.common.datamodel.Authority;
import kitra.employeetraining.common.datamodel.Person;
import kitra.employeetraining.common.util.EntityUtils;
import kitra.employeetraining.common.util.MyBatisUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;

public class PasswordVerify {
    public static boolean verifyPassword(int userId, char[] password) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            String hash = mapper.getPasswordHash(userId);
            if(hash == null) return false;
            if(hash.isEmpty()) return true;
            return verifyHash(new String(password), hash);
        }
    }

    public static boolean verifyPassword(String username, char[] password) {
        if(!hasAccount(username)) return false;
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            int userId = mapper.getByUserName(username).getId();
            String hash = mapper.getPasswordHash(userId);
            if(hash == null) return false;
            if(hash.isEmpty()) return true;
            return verifyHash(new String(password), hash);
        }
    }

    public static boolean hasPassword(int userId) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            String hash = mapper.getPasswordHash(userId);
            return !hash.isEmpty();
        }
    }

    public static boolean verifyHash(String password, String hash) {
        return hash(password).equals(hash);
    }

    public static String hash(String data) {
        return DigestUtils.sha512Hex(data);
    }

    public static void setPassword(int userId, String password) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            mapper.setPassword(userId, hash(password));
            session.commit();
        }
    }

    public static Authority getAuthority(int userId) {
        Person person = (Person) EntityUtils.getEntityById(Person.class, userId);
        if(person == null) return null;
        return person.getAuthority();
    }

    public static boolean hasAccount(String username) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            PersonDao mapper = session.getMapper(PersonDao.class);
            Person person = mapper.getByUserName(username);
            return !(person == null);
        }
    }
}
