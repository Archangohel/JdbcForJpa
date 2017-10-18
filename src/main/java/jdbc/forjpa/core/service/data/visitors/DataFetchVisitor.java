package jdbc.forjpa.core.service.data.visitors;

import jdbc.forjpa.core.dao.BaseDao;
import jdbc.forjpa.core.dao.DaoFactory;
import jdbc.forjpa.core.metadata.JpaAssociationDescriptor;
import jdbc.forjpa.core.metadata.JpaEntityMetadataFactory;
import jdbc.forjpa.core.service.data.DataNode;
import jdbc.forjpa.core.utils.CommonJdbcUtils;
import jdbc.forjpa.util.ds.tree.AbstractTreeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Fetch data using IN / EQUALS query (for SQL Dialects) and set the results on the
 * {@link DataNode}
 *
 * @auther Archan on 16/10/17.
 */
@Component
public class DataFetchVisitor<T extends DataNode<T>> extends AbstractTreeVisitor<T> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DaoFactory daoFactory;

    @Autowired
    private JpaEntityMetadataFactory jpaEntityMetadataFactory;

    @Autowired
    private CommonJdbcUtils commonJdbcUtils;

    @Override
    public void beforeVisit(T element) {
        // populate the filters for children
        if (element.getIdMap() != null && element.getIdMap().isEmpty()
                && element.getParentNode() != null && ((DataNode) element.getParentNode()).getResult() != null) {
            DataNode dataNode = element;
            JpaAssociationDescriptor<?> associationDescriptor = dataNode.getAssociationDescriptor();
            if (JpaAssociationDescriptor.AssociationStrategy.MAPPED_BY.equals(associationDescriptor.getStrategy())) {
                Object parentObject = dataNode.getParentNode().getContent();
                List<Object> ids = new ArrayList<>();
                if (Collection.class.isAssignableFrom(parentObject.getClass())) {
                    ids.addAll(getIdsForCollection((Collection) parentObject));
                } else {
                    ids.add(commonJdbcUtils.fetchIdValue(parentObject));
                }
                String childField = dataNode.getAssociationDescriptor().getLinkedField();
                String childColumnName = jpaEntityMetadataFactory.getEntityMetadata(dataNode.getClazz()).getFieldsToColumnMap().get(childField).toString();
                logger.info("Setting child ids {} for child column {}", ids, childColumnName);
                //TODO: add support for list
                dataNode.getIdMap().put(childColumnName, ids.get(0));
            } else {
                //not yet supported
                logger.info("Association Strategy {} not yet supported", associationDescriptor.getStrategy());
            }
        }
    }

    private Collection<Object> getIdsForCollection(Collection objects) {
        List<Object> ids = new ArrayList<>();
        for (Object object : objects) {
            ids.add(commonJdbcUtils.fetchIdValue(object));
        }
        return ids;
    }

    @Override
    public void visit(T element) {
        BaseDao<?> dao = daoFactory.getDaoInstance(element.getClazz());
        List<?> result = dao.find(element.getIdMap());
        if (result != null) {
            element.setResult(result);
        }
    }

    @Override
    public void afterVisit(T element) {
        if (element.getParentNode() == null) {
            Collection<?> collection = Collection.class.cast(element.getContent());
            if (collection.size() > 1) {
                throw new RuntimeException("Not supported multiple parent");
            } else {
                element.setResult(collection.iterator().next());
            }
        }
    }
}
