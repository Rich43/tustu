package javax.swing.tree;

/* loaded from: rt.jar:javax/swing/tree/MutableTreeNode.class */
public interface MutableTreeNode extends TreeNode {
    void insert(MutableTreeNode mutableTreeNode, int i2);

    void remove(int i2);

    void remove(MutableTreeNode mutableTreeNode);

    void setUserObject(Object obj);

    void removeFromParent();

    void setParent(MutableTreeNode mutableTreeNode);
}
