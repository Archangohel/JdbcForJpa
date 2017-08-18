package jdbc.forjpa.core.metadata;

/**
 * Created by archangohel on 14/08/17.
 */
class JpaAssociationDescriptor<C> {
    enum AssociationStrategy {DEFAULT, MAPPED_BY}

    ;

    private Class<?> type; // onetoone , onetomany etc
    private Class<?> returnType;
    private Class<?> returnGenericType;
    private AssociationStrategy strategy;
    private String associationTable;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AssociationDescriptor{");
        sb.append("type=").append(type);
        sb.append(", returnType=").append(returnType);
        sb.append(", returnGenericType=").append(returnGenericType);
        sb.append(", strategy=").append(strategy);
        sb.append(", associationTable='").append(associationTable).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
