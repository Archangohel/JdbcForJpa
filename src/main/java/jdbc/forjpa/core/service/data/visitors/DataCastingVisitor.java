package jdbc.forjpa.core.service.data.visitors;

import jdbc.forjpa.core.metadata.JpaAssociationDescriptor;
import jdbc.forjpa.core.service.data.DataNode;
import jdbc.forjpa.util.ds.tree.AbstractTreeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther Archan on 17/10/17.
 */
@Component
public class DataCastingVisitor<T extends DataNode<T>> extends AbstractTreeVisitor<T> {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void visit(T element) {
        /**
         * TODO:
         * 1) get parent
         * 2) find the type of the field
         * 3) Cast/Convert to the field type
         */
        if (element.getParentNode() != null) {
            JpaAssociationDescriptor<?> descriptor = element.getAssociationDescriptor();

            //will look like type<genericType>
            Class<?> genericType = descriptor.getReturnGenericType();
            Class<?> type = descriptor.getReturnType();

            if (Collection.class.isAssignableFrom(type)) {
                Collection object = Collection.class.cast(getObjectForCollectionType(type));
                Collection collectionResult = Collection.class.cast(element.getResult());
                object.addAll(collectionResult);
                element.setResult(object);
            } else if (Map.class.isAssignableFrom(type)) {
                logger.info("Not supported {} return types.", Map.class);
            } else if (type.isAssignableFrom(element.getClass())) {
                // do nothing.
            }
        }
    }

    private Object getObjectForCollectionType(Class<?> type) {
        if (Set.class.isAssignableFrom(type)) {
            return new HashSet<>();
        } else {
            return new ArrayList<>();
        }
    }
}
