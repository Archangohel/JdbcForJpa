package jdbc.forjpa.util.ds.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @auther Archan on 14/10/17.
 */
public class Tree<T> implements Iterable<TreeNode<T>> {
    private TreeNode<T> root;

    enum TraverseStrategy {PARENT_TO_CHILD, CHILD_TO_PARENT}

    ;

    public TreeNode<T> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<T> root) {
        this.root = root;
    }

    /**
     * Traverse the tree in specified {@link jdbc.forjpa.util.ds.tree.Tree.TraverseStrategy} order
     * and executes visitor on each node.
     *
     * @param visitor
     * @param traverseStrategy
     * @param contextHolder
     */
    public void traverse(Visitor<TreeNode<T>> visitor, TraverseStrategy traverseStrategy,
                         ContextHolder contextHolder) {
        traverseInternal(this.getRoot(), traverseStrategy, visitor, contextHolder);
    }

    private void traverseInternal(TreeNode<T> node, TraverseStrategy traverseStrategy,
                                  Visitor<TreeNode<T>> visitor, ContextHolder contextHolder) {
        if (node == null) {
            return;
        }
        try {
            if (TraverseStrategy.PARENT_TO_CHILD.equals(traverseStrategy)) {
                visitor.beforeVisit(node);
                visitor.visit(node);
                while (node.hasNextChild()) {
                    traverseInternal(node.nextChild(), traverseStrategy, visitor, contextHolder);
                }
                visitor.afterVisit(node);
            } else if (TraverseStrategy.CHILD_TO_PARENT.equals(traverseStrategy)) {
                visitor.beforeVisit(node);
                while (node.hasNextChild()) {
                    traverseInternal(node.nextChild(), traverseStrategy, visitor, contextHolder);
                }
                visitor.visit(node);
                visitor.afterVisit(node);
            } else {
                // invalid traverse type.
                throw new RuntimeException("WTF! Invalid traverse strategy " + traverseStrategy.name() + "!");
            }
        } catch (Exception ex) {
            //cleanup the iterators as quiting the recursion.
            cleanup();
            throw ex;
        } finally {
        }
    }

    /**
     * Returns a iterator with Top to bottom node order.
     *
     * @return
     */
    @Override
    public Iterator<TreeNode<T>> iterator() {
        final List<TreeNode<T>> orderedList = new ArrayList<>();
        Visitor<TreeNode<T>> listRegistrarVisitor = new AbstractVisitor<TreeNode<T>>() {
            @Override
            public void visit(TreeNode<T> element) {
                orderedList.add(element);
            }
        };
        traverseInternal(this.getRoot(), TraverseStrategy.PARENT_TO_CHILD, listRegistrarVisitor, null);
        return orderedList.iterator();
    }

    /**
     * Readable tree. To be used for debugging purpose.
     *
     * @return
     */
    public String printWithIndentation() {
        StringBuilder builder = new StringBuilder();
        Visitor<TreeNode<T>> printingVisitor = new AbstractVisitor<TreeNode<T>>() {
            int tabIndentationCounter = 0;

            @Override
            public void beforeVisit(TreeNode<T> element) {
                tabIndentationCounter++;
                builder.append("\n");
            }

            @Override
            public void visit(TreeNode<T> element) {
                boolean isChild = false;
                for (int i = 1; i < tabIndentationCounter; i++) {
                    builder.append("\\    ");
                    isChild = true;
                }
                if (isChild) {
                    builder.append("\\--->");
                }
                builder.append(element.printCurrent());
            }

            @Override
            public void afterVisit(TreeNode<T> element) {
                tabIndentationCounter--;
            }
        };
        traverseInternal(this.getRoot(), TraverseStrategy.PARENT_TO_CHILD, printingVisitor, null);
        return builder.toString();
    }

    /**
     * Cleans up all the ad-hoc objects created to iterate / traverse through the tree.
     */
    public void cleanup() {
        for (TreeNode<T> node : this) {
            node.cleanup();
        }
    }

    @Override
    public String toString() {
        return "Tree{root=" + root + "}";
    }
}