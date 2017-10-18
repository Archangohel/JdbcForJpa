package jdbc.forjpa.core.metadata;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by archangohel on 14/08/17.
 */
@Component
class JpaEntityMetadataUtils {

    public static Set<String> getFieldsForEntityByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        Set<String> returnFields = new HashSet<String>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<String> fieldList = new ArrayList<String>();
            for (Field field : fields) {
                fieldList.add(field.getName());
            }
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                String fieldName = pd.getName();
                if (fieldList.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    if (field.getAnnotation(annotationClazz) != null) {
                        returnFields.add(fieldName);
                    } else if (pd.getReadMethod().getAnnotation(annotationClazz) != null) {
                        returnFields.add(pd.getName());
                    }
                }
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFields.addAll(getFieldsForEntityByAnnotation(superClasses, annotationClazz));
        }
        return returnFields;
    }

    public static Set<String> getFieldsForEntity(Class<?> clazz) {
        Set<String> returnFields = new HashSet<String>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                returnFields.add(field.getName());
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFields.addAll(getFieldsForEntity(superClasses));
        }
        return returnFields;
    }

    public static Map<String, Field> getFieldMapForEntity(Class<?> clazz) {
        Map<String, Field> returnFields = new HashMap<>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                returnFields.put(field.getName(), field);
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFields.putAll(getFieldMapForEntity(superClasses));
        }
        return returnFields;
    }

    public static Class<?> getSuperClass(Class<?> mainClazz) {
        Class superclass = mainClazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            return superclass;
        } else {
            return null;
        }
    }

    public static <T> Map<String, String> getFieldsToColumnMap(Class<T> clazz) {
        Map<String, String> returnFieldsMap = new HashMap<String, String>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<String> fieldList = new ArrayList<String>();
            for (Field field : fields) {
                fieldList.add(field.getName());
            }
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                String fieldName = pd.getName();
                if (fieldList.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    String columnName = fieldName;
                    if (field.getAnnotation(Column.class) != null) {
                        Column annotation = field.getAnnotation(Column.class);
                        columnName = annotation.name();
                    } else if (pd.getReadMethod().getAnnotation(Column.class) != null) {
                        Column annotation = pd.getReadMethod().getAnnotation(Column.class);
                        columnName = annotation.name();
                    }else if(field.getAnnotation(JoinColumn.class) != null) {
                        JoinColumn annotation = field.getAnnotation(JoinColumn.class);
                        columnName = annotation.name();
                    } else if (pd.getReadMethod().getAnnotation(JoinColumn.class) != null) {
                        JoinColumn annotation = pd.getReadMethod().getAnnotation(JoinColumn.class);
                        columnName = annotation.name();
                    }
                    returnFieldsMap.put(fieldName, columnName);
                }
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFieldsMap.putAll(getFieldsToColumnMap(superClasses));
        }
        return returnFieldsMap;
    }

    public static <T> String getTableName(Class<T> clazz) {
        Table entityTable = clazz.getAnnotation(Table.class);
        if (entityTable != null) {
            return entityTable.name();
        } else {
            return clazz.getSimpleName();
        }
    }

    public static <T> Map<String, JpaAssociationDescriptor<?>> getAssociationMap(Class<T> clazz) {
        Map<String, JpaAssociationDescriptor<?>> associationMap = new HashMap<String, JpaAssociationDescriptor<?>>();
        Set<String> oneToOneAssociations = getFieldsForEntityByAnnotation(clazz, OneToOne.class);
        Set<String> oneToManyAssociations = getFieldsForEntityByAnnotation(clazz, OneToMany.class);
        Set<String> manyToOneAssociations = getFieldsForEntityByAnnotation(clazz, ManyToOne.class);
        Set<String> manyToManyAssociations = getFieldsForEntityByAnnotation(clazz, ManyToMany.class);
        try {
            if (!oneToOneAssociations.isEmpty()) {
                for (String associationFieldName : oneToOneAssociations) {
                    Field field = clazz.getDeclaredField(associationFieldName);
                    JpaAssociationDescriptor<?> associationDescriptor = new JpaAssociationDescriptor<Object>();
                    associationDescriptor.setType(OneToOne.class);
                    OneToOne annotation = field.getDeclaredAnnotation(OneToOne.class);
                    String mappedBy = annotation.mappedBy();
                    if (StringUtils.isEmpty(mappedBy)) {
                        associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.DEFAULT);
                    } else {
                        associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.MAPPED_BY);
                    }
                    associationDescriptor.setAssociationTable(getTableName((Class<?>) field.getType()));
                    associationDescriptor.setReturnType(field.getType());
                    associationMap.put(associationFieldName, associationDescriptor);
                }
            }
            if (!oneToManyAssociations.isEmpty()) {
                for (String associationFieldName : oneToManyAssociations) {
                    Field field = clazz.getDeclaredField(associationFieldName);
                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    JpaAssociationDescriptor<?> associationDescriptor = new JpaAssociationDescriptor<Object>();
                    associationDescriptor.setType(OneToMany.class);
                    OneToMany annotation = field.getDeclaredAnnotation(OneToMany.class);
                    String mappedBy = annotation.mappedBy();
                    if (StringUtils.isEmpty(mappedBy)) {
                        associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.DEFAULT);
                    } else {
                        associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.MAPPED_BY);
                        associationDescriptor.setLinkedField(mappedBy);
                    }
                    associationDescriptor.setAssociationTable(getTableName((Class<?>) type));
                    associationDescriptor.setReturnGenericType((Class<?>) type);
                    associationDescriptor.setReturnType(field.getType());
                    associationMap.put(associationFieldName, associationDescriptor);

                }
            }
            if (!manyToOneAssociations.isEmpty()) {
                for (String associationFieldName : manyToOneAssociations) {
                    Field field = clazz.getDeclaredField(associationFieldName);
                    JpaAssociationDescriptor<?> associationDescriptor = new JpaAssociationDescriptor<Object>();
                    Type type = field.getType();
                    associationDescriptor.setType(ManyToOne.class);
                    associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.DEFAULT);
                    associationDescriptor.setAssociationTable(getTableName((Class<?>) type));
                    associationDescriptor.setReturnGenericType((Class<?>) type);
                    associationDescriptor.setReturnType(field.getType());
                    associationMap.put(associationFieldName, associationDescriptor);

                }
            }
            if (!manyToManyAssociations.isEmpty()) {
                for (String associationFieldName : manyToManyAssociations) {
                    Field field = clazz.getDeclaredField(associationFieldName);
                    JpaAssociationDescriptor<?> associationDescriptor = new JpaAssociationDescriptor<Object>();
                    Type type = field.getType();
                    associationDescriptor.setType(ManyToMany.class);
                    associationDescriptor.setStrategy(JpaAssociationDescriptor.AssociationStrategy.DEFAULT);
                    associationDescriptor.setAssociationTable(getTableName((Class<?>) type));
                    associationDescriptor.setReturnGenericType((Class<?>) type);
                    associationDescriptor.setReturnType(field.getType());
                    associationMap.put(associationFieldName, associationDescriptor);

                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return associationMap;
    }


    public static <T> Map<String, Class<?>> getFieldsMapForEntityByAnnotation(Class<T> clazz, Class<Embedded> annotationClazz) {
        Map<String, Class<?>> returnFields = new HashMap<String, Class<?>>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<String> fieldList = new ArrayList<String>();
            for (Field field : fields) {
                fieldList.add(field.getName());
            }
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                String fieldName = pd.getName();
                if (fieldList.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    if (field.getAnnotation(annotationClazz) != null) {
                        returnFields.put(fieldName, field.getType());
                    } else if (pd.getReadMethod().getAnnotation(annotationClazz) != null) {
                        returnFields.put(pd.getName(), field.getType());
                    }
                }
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFields.putAll(getFieldsMapForEntityByAnnotation(superClasses, annotationClazz));
        }
        return returnFields;
    }

    public static <T> Map<String, String> getColumnsToFieldMap(Class<T> clazz) {
        Map<String, String> returnFieldsMap = new HashMap<String, String>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<String> fieldList = new ArrayList<String>();
            for (Field field : fields) {
                fieldList.add(field.getName());
            }
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : props) {
                String fieldName = pd.getName();
                if (fieldList.contains(fieldName)) {
                    Field field = clazz.getDeclaredField(fieldName);
                    String columnName = fieldName;
                    if (field.getAnnotation(Column.class) != null) {
                        Column annotation = field.getAnnotation(Column.class);
                        columnName = annotation.name();
                    } else if (pd.getReadMethod().getAnnotation(Column.class) != null) {
                        Column annotation = pd.getReadMethod().getAnnotation(Column.class);
                        columnName = annotation.name();
                    }
                    returnFieldsMap.put(columnName, fieldName);
                }
            }
        } catch (Exception e) {
            //TODO: handle exception.
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            returnFieldsMap.putAll(getFieldsToColumnMap(superClasses));
        }
        return returnFieldsMap;
    }

    public static <T> Map<Class<?>, Method> getSpecialMethods(Class<T> clazz) {
        Map<Class<?>, Method> specialMethods = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(PrePersist.class) != null) {
                specialMethods.put(PrePersist.class, method);
            } else if (method.getAnnotation(PreUpdate.class) != null) {
                specialMethods.put(PreUpdate.class, method);
            }
        }
        Class<?> superClasses = getSuperClass(clazz);
        if (superClasses != null) {
            specialMethods.putAll(getSpecialMethods(superClasses));
        }
        return specialMethods;
    }
}