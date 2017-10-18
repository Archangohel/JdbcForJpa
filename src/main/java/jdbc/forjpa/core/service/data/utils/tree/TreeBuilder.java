package jdbc.forjpa.core.service.data.utils.tree;

import jdbc.forjpa.util.ds.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.NotSupportedException;

/**
 * Abstract tree building class. It manages all the tree building stiff
 *
 * @auther Archan on 18/10/17.
 */

public abstract class TreeBuilder {

    @Autowired
    protected TreeBuilderFactory treeBuilderFactory;

    public abstract <T> Tree<T> buildTreeForEntity(Class<T> entityClass) throws NotSupportedException;

    public abstract <T> Tree<T> buildTreeForEntity(Class<T> entityClass, EntityAssociationDescriptor entityDescriptor) throws NotSupportedException;

    public TreeBuilderFactory getTreeBuilderFactory() {
        return treeBuilderFactory;
    }
}
