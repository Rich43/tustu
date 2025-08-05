package javax.swing.tree;

import javax.swing.event.TreeModelListener;

/* loaded from: rt.jar:javax/swing/tree/TreeModel.class */
public interface TreeModel {
    Object getRoot();

    Object getChild(Object obj, int i2);

    int getChildCount(Object obj);

    boolean isLeaf(Object obj);

    void valueForPathChanged(TreePath treePath, Object obj);

    int getIndexOfChild(Object obj, Object obj2);

    void addTreeModelListener(TreeModelListener treeModelListener);

    void removeTreeModelListener(TreeModelListener treeModelListener);
}
