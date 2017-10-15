package jdbc.forjpa.util.ds.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Archan on 14/10/17.
 */

class TreeNode<T> implements Node<T> {

    private TreeNode<T> parent;
    private List<TreeNode<T>> children = new LinkedList<>();
    private Iterator<? extends TreeNode<T>> iterator;
    T data;

    public TreeNode(T data) {
        this.data = data;
    }

    @Override
    public T getContent() {
        return data;
    }

    @Override
    public void setContent(T content) {
        this.data = content;
    }

    @Override
    public TreeNode<T> getParentNode() {
        return parent;
    }

    @Override
    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public TreeNode<T> nextChild() {
        if (iterator == null) {
            iterator = getChildren().iterator();
        }
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            iterator = null;
            return null;
        }
    }

    public TreeNode<T> nextChild(boolean startFromBeginning) {
        iterator = null;
        return nextChild();
    }

    public boolean hasNextChild() {
        if (iterator == null) {
            iterator = getChildren().iterator();
        }
        boolean flag = iterator.hasNext();
        if (!flag) {
            iterator = null;
        }
        return flag;
    }

    public void setParent(Node<T> parent) {
        this.parent = (TreeNode<T>) parent;
    }

    @Override
    public void append(TreeNode<T> nodeToAdd) {
        addChildNode(nodeToAdd);
    }

    private void addChildNode(Node<T> child) {
        this.children.add((TreeNode<T>) child);
        child.setParent(this);
    }

    public void cleanup() {
        iterator = null;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                " data=" + data +
                ", children=" + children +
                '}';
    }

    public String printCurrent() {
        return "TreeNode{ data=" + data + " , parent = " + (parent != null ? parent.printCurrent() : null) + " }";
    }
}
