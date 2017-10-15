package jdbc.forjpa.util.ds.tree;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther Archan on 14/10/17.
 */
public class TreeTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testTraverseTree() {
        // A
        // \--> B
        // \    \-->D
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


        Visitor<TreeNode<Character>> printingVisitor = new Visitor<TreeNode<Character>>() {
            @Override
            public void beforeVisit(TreeNode<Character> element) {
                //logger.info("Before visiting {}", element.getContent());
            }

            @Override
            public void visit(TreeNode<Character> element) {
                logger.info("NODE {}", element.getContent());

            }

            @Override
            public void afterVisit(TreeNode<Character> element) {
                //logger.info("After visiting {}", element.getContent());
            }
        };
        characterTree.traverse(printingVisitor, Tree.TraverseStrategy.TOP_TO_BOTTOM, new ContextHolder());
    }


    @Test
    public void testIterationTree() {
        // A
        // \--> B
        // \    \-->D
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

        logger.info("Tree {}",characterTree );

        for (TreeNode<Character> node : characterTree) {
            logger.info("NODE {}", node.getContent());
        }
    }

    @Test
    public void testPrintTree() {
        // A
        // \--> B
        // \    \-->D
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
        logger.info("{}",characterTree );
    }


    @Test
    public void testPrintWithIndentationTree() {
        // A
        // \--> B
        // \    \-->D
        // \    \-->E
        // \--> C
        Tree<Character> characterTree = new Tree<>();
        TreeNode<Character> root = new TreeNode<>('A');
        characterTree.setRoot(root);
        TreeNode<Character> rootChild = new TreeNode<>('B');
        root.append(rootChild);
        TreeNode<Character> rootChildSibling = new TreeNode<>('C');
        root.append(rootChildSibling);
        TreeNode<Character> rootChildSibling1 = new TreeNode<>('H');
        root.append(rootChildSibling1);
        logger.info("{}",characterTree.printWithIndentation() );
    }

}

