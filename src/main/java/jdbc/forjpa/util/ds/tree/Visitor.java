package jdbc.forjpa.util.ds.tree;

/**
 * @auther Archan on 14/10/17.
 */
public interface Visitor<T> {

    void beforeVisit(T element);

    void visit(T element);

    void afterVisit(T element);

    Tree.TraverseStrategy getDefaultTraverseStrategy();
}
