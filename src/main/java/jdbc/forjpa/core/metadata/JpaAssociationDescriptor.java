package jdbc.forjpa.core.metadata;

/**
 * Created by archangohel on 14/08/17.
 */
public class JpaAssociationDescriptor<C> {
    public enum AssociationStrategy {DEFAULT, MAPPED_BY}

    ;

    private Class<?> type; // onetoone , onetomany etc
    private Class<?> returnType;
    private Class<?> returnGenericType;
    private AssociationStrategy strategy;
    private String associationTable;
    private String linkedField;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public AssociationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(AssociationStrategy strategy) {
        this.strategy = strategy;
    }

    public String getAssociationTable() {
        return associationTable;
    }

    public void setAssociationTable(String associationTable) {
        this.associationTable = associationTable;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<?> getReturnGenericType() {
        return returnGenericType;
    }

    public void setReturnGenericType(Class<?> returnGenericType) {
        this.returnGenericType = returnGenericType;
    }

    public String getLinkedField() {
        return linkedField;
    }

    public void setLinkedField(String linkedField) {
        this.linkedField = linkedField;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JpaAssociationDescriptor{");
        sb.append("associationTable='").append(associationTable).append('\'');
        sb.append(", type=").append(type);
        sb.append(", returnType=").append(returnType);
        sb.append(", returnGenericType=").append(returnGenericType);
        sb.append(", strategy=").append(strategy);
        sb.append(", linkedField='").append(linkedField).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
