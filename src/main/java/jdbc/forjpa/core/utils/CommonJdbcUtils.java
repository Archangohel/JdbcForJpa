package jdbc.forjpa.core.utils;

import jdbc.forjpa.core.metadata.JpaEntityMetadataFactory;
import jdbc.forjpa.core.metadata.JpaEntityMetadataHelper;
import jdbc.forjpa.core.metadata.JpaEntityMetadataInfo;
import jdbc.forjpa.core.sql.SqlAndParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common features that we need for jdbc goes here.
 *
 * @author Archan
 */
@Component
public class CommonJdbcUtils {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JpaEntityMetadataFactory jpaEntityMetadataFactory;

    @Autowired
    private JpaEntityMetadataHelper jpaEntityMetadataHelper;

    public JpaEntityMetadataFactory getJpaEntityMetadataFactory() {
        return jpaEntityMetadataFactory;
    }

    public void setJpaEntityMetadataFactory(JpaEntityMetadataFactory jpaEntityMetadataFactory) {
        this.jpaEntityMetadataFactory = jpaEntityMetadataFactory;
    }

    public JpaEntityMetadataHelper getJpaEntityMetadataHelper() {
        return jpaEntityMetadataHelper;
    }

    public void setJpaEntityMetadataHelper(JpaEntityMetadataHelper jpaEntityMetadataHelper) {
        this.jpaEntityMetadataHelper = jpaEntityMetadataHelper;
    }

    public <T> void setLongIdForEntity(T entity, Long newId) {
        JpaEntityMetadataInfo<T> entityMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());
        for (String idField : entityMetadataInfo.getIdentityFields()) {
            Field field = entityMetadataInfo.getFieldMap().get(idField);
            if (field.getType().isAssignableFrom(Long.class)) {
                try {
                    Method method = entity.getClass().getDeclaredMethod("set" + StringUtils.capitalize(idField),
                            Long.class);
                    method.invoke(entity, newId);
                    break;
                } catch (NoSuchMethodException e) {
                    logger.error(
                            "No setter method found while setting identity field for entity {} and field {} => type {} , trying to set value {}",
                            entity, idField, field.getType(), newId, e);
                } catch (SecurityException e) {
                    logger.error(
                            "No setter method found while setting identity field for entity {} and field {} => type {} , trying to set value {}",
                            entity, idField, field.getType(), newId, e);
                } catch (Exception e) {
                    logger.error(
                            "Error occured while setting identity field for entity {} and field {} => type {} , trying to set value {}",
                            entity, idField, field.getType(), newId, e);
                }
            } else {
                logger.error("Invalid id type while setting identity field for entity {} and field {} => type {}",
                        entity, idField, field.getType());
            }
        }
    }

    public <T> Object[] generateJdbcArgsForEntity(T entity, SqlAndParams sqlAndParams) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());

        List<String> strParams = sqlAndParams.getParams();
        Object[] params = new Object[strParams.size()];
        for (int index = 0; index < strParams.size(); index++) {
            String fieldName = metadataInfo.getColumnsToFieldMap().get(strParams.get(index));
            if (fieldName == null) {
                // could be embedded field.
                Object embeddedEntity = getEmbeddedEntityForField(entity, strParams.get(index));
                if (embeddedEntity != null) {
                    JpaEntityMetadataInfo<T> embeddedEntityMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(embeddedEntity.getClass());
                    Field field = null;
                    try {
                        String embeddedFieldName = embeddedEntityMetadataInfo.getColumnsToFieldMap().get(strParams.get(index));
                        field = jpaEntityMetadataHelper.getFieldObjectByFieldName(embeddedEntity.getClass(), embeddedFieldName);// embeddedEntity.getClass().getDeclaredField(embeddedFieldName);
                        field.setAccessible(true);
                        params[index] = field.get(embeddedEntity);
                    } catch (Exception e1) {
                        logger.error("Unable to get the value for embedded entity {} and field {}", embeddedEntity,
                                strParams.get(index), e1);
                    }
                }
            } else {
                try {
                    Field field = jpaEntityMetadataHelper.getFieldObjectByFieldName(entity.getClass(), fieldName);// entity.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    params[index] = field.get(entity);
                } catch (IllegalArgumentException e) {
                    logger.error("Unable to get the value for entity {} and field {}", entity, strParams.get(index), e);
                } catch (IllegalAccessException e) {
                    logger.error("Unable to get the value for entity {} and field {}", entity, strParams.get(index), e);
                } catch (SecurityException e) {
                    logger.error("Unable to get the value for entity {} and field {}", entity, strParams.get(index), e);
                }
            }

        }
        return params;
    }

    private <T> Object getEmbeddedEntityForField(T entity, String columnName) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());
        for (Map.Entry<String, Class<?>> embeddedFieldEntry : metadataInfo.getEmbeddedFieldsMap().entrySet()) {
            JpaEntityMetadataInfo<T> embeddedMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(embeddedFieldEntry.getValue());
            if (embeddedMetadataInfo.getAllColumns().contains(columnName)) {
                try {
                    Field field = jpaEntityMetadataHelper.getFieldObjectByFieldName(entity.getClass(), embeddedFieldEntry.getKey());// entity.getClass().getDeclaredField(embeddedFieldEntry.getKey());
                    field.setAccessible(true);
                    return field.get(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public <T> Object fetchIdValue(T entity) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());
        Set<String> idFields = metadataInfo.getIdentityFields();
        if (idFields.size() > 0) {
            String idFieldName = idFields.iterator().next();
            Field fieldObject = metadataInfo.getFieldMap().get(idFieldName);
            fieldObject.setAccessible(true);
            try {
                return fieldObject.get(entity);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get the value for entity {} and field {}", entity, fieldObject, e);
            } catch (IllegalAccessException e) {
                logger.error("Unable to get the value for entity {} and field {}", entity, fieldObject, e);
            }
        }
        return null;
    }

    public <T1, T2> String getIdColumnForEntity(Class<T1> clazz, Class<T2> idType) {
        JpaEntityMetadataInfo<T1> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        for (String fieldName : metadataInfo.getIdentityFields()) {
            Field field = metadataInfo.getFieldMap().get(fieldName);
            if (field.getType().isAssignableFrom(idType)) {
                return metadataInfo.getFieldsToColumnMap().get(fieldName);
            }
        }
        return null;
    }

    public <T> void prePersist(T entity) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());
        Method method = metadataInfo.getSpecialMethods().get(PrePersist.class);
        if (method != null) {
            try {
                method.invoke(entity);
            } catch (Exception e) {
                logger.error("Error executing the prePersist of entity {}. ", entity, e);
                throw new RuntimeException(e);
            }
        }
    }

    public <T> void preUpdate(T entity) {
        JpaEntityMetadataInfo<T> metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entity.getClass());
        Method method = metadataInfo.getSpecialMethods().get(PreUpdate.class);
        if (method != null) {
            try {
                method.invoke(entity);
            } catch (Exception e) {
                logger.error("Error executing the preUpdate of entity {}. ", entity, e);
                throw new RuntimeException(e);
            }
        }
    }
}
