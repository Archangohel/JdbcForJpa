package jdbc.forjpa.util.ds.tree;

import java.util.List;

/**
 * Common node interface
 *
 * @auther Archan on 14/10/17.
 */
public interface Node<T> {
    T getContent();

    void setContent(T content);

    Node<T> getParentNode();

    List<? extends Node<T>> getChildren();

    void setParent(Node<T> parent);

    void append(TreeNode<T> nodeToAdd);
}
