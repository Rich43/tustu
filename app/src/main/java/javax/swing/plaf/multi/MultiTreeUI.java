package javax.swing.plaf.multi;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/plaf/multi/MultiTreeUI.class */
public class MultiTreeUI extends TreeUI {
    protected Vector uis = new Vector();

    public ComponentUI[] getUIs() {
        return MultiLookAndFeel.uisToArray(this.uis);
    }

    @Override // javax.swing.plaf.TreeUI
    public Rectangle getPathBounds(JTree jTree, TreePath treePath) {
        Rectangle pathBounds = ((TreeUI) this.uis.elementAt(0)).getPathBounds(jTree, treePath);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).getPathBounds(jTree, treePath);
        }
        return pathBounds;
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getPathForRow(JTree jTree, int i2) {
        TreePath pathForRow = ((TreeUI) this.uis.elementAt(0)).getPathForRow(jTree, i2);
        for (int i3 = 1; i3 < this.uis.size(); i3++) {
            ((TreeUI) this.uis.elementAt(i3)).getPathForRow(jTree, i2);
        }
        return pathForRow;
    }

    @Override // javax.swing.plaf.TreeUI
    public int getRowForPath(JTree jTree, TreePath treePath) {
        int rowForPath = ((TreeUI) this.uis.elementAt(0)).getRowForPath(jTree, treePath);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).getRowForPath(jTree, treePath);
        }
        return rowForPath;
    }

    @Override // javax.swing.plaf.TreeUI
    public int getRowCount(JTree jTree) {
        int rowCount = ((TreeUI) this.uis.elementAt(0)).getRowCount(jTree);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).getRowCount(jTree);
        }
        return rowCount;
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getClosestPathForLocation(JTree jTree, int i2, int i3) {
        TreePath closestPathForLocation = ((TreeUI) this.uis.elementAt(0)).getClosestPathForLocation(jTree, i2, i3);
        for (int i4 = 1; i4 < this.uis.size(); i4++) {
            ((TreeUI) this.uis.elementAt(i4)).getClosestPathForLocation(jTree, i2, i3);
        }
        return closestPathForLocation;
    }

    @Override // javax.swing.plaf.TreeUI
    public boolean isEditing(JTree jTree) {
        boolean zIsEditing = ((TreeUI) this.uis.elementAt(0)).isEditing(jTree);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).isEditing(jTree);
        }
        return zIsEditing;
    }

    @Override // javax.swing.plaf.TreeUI
    public boolean stopEditing(JTree jTree) {
        boolean zStopEditing = ((TreeUI) this.uis.elementAt(0)).stopEditing(jTree);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).stopEditing(jTree);
        }
        return zStopEditing;
    }

    @Override // javax.swing.plaf.TreeUI
    public void cancelEditing(JTree jTree) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).cancelEditing(jTree);
        }
    }

    @Override // javax.swing.plaf.TreeUI
    public void startEditingAtPath(JTree jTree, TreePath treePath) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).startEditingAtPath(jTree, treePath);
        }
    }

    @Override // javax.swing.plaf.TreeUI
    public TreePath getEditingPath(JTree jTree) {
        TreePath editingPath = ((TreeUI) this.uis.elementAt(0)).getEditingPath(jTree);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((TreeUI) this.uis.elementAt(i2)).getEditingPath(jTree);
        }
        return editingPath;
    }

    @Override // javax.swing.plaf.ComponentUI
    public boolean contains(JComponent jComponent, int i2, int i3) {
        boolean zContains = ((ComponentUI) this.uis.elementAt(0)).contains(jComponent, i2, i3);
        for (int i4 = 1; i4 < this.uis.size(); i4++) {
            ((ComponentUI) this.uis.elementAt(i4)).contains(jComponent, i2, i3);
        }
        return zContains;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).update(graphics, jComponent);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        MultiTreeUI multiTreeUI = new MultiTreeUI();
        return MultiLookAndFeel.createUIs(multiTreeUI, multiTreeUI.uis, jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).installUI(jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).uninstallUI(jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        for (int i2 = 0; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).paint(graphics, jComponent);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = ((ComponentUI) this.uis.elementAt(0)).getPreferredSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getPreferredSize(jComponent);
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension minimumSize = ((ComponentUI) this.uis.elementAt(0)).getMinimumSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getMinimumSize(jComponent);
        }
        return minimumSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension maximumSize = ((ComponentUI) this.uis.elementAt(0)).getMaximumSize(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getMaximumSize(jComponent);
        }
        return maximumSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getAccessibleChildrenCount(JComponent jComponent) {
        int accessibleChildrenCount = ((ComponentUI) this.uis.elementAt(0)).getAccessibleChildrenCount(jComponent);
        for (int i2 = 1; i2 < this.uis.size(); i2++) {
            ((ComponentUI) this.uis.elementAt(i2)).getAccessibleChildrenCount(jComponent);
        }
        return accessibleChildrenCount;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Accessible getAccessibleChild(JComponent jComponent, int i2) {
        Accessible accessibleChild = ((ComponentUI) this.uis.elementAt(0)).getAccessibleChild(jComponent, i2);
        for (int i3 = 1; i3 < this.uis.size(); i3++) {
            ((ComponentUI) this.uis.elementAt(i3)).getAccessibleChild(jComponent, i2);
        }
        return accessibleChild;
    }
}
