package jdbc.forjpa.core.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by archangohel on 14/08/17.
 */

@Component
public class JpaEntityMetadataHelper {

    @Autowired
    private JpaEntityMetadataFactory jpaEntityMetadataFactory;

    public JpaEntityMetadataFactory getJpaEntityMetadataFactory() {
        return jpaEntityMetadataFactory;
    }

    public void setJpaEntityMetadataFactory(JpaEntityMetadataFactory jpaEntityMetadataFactory) {
        this.jpaEntityMetadataFactory = jpaEntityMetadataFactory;
    }

    public <T> List<String> getAllColumnsForEntity(JpaEntityMetadataInfo<T> jpaEntityMetadataInfo) {
        List<String> columns = new ArrayList<String>();
        Set<String> associationFields = jpaEntityMetadataInfo.getFieldsAssociationMap().keySet();
        for (String fieldName : jpaEntityMetadataInfo.getAllFields()) {
            if (jpaEntityMetadataInfo.getEmbeddedFields().contains(fieldName)) {
                try {
                    Field field = jpaEntityMetadataInfo.getClazz().getDeclaredField(fieldName);
                    Class<?> embeddedType = field.getType();
                    JpaEntityMetadataInfo<?> embeddedTypeMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(embeddedType);
                    columns.addAll(getAllColumnsForEntity(embeddedTypeMetadataInfo));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } else if (associationFields.contains(fieldName)) {
                //ignore these fields.
                continue;
            }else if (jpaEntityMetadataInfo.getTransientFields().contains(fieldName)) {
                //ignore these fields.
                continue;
            }else {
                columns.add(jpaEntityMetadataInfo.getFieldsToColumnMap().get(fieldName));
            }
        }
        return columns;
    }

    public <T> Field getFieldObjectByFieldName(Class<T> clazz, String fieldName){
        JpaEntityMetadataInfo<T> jpaEntityMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        return jpaEntityMetadataInfo.getFieldMap().get(fieldName);
    }

    public <T> Field getFieldObjectByColumn(Class<T> clazz, String columnName){
        JpaEntityMetadataInfo<T> jpaEntityMetadataInfo = jpaEntityMetadataFactory.getEntityMetadata(clazz);
        String fieldName = jpaEntityMetadataInfo.getColumnsToFieldMap().get(columnName);
        return jpaEntityMetadataInfo.getFieldMap().get(fieldName);
    }

}
