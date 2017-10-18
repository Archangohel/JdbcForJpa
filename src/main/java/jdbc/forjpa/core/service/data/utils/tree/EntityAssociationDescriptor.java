package jdbc.forjpa.core.service.data.utils.tree;

import java.util.List;

/**
 * Class to represent an jpa entity and associations structure using field names.
 *
 * @auther Archan on 18/10/17.
 */
public class EntityAssociationDescriptor {

    private String fieldName;
    List<EntityAssociationDescriptor> children;

    public List<EntityAssociationDescriptor> getChildren() {
        return children;
    }

    public void setChildren(List<EntityAssociationDescriptor> children) {
        this.children = children;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
