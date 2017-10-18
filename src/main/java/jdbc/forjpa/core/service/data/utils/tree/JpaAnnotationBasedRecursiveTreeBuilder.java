package jdbc.forjpa.core.service.data.utils.tree;

import jdbc.forjpa.core.service.data.TreeBuildingStrategy;
import jdbc.forjpa.util.ds.tree.Tree;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.NotSupportedException;

/**
 * Non recursive tree builder. It should consider the parent as root
 * and consider all the children recurrsively
 *
 * @auther Archan on 18/10/17.
 */

@Component
class JpaAnnotationBasedRecursiveTreeBuilder extends TreeBuilder {

    @PostConstruct
    public void init(){
        getTreeBuilderFactory().registerTreeBuilder(this, TreeBuildingStrategy.AUTO_ALL_ASSOCIATIONS_RECURSIVE);
    }

    @Override
    public <T> Tree<T> buildTreeForEntity(Class<T> entityClass) {
        return null;
    }

    @Override
    public <T> Tree<T> buildTreeForEntity(Class<T> entityClass, EntityAssociationDescriptor entityDescriptor) throws NotSupportedException {
        throw new NotSupportedException("This building strategy doesnt support default");
    }
}
