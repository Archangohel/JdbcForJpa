package jdbc.forjpa.core.service.data.utils.tree;

import jdbc.forjpa.core.metadata.JpaAssociationDescriptor;
import jdbc.forjpa.core.metadata.JpaEntityMetadataFactory;
import jdbc.forjpa.core.metadata.JpaEntityMetadataInfo;
import jdbc.forjpa.core.service.data.DataNode;
import jdbc.forjpa.core.service.data.TreeBuildingStrategy;
import jdbc.forjpa.util.ds.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.NotSupportedException;
import java.util.Map;

/**
 * Non recursive tree builder. It should consider the parent as root
 * and consider all the immediate children.
 *
 * @auther Archan on 18/10/17.
 */
@Component
class JpaAnnotationBasedTreeBuilder extends TreeBuilder {


    @Autowired
    private JpaEntityMetadataFactory jpaEntityMetadataFactory;

    @PostConstruct
    public void init() {
        getTreeBuilderFactory().registerTreeBuilder(this, TreeBuildingStrategy.AUTO_ALL_ASSOCIATIONS);
    }

    @Override
    public <T> Tree<T> buildTreeForEntity(Class<T> entityClass) {
        JpaEntityMetadataInfo metadataInfo = jpaEntityMetadataFactory.getEntityMetadata(entityClass);

        Tree<T> tree = new Tree<>();
        DataNode<T> root = new DataNode<>(null);
        root.setClazz(entityClass);
        tree.setRoot(root);

        Map<String, JpaAssociationDescriptor<?>> map = metadataInfo.getFieldsAssociationMap();

        for (Map.Entry<String, JpaAssociationDescriptor<?>> associationDescriptorEntry : map.entrySet()) {
            DataNode<T> node = new DataNode<>(null);
            node.setAssociationDescriptor(associationDescriptorEntry.getValue());
            node.setParentFieldName(associationDescriptorEntry.getKey());
            node.setClazz(associationDescriptorEntry.getValue().getReturnGenericType() == null ?
                    associationDescriptorEntry.getValue().getReturnType() : associationDescriptorEntry.getValue().getReturnGenericType());
            root.append(node);
        }

        return tree;
    }

    @Override
    public <T> Tree<T> buildTreeForEntity(Class<T> entityClass, EntityAssociationDescriptor entityDescriptor) throws NotSupportedException {
        throw new NotSupportedException("This building strategy doesnt support default");
    }
}
