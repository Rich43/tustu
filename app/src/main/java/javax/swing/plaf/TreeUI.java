package javax.swing.plaf;

import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/plaf/TreeUI.class */
public abstract class TreeUI extends ComponentUI {
    public abstract Rectangle getPathBounds(JTree jTree, TreePath treePath);

    public abstract TreePath getPathForRow(JTree jTree, int i2);

    public abstract int getRowForPath(JTree jTree, TreePath treePath);

    public abstract int getRowCount(JTree jTree);

    public abstract TreePath getClosestPathForLocation(JTree jTree, int i2, int i3);

    public abstract boolean isEditing(JTree jTree);

    public abstract boolean stopEditing(JTree jTree);

    public abstract void cancelEditing(JTree jTree);

    public abstract void startEditingAtPath(JTree jTree, TreePath treePath);

    public abstract TreePath getEditingPath(JTree jTree);
}
