package javax.swing.tree;

import java.util.Enumeration;

/* loaded from: rt.jar:javax/swing/tree/TreeNode.class */
public interface TreeNode {
    TreeNode getChildAt(int i2);

    int getChildCount();

    TreeNode getParent();

    int getIndex(TreeNode treeNode);

    boolean getAllowsChildren();

    boolean isLeaf();

    Enumeration children();
}
