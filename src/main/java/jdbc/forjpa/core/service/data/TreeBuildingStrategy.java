package jdbc.forjpa.core.service.data;

/**
 * Different tree building strategies.
 *
 * @auther Archan on 18/10/17.
 */
public enum TreeBuildingStrategy {
    AUTO_ALL_ASSOCIATIONS, // build tree for the root as well as the one level associations.
    AUTO_ALL_ASSOCIATIONS_RECURSIVE, // build tree for the root as well as the all level associations.
    EXTERNAL // build tree for the root using external way not by using the JAP annotaions.
}
