package jdbc.forjpa.util.ds.tree;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther Archan on 14/10/17.
 */
public class TreeTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Tree<Character> generateTree() {
        // A
        // \--> B
        // \    \--> D
        // \    \--> E
        // \--> C

        Tree<Character> characterTree = new Tree<>();
        TreeNode<Character> root = new TreeNode<>('A');
        characterTree.setRoot(root);
        TreeNode<Character> rootChild = new TreeNode<>('B');
        root.append(rootChild);
        TreeNode<Character> rootChildSibling = new TreeNode<>('C');
        root.append(rootChildSibling);
        TreeNode<Character> rooChildChild = new TreeNode<>('D');
        rootChild.append(rooChildChild);
        TreeNode<Character> rooChildChild1 = new TreeNode<>('E');
        rootChild.append(rooChildChild1);
        return characterTree;
    }

    @Test
    public void testTreeTopToBottomTraversal() {
        Tree<Character> characterTree = generateTree();
        Visitor<TreeNode<Character>> printingVisitor = new Visitor<TreeNode<Character>>() {
            @Override
            public void beforeVisit(TreeNode<Character> element) {
            }

            @Override
            public void visit(TreeNode<Character> element) {
                logger.info("NODE {}", element.getContent());

            }

            @Override
            public void afterVisit(TreeNode<Character> element) {
            }

            @Override
            public Tree.TraverseStrategy getDefaultTraverseStrategy() {
                return null;
            }
        };
        characterTree.traverse(printingVisitor, Tree.TraverseStrategy.PARENT_TO_CHILD, new ContextHolder());
        logger.info("{}", characterTree.printWithIndentation());

    }

    @Test
    public void testTreeBottomToTopTraversal() {
        Tree<Character> characterTree = generateTree();
        Visitor<TreeNode<Character>> printingVisitor = new Visitor<TreeNode<Character>>() {
            @Override
            public void beforeVisit(TreeNode<Character> element) {
            }

            @Override
            public void visit(TreeNode<Character> element) {
                logger.info("NODE {}", element.getContent());
            }

            @Override
            public void afterVisit(TreeNode<Character> element) {
            }

            @Override
            public Tree.TraverseStrategy getDefaultTraverseStrategy() {
                return null;
            }
        };
        characterTree.traverse(printingVisitor, Tree.TraverseStrategy.PARENT_TO_CHILD, new ContextHolder());
        logger.info("{}", characterTree.printWithIndentation());

    }

    @Test
    public void testTreeIteration() {
        Tree<Character> characterTree = generateTree();
        logger.info("Tree {}", characterTree);
        for (TreeNode<Character> node : characterTree) {
            logger.info("NODE {}", node.getContent());
        }
        logger.info("{}", characterTree.printWithIndentation());

    }

    @Test
    public void testPrintTree() {
        Tree<Character> characterTree = generateTree();
        logger.info("{}", characterTree);
    }


    @Test
    public void testPrintWithIndentationTree() {
        Tree<Character> characterTree = generateTree();
        logger.info("run1 {}", characterTree.printWithIndentation());
        logger.info("run2 {}", characterTree.printWithIndentation());
    }

}

