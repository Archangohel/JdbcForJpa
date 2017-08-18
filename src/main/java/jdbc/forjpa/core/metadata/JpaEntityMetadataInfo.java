package jdbc.forjpa.core.metadata;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to keep the entity metadata.
 *
 * @author Archan.
 */
public class JpaEntityMetadataInfo<T> {
    private Class<T> clazz;
    private String tableName;
    private Set<String> identityFields; // @Id annotation
    private Set<String> transientFields; // @Transient annotation
    private Map<String, Class<?>> embeddedFields; // @Embedded annotation.
    private Set<String> allFields;
    private Map<String, String> fieldsToColumnMap;
    private Map<String, String> columnToFieldsMap;
    private Map<String, Field> fieldMap;
    private Map<Class<?>, Method> specialMethods;

    private Map<String, JpaAssociationDescriptor<?>> fieldsAssociationMap;

    public JpaEntityMetadataInfo(Class<T> clazz) {
        initializeByClass(clazz);
    }

    private void initializeByClass(Class<T> clazz) {
        this.clazz = clazz;
        this.identityFields = JpaEntityMetadataUtils.getFieldsForEntityByAnnotation(clazz, Id.class);
        this.transientFields = JpaEntityMetadataUtils.getFieldsForEntityByAnnotation(clazz, Transient.class);
        this.embeddedFields = JpaEntityMetadataUtils.getFieldsMapForEntityByAnnotation(clazz, Embedded.class);
        this.allFields = JpaEntityMetadataUtils.getFieldsForEntity(clazz);
        this.fieldsToColumnMap = JpaEntityMetadataUtils.getFieldsToColumnMap(clazz);
        this.columnToFieldsMap = JpaEntityMetadataUtils.getColumnsToFieldMap(clazz);
        this.tableName = JpaEntityMetadataUtils.getTableName(clazz);
        this.fieldsAssociationMap = JpaEntityMetadataUtils.getAssociationMap(clazz);
        this.fieldMap = JpaEntityMetadataUtils.getFieldMapForEntity(clazz);
        this.specialMethods = JpaEntityMetadataUtils.getSpecialMethods(clazz);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public Set<String> getIdentityFields() {
        return identityFields;
    }

    public Set<String> getIdentityColumns() {
        Set<String> idColumns = new HashSet<>();
        identityFields.stream().forEach(r -> {
            idColumns.add(fieldsToColumnMap.get(r));
        });
        return idColumns;
    }

    public Set<String> getTransientFields() {
        return transientFields;
    }

    public Set<String> getAllFields() {
        return allFields;
    }

    public Set<String> getAllColumns() {
        Set<String> set = new HashSet<>();
        set.addAll(fieldsToColumnMap.values());
        return set;
    }

    public Map<String, String> getFieldsToColumnMap() {
        return fieldsToColumnMap;
    }

    public Map<String, String> getColumnsToFieldMap() {
        return columnToFieldsMap;
    }

    public Map<String, JpaAssociationDescriptor<?>> getFieldsAssociationMap() {
        return fieldsAssociationMap;
    }

    public Set<String> getEmbeddedFields() {
        return embeddedFields.keySet();
    }

    public Map<String, Class<?>> getEmbeddedFieldsMap() {
        return embeddedFields;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public Map<Class<?>, Method> getSpecialMethods() {
        return specialMethods;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JpaEntityPropertyDescriptor{");
        sb.append("clazz=").append(clazz);
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", identityFields=").append(identityFields);
        sb.append(", transientFields=").append(transientFields);
        sb.append(", embeddedFields=").append(embeddedFields);
        sb.append(", allFields=").append(allFields);
        sb.append(", fieldsToColumnMap=").append(fieldsToColumnMap);
        sb.append(", fieldsAssociationMap=").append(fieldsAssociationMap);
        sb.append('}');
        return sb.toString();
    }
}


