package jdbc.forjpa.util.ds.tree;

/**
 * @auther Archan on 14/10/17.
 */
public abstract class AbstractVisitor<T> implements Visitor<T> {

    public void beforeVisit(T element) {

    }

    abstract public void visit(T element);

    public void afterVisit(T element) {

    }

}
