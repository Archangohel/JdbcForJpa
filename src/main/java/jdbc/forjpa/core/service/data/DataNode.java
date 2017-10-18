package jdbc.forjpa.core.service.data;


import jdbc.forjpa.core.metadata.JpaAssociationDescriptor;
import jdbc.forjpa.util.ds.tree.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder for the jpa entity data.
 *
 * @auther Archan on 16/10/17.
 */
public class DataNode<T> extends TreeNode<T> {

    /**
     * Target class for the data fetched by the node
     */
    private Class<?> clazz;
    /**
     * Association information from the parent entity for the node
     */
    private JpaAssociationDescriptor<?> associationDescriptor;

    /**
     * id column vs id value. In case of multiple values it will keep list of values.
     * Typically to be dynamically populated while traversing the tree using parent node.
     */
    private Map<String, Object> idMap;
    /**
     * The association field name
     */
    private String parentFieldName;


    public DataNode(T data) {
        super(data);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }


    public Map<String, Object> getIdMap() {
        if (idMap == null) {
            idMap = new HashMap<>();
        }
        return idMap;
    }

    public void setIdMap(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    public Object getResult() {
        return getContent();
    }

    public void setResult(Object result) {
        this.setContent((T) result);
    }

    public JpaAssociationDescriptor<?> getAssociationDescriptor() {
        return associationDescriptor;
    }

    public void setAssociationDescriptor(JpaAssociationDescriptor<?> associationDescriptor) {
        this.associationDescriptor = associationDescriptor;
    }

    public String getParentFieldName() {
        return parentFieldName;
    }

    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
    }
}
