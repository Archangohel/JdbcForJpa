package jdbc.forjpa.core.service.data.visitors;

import jdbc.forjpa.core.service.data.DataNode;
import jdbc.forjpa.util.ds.tree.AbstractTreeVisitor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @auther Archan on 17/10/17.
 */
@Component
public class DataLinkingVisitor<T extends DataNode<T>> extends AbstractTreeVisitor<T> {
    @Override
    public void visit(T element) {
        /**
         * TODO:
         * 1) get the parent
         * 2) get the association field
         * 3) set the field using element.getResult()
         * NOTE: need to make sure that the result is having correct type.
         */
        if (element.getParentNode() != null) {
            Object object = element.getResult();
            String parentField = element.getParentFieldName();
            Object parentObject = ((DataNode) element.getParentNode()).getResult();
            try {
                Method method = parentObject.getClass().getDeclaredMethod("set" + StringUtils.capitalize(parentField)
                        , element.getAssociationDescriptor().getReturnType());
                method.invoke(parentObject, object);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
