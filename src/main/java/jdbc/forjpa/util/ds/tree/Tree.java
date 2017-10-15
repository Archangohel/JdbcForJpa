package jdbc.forjpa.util.ds.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @auther Archan on 14/10/17.
 */
public class Tree<T> implements Iterable<TreeNode<T>> {
    private TreeNode<T> root;

    enum TraverseStrategy {TOP_TO_BOTTOM, BOTTOM_TO_TOP}

    ;

    public TreeNode<T> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<T> root) {
        this.root = root;
    }

    public void traverse(Visitor<TreeNode<T>> visitor, TraverseStrategy traverseStrategy,
                         ContextHolder contextHolder) {
        traverseInternal(this.getRoot(), traverseStrategy, visitor, contextHolder);
    }

    private void traverseInternal(TreeNode<T> node, TraverseStrategy traverseStrategy,
                                  Visitor<TreeNode<T>> visitor, ContextHolder contextHolder) {
        if (node == null) {
            return;
        }
        if (TraverseStrategy.TOP_TO_BOTTOM.equals(traverseStrategy)) {
            visitor.beforeVisit(node);
            visitor.visit(node);
            traverseInternal(node.getChildNode(), traverseStrategy, visitor, contextHolder);
            traverseInternal(node.getSiblingNode(), traverseStrategy, visitor, contextHolder);
            visitor.afterVisit(node);
        } else if (TraverseStrategy.BOTTOM_TO_TOP.equals(traverseStrategy)) {
            traverseInternal(node.getChildNode(), traverseStrategy, visitor, contextHolder);
            traverseInternal(node.getSiblingNode(), traverseStrategy, visitor, contextHolder);
            visitor.beforeVisit(node);
            visitor.visit(node);
            visitor.afterVisit(node);
        } else {
            // invalid traverse type.
        }
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        final List<TreeNode<T>> orderedList = new ArrayList<>();
        Visitor<TreeNode<T>> listRegistrarVisitor = new AbstractVisitor<TreeNode<T>>() {
            @Override
            public void visit(TreeNode<T> element) {
                orderedList.add(element);
            }
        };
        traverseInternal(this.getRoot(), TraverseStrategy.TOP_TO_BOTTOM, listRegistrarVisitor, null);
        return orderedList.iterator();
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                "}";
    }

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
        traverseInternal(this.getRoot(), TraverseStrategy.TOP_TO_BOTTOM, printingVisitor, null);
        return builder.toString();
    }
}