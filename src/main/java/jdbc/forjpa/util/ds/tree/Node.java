package jdbc.forjpa.util.ds.tree;

/**
 * Common node interface
 *
 * @auther Archan on 14/10/17.
 */
public interface Node<T> {
    T getContent();

    void setContent(T content);

    Node<T> getParentNode();

    Node<T> getChildNode();

    Node<T> getSiblingNode();

    void setParent(Node<T> parent);

    void append(TreeNode<T> nodeToAdd);
}
