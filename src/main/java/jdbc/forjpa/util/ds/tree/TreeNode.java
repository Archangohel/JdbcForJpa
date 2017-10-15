package jdbc.forjpa.util.ds.tree;

/**
 * @auther Archan on 14/10/17.
 */

class TreeNode<T> implements Node<T> {

    private TreeNode<T> parent;
    private TreeNode<T> child;
    private TreeNode<T> sibling;
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
    public TreeNode<T> getChildNode() {
        return child;
    }

    @Override
    public TreeNode<T> getSiblingNode() {
        return sibling;
    }

    private void addChildNode(Node<T> child) {
        this.child = (TreeNode<T>) child;
        child.setParent(this);
    }

    private void addSiblingNode(Node<T> sibling) {
        this.sibling = (TreeNode<T>) sibling;
        sibling.setParent(this);
    }

    public void setParent(Node<T> parent) {
        this.parent = (TreeNode<T>) parent;
    }

    @Override
    public void append(TreeNode<T> nodeToAdd) {
        TreeNode<T> child = getChildNode();
        if (child == null) {
            this.addChildNode(nodeToAdd);
        } else {
            // append to last sibling
            TreeNode<T> lastSibling = this.getLastSibling();
            if (lastSibling != null) {
                lastSibling.addSiblingNode(nodeToAdd);
            } else {
                throw new IllegalStateException("The tree structure is not ideal");
            }
        }
    }

    private TreeNode<T> getLastSibling() {
        TreeNode<T> node = this.getSiblingNode();
        if (node == null) {
            return this;
        }
        while (node != null) {
            if (node.getSiblingNode() == null) {
                return node;
            } else {
                node = node.getSiblingNode();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                " data=" + data +
                ", child=" + child +
                ", sibling=" + sibling +
                '}';
    }

    public String printCurrent() {

        return "TreeNode{ data=" + data + " , parent = " + (parent != null ? parent.printCurrent() : null) + " }";
    }
}
