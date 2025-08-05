package javax.swing.tree;

import java.beans.PropertyChangeListener;
import javax.swing.event.TreeSelectionListener;

/* loaded from: rt.jar:javax/swing/tree/TreeSelectionModel.class */
public interface TreeSelectionModel {
    public static final int SINGLE_TREE_SELECTION = 1;
    public static final int CONTIGUOUS_TREE_SELECTION = 2;
    public static final int DISCONTIGUOUS_TREE_SELECTION = 4;

    void setSelectionMode(int i2);

    int getSelectionMode();

    void setSelectionPath(TreePath treePath);

    void setSelectionPaths(TreePath[] treePathArr);

    void addSelectionPath(TreePath treePath);

    void addSelectionPaths(TreePath[] treePathArr);

    void removeSelectionPath(TreePath treePath);

    void removeSelectionPaths(TreePath[] treePathArr);

    TreePath getSelectionPath();

    TreePath[] getSelectionPaths();

    int getSelectionCount();

    boolean isPathSelected(TreePath treePath);

    boolean isSelectionEmpty();

    void clearSelection();

    void setRowMapper(RowMapper rowMapper);

    RowMapper getRowMapper();

    int[] getSelectionRows();

    int getMinSelectionRow();

    int getMaxSelectionRow();

    boolean isRowSelected(int i2);

    void resetRowSelection();

    int getLeadSelectionRow();

    TreePath getLeadSelectionPath();

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void addTreeSelectionListener(TreeSelectionListener treeSelectionListener);

    void removeTreeSelectionListener(TreeSelectionListener treeSelectionListener);
}
