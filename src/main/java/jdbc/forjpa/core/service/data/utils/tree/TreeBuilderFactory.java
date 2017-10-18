package jdbc.forjpa.core.service.data.utils.tree;

import jdbc.forjpa.core.service.data.TreeBuildingStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther Archan on 18/10/17.
 */

@Component
public class TreeBuilderFactory {

    private Map<TreeBuildingStrategy, TreeBuilder> treeBuilderMap = new HashMap<>();

    void registerTreeBuilder(TreeBuilder treeBuilder, TreeBuildingStrategy treeBuildingStrategy) {
        treeBuilderMap.put(treeBuildingStrategy, treeBuilder);
    }

    public TreeBuilder getTreeBuilderByStrategy(TreeBuildingStrategy strategy) {
        if (treeBuilderMap.containsKey(strategy)) {
            return treeBuilderMap.get(strategy);
        } else {
            throw new RuntimeException("No tree builder found for strategy " + strategy);
        }
    }
}
