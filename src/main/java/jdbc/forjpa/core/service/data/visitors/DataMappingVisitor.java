package jdbc.forjpa.core.service.data.visitors;

import jdbc.forjpa.core.service.data.DataNode;
import jdbc.forjpa.util.ds.tree.AbstractTreeVisitor;

/**
 * Map the child records to parent records.
 * @auther Archan on 18/10/17.
 */
public class DataMappingVisitor<T extends DataNode<T>> extends AbstractTreeVisitor<T> {
    @Override
    public void visit(T element) {

    }
}
