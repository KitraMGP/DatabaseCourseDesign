package kitra.employeetraining.common.util;

import kitra.employeetraining.common.annotation.ColumnName;
import kitra.employeetraining.common.annotation.EntityClass;
import kitra.employeetraining.common.annotation.ImportantColumn;
import kitra.employeetraining.common.annotation.KeyColumn;
import kitra.employeetraining.common.datamodel.Entity;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class EntityUtils {
    // 对两个依赖反射的方法的结果/中间结果进行缓存，减小性能损失
    private static final Map<Class<? extends Entity>, Vector<String>> entityColumnNamesMap = new HashMap<>();
    private static final Map<Class<? extends Entity>, Field[]> entityFieldsMap = new HashMap<>();
    private static final Map<Class<? extends Entity>, boolean[]> foreignKeyMap = new HashMap<>();
    private static final Map<Class<? extends Entity>, boolean[]> importantColumnMap = new HashMap<>();
    private static final Map<Class<? extends Entity>, Field> keyFieldMap = new HashMap<>();

    public static Vector<String> getStringVectorFromEntity(Entity o) {
        Class<? extends Entity> entityClass = o.getClass();
        Field[] fields = getFields(entityClass);
        Vector<String> stringVector = new Vector<>(fields.length);
        try {
            for (Field field : fields) {
                Object obj = field.get(o);
                if (obj == null) {
                    stringVector.add("");
                } else {
                    stringVector.add(obj.toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return stringVector;
    }

    public static Vector<String> filteredGetStringVectorFromEntity(Entity o, boolean[] importantColumns) {
        Vector<String> allColumns = getStringVectorFromEntity(o);
        Vector<String> filteredColumns = new Vector<>();
        for (int i = 0; i < allColumns.size(); i++) {
            if (importantColumns[i]) filteredColumns.add(allColumns.get(i));
        }
        return filteredColumns;
    }

    public static Vector<String> getColumnNames(Class<? extends Entity> entityClass) {
        if (entityColumnNamesMap.containsKey(entityClass)) {
            return new Vector<>(entityColumnNamesMap.get(entityClass));
        }
        Vector<String> columnNames = new Vector<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            ColumnName columnNameAnnotation = field.getAnnotation(ColumnName.class);
            if (columnNameAnnotation != null) columnNames.add(columnNameAnnotation.value());
        }
        entityColumnNamesMap.put(entityClass, columnNames);
        return new Vector<>(columnNames);
    }

    public static Vector<String> filteredGetColumnNames(Class<? extends Entity> entityClass, boolean[] importantColumns) {
        Vector<String> allColumns = getColumnNames(entityClass);
        Vector<String> filteredColumns = new Vector<>();
        for (int i = 0; i < allColumns.size(); i++) {
            if (importantColumns[i]) filteredColumns.add(allColumns.get(i));
        }
        return filteredColumns;
    }

    public static Field[] getFields(Class<? extends Entity> entityClass) {
        Field[] fields;
        // 若未缓存实体类的字段，则先存入缓存
        if (!entityFieldsMap.containsKey(entityClass)) {
            fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            entityFieldsMap.put(entityClass, fields);
        } else {
            fields = entityFieldsMap.get(entityClass);
        }
        return fields;
    }

    public static boolean[] getIsForeignKey(Class<? extends Entity> entityClass) {
        if (foreignKeyMap.containsKey(entityClass))
            return foreignKeyMap.get(entityClass).clone();
        Field[] fields = getFields(entityClass);
        boolean[] bools = new boolean[fields.length];
        for (int i = 0; i < fields.length; i++) {
            bools[i] = Entity.class.isAssignableFrom(fields[i].getType());
        }
        foreignKeyMap.put(entityClass, bools.clone());
        return bools;
    }

    public static boolean[] getIsImportantColumn(Class<? extends Entity> entityClass) {
        if (importantColumnMap.containsKey(entityClass))
            return importantColumnMap.get(entityClass).clone();
        Field[] fields = getFields(entityClass);
        boolean[] bools = new boolean[fields.length];
        for (int i = 0; i < fields.length; i++) {
            ImportantColumn annotation = fields[i].getAnnotation(ImportantColumn.class);
            bools[i] = annotation != null;
        }
        importantColumnMap.put(entityClass, bools.clone());
        return bools;
    }

    public static Field getKeyField(Class<? extends Entity> entityClass) {
        if (keyFieldMap.containsKey(entityClass))
            return keyFieldMap.get(entityClass);
        Field[] fields = getFields(entityClass);
        for (Field f : fields) {
            KeyColumn keyColumnAnnotation = f.getAnnotation(KeyColumn.class);
            if (keyColumnAnnotation != null) {
                keyFieldMap.put(entityClass, f);
                return f;
            }
        }
        return null;
    }

    /**
     * 将实体转换为可传入Dao进行操作的Object数组，字段的原始类型都将被保留，类型为实体的字段会被替换为它的Key
     *
     * @param entity 要转换的实体
     * @return 转换后的Object数组
     */
    @SuppressWarnings("unchecked")
    public static Object[] getEntityParams(Entity entity) {
        Class<? extends Entity> entityClass = entity.getClass();
        Field[] fields = getFields(entityClass);
        boolean[] isForeignKey = getIsForeignKey(entityClass);
        Object[] entityParams = new Object[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                if (isForeignKey[i]) {
                    Field fkField = fields[i];
                    Object fkEntity = fkField.get(entity);
                    // 外键实体的key字段
                    Field keyField = getKeyField((Class<? extends Entity>) fkField.getType());

                    Object fkEntityKey;
                    // fkEntity可能为null，此时获取fkEntity.key会导致空指针异常
                    // 需要手动处理
                    if (fkEntity != null) {
                        fkEntityKey = keyField.get(fkEntity);
                    } else {
                        // 默认的key，再代码表中代表默认选项，在其他实体表中代表无效数据
                        fkEntityKey = 0;
                    }

                    // 外键空值处理
                    /*try (SqlSession session = MyBatisUtils.getSqlSession()) {

                    }*/
                    entityParams[i] = fkEntityKey;
                    if (entityParams[i] == null) entityParams[i] = fields[i].getType().getConstructor().newInstance();
                } else {
                    entityParams[i] = fields[i].get(entity);
                    // 空值处理
                    if (entityParams[i] == null) entityParams[i] = fields[i].getType().getConstructor().newInstance();
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityParams;
    }

    public static int updateEntity(Class<? extends Entity> entityClass, Object[] entityParams) {
        try (SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());
            int result = (int) MethodUtils.invokeMethod(mapper, "update", entityParams);
            session.commit();
            return result;
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            // 是用户输入数据不合法
            return 0;
        }
    }

    public static int insertEntity(Class<? extends Entity> entityClass, Object[] entityParamsWithId) {
        try (SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());

            // 忽略entityParams中的第一项id
            Object[] entityParams = new Object[entityParamsWithId.length - 1];
            System.arraycopy(entityParamsWithId, 1, entityParams, 0, entityParams.length);

            int result = (int) MethodUtils.invokeMethod(mapper, "insert", entityParams);
            session.commit();
            return result;
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            // 是用户输入数据不合法导致
            // e.printStackTrace();
            return 0;
        }
    }

    public static int deleteEntity(Class<? extends Entity> entityClass, int id) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());
            int result = (int) mapper.getClass().getMethod("delete", Integer.class).invoke(mapper, id);
            session.commit();
            return result;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Entity getEntityById(Class<? extends Entity> entityClass, int id) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());
            Entity entity = (Entity) mapper.getClass().getMethod("getById", Integer.class).invoke(mapper, id);
            return entity;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Entity> getAll(Class<? extends Entity> entityClass) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());
            List<Entity> entityList = (List<Entity>) mapper.getClass().getMethod("getAll").invoke(mapper);
            return entityList;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return List.of();
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Entity> searchByName(Class<? extends Entity> entityClass, String search) {
        try(SqlSession session = MyBatisUtils.getSqlSession()) {
            Object mapper = session.getMapper(entityClass.getAnnotation(EntityClass.class).daoClass());
            List<Entity> entityList = (List<Entity>) mapper.getClass().getMethod("searchByName", String.class).invoke(mapper, search);
            return entityList;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return List.of();
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
