package jdbc.forjpa.core.service.data;

import jdbc.forjpa.core.service.data.utils.tree.TreeBuilder;
import jdbc.forjpa.core.service.data.utils.tree.TreeBuilderFactory;
import jdbc.forjpa.core.service.data.visitors.DataCastingVisitor;
import jdbc.forjpa.core.service.data.visitors.DataFetchVisitor;
import jdbc.forjpa.core.service.data.visitors.DataLinkingVisitor;
import jdbc.forjpa.util.ds.tree.Tree;
import jdbc.forjpa.util.ds.tree.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Core service that supports the READ operation. It can load the JPA associations.
 *
 * @auther Archan on 17/10/17.
 */
@Component
public class FetchDataService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<Visitor> defaultVisitorSequence = new ArrayList<>();

    @Autowired
    private TreeBuilderFactory treeBuilderFactory;

    @Autowired
    private ApplicationContext applicationContext;
    private TreeBuildingStrategy defaultTreeBuildingStrategy = TreeBuildingStrategy.AUTO_ALL_ASSOCIATIONS;

    @PostConstruct
    public void init() {
        initialiseVisitorFlow();
    }

    private void initialiseVisitorFlow() {
        this.defaultVisitorSequence.add(applicationContext.getBean(DataFetchVisitor.class));
        this.defaultVisitorSequence.add(applicationContext.getBean(DataCastingVisitor.class));
        this.defaultVisitorSequence.add(applicationContext.getBean(DataLinkingVisitor.class));
    }

    public <T> Object fetchEntity(Class<T> entityClass, Map<String, Object> filter, TreeBuildingStrategy treeBuildingStrategy) {
        Tree<T> tree = buildTree(entityClass, treeBuildingStrategy);
        applyFilters(tree, filter);

        for (Visitor visitor : this.defaultVisitorSequence) {
            tree.traverse(visitor, null);
        }

        return ((DataNode) tree.getRoot()).getResult();

    }

    public <T> Object fetchEntity(Class<T> entityClass, Map<String, Object> filter) {
        return this.fetchEntity(entityClass, filter, defaultTreeBuildingStrategy);
    }

    private <T> Tree<T> buildTree(Class<T> entityClass, TreeBuildingStrategy strategy) {
        TreeBuilder treeBuilder = treeBuilderFactory.getTreeBuilderByStrategy(strategy);
        try {
            return treeBuilder.buildTreeForEntity(entityClass);
        } catch (NotSupportedException e) {
            throw new RuntimeException("Strategy " + TreeBuildingStrategy.AUTO_ALL_ASSOCIATIONS + " is not supported");
        }
    }

    private <T> void applyFilters(Tree<T> tree, Map<String, Object> filter) {
        if (tree.getRoot() instanceof DataNode) {
            ((DataNode) tree.getRoot()).setIdMap(filter);
        } else {
            logger.warn("Not able to set the filter");
            throw new IllegalStateException("Not able to set the filter");
        }
    }
}
