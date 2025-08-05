package javax.swing.tree;

import java.awt.Rectangle;
import java.util.Enumeration;
import javax.swing.event.TreeModelEvent;

/* loaded from: rt.jar:javax/swing/tree/AbstractLayoutCache.class */
public abstract class AbstractLayoutCache implements RowMapper {
    protected NodeDimensions nodeDimensions;
    protected TreeModel treeModel;
    protected TreeSelectionModel treeSelectionModel;
    protected boolean rootVisible;
    protected int rowHeight;

    /* loaded from: rt.jar:javax/swing/tree/AbstractLayoutCache$NodeDimensions.class */
    public static abstract class NodeDimensions {
        public abstract Rectangle getNodeDimensions(Object obj, int i2, int i3, boolean z2, Rectangle rectangle);
    }

    public abstract boolean isExpanded(TreePath treePath);

    public abstract Rectangle getBounds(TreePath treePath, Rectangle rectangle);

    public abstract TreePath getPathForRow(int i2);

    public abstract int getRowForPath(TreePath treePath);

    public abstract TreePath getPathClosestTo(int i2, int i3);

    public abstract Enumeration<TreePath> getVisiblePathsFrom(TreePath treePath);

    public abstract int getVisibleChildCount(TreePath treePath);

    public abstract void setExpandedState(TreePath treePath, boolean z2);

    public abstract boolean getExpandedState(TreePath treePath);

    public abstract int getRowCount();

    public abstract void invalidateSizes();

    public abstract void invalidatePathBounds(TreePath treePath);

    public abstract void treeNodesChanged(TreeModelEvent treeModelEvent);

    public abstract void treeNodesInserted(TreeModelEvent treeModelEvent);

    public abstract void treeNodesRemoved(TreeModelEvent treeModelEvent);

    public abstract void treeStructureChanged(TreeModelEvent treeModelEvent);

    public void setNodeDimensions(NodeDimensions nodeDimensions) {
        this.nodeDimensions = nodeDimensions;
    }

    public NodeDimensions getNodeDimensions() {
        return this.nodeDimensions;
    }

    public void setModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public TreeModel getModel() {
        return this.treeModel;
    }

    public void setRootVisible(boolean z2) {
        this.rootVisible = z2;
    }

    public boolean isRootVisible() {
        return this.rootVisible;
    }

    public void setRowHeight(int i2) {
        this.rowHeight = i2;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public void setSelectionModel(TreeSelectionModel treeSelectionModel) {
        if (this.treeSelectionModel != null) {
            this.treeSelectionModel.setRowMapper(null);
        }
        this.treeSelectionModel = treeSelectionModel;
        if (this.treeSelectionModel != null) {
            this.treeSelectionModel.setRowMapper(this);
        }
    }

    public TreeSelectionModel getSelectionModel() {
        return this.treeSelectionModel;
    }

    public int getPreferredHeight() {
        Rectangle bounds;
        int rowCount = getRowCount();
        if (rowCount > 0 && (bounds = getBounds(getPathForRow(rowCount - 1), null)) != null) {
            return bounds.f12373y + bounds.height;
        }
        return 0;
    }

    public int getPreferredWidth(Rectangle rectangle) {
        TreePath pathClosestTo;
        int i2;
        int iMax;
        if (getRowCount() > 0) {
            if (rectangle == null) {
                pathClosestTo = getPathForRow(0);
                i2 = Integer.MAX_VALUE;
            } else {
                pathClosestTo = getPathClosestTo(rectangle.f12372x, rectangle.f12373y);
                i2 = rectangle.height + rectangle.f12373y;
            }
            Enumeration<TreePath> visiblePathsFrom = getVisiblePathsFrom(pathClosestTo);
            if (visiblePathsFrom != null && visiblePathsFrom.hasMoreElements()) {
                Rectangle bounds = getBounds(visiblePathsFrom.nextElement(), null);
                if (bounds != null) {
                    iMax = bounds.f12372x + bounds.width;
                    if (bounds.f12373y >= i2) {
                        return iMax;
                    }
                } else {
                    iMax = 0;
                }
                while (bounds != null && visiblePathsFrom.hasMoreElements()) {
                    bounds = getBounds(visiblePathsFrom.nextElement(), bounds);
                    if (bounds != null && bounds.f12373y < i2) {
                        iMax = Math.max(iMax, bounds.f12372x + bounds.width);
                    } else {
                        bounds = null;
                    }
                }
                return iMax;
            }
            return 0;
        }
        return 0;
    }

    @Override // javax.swing.tree.RowMapper
    public int[] getRowsForPaths(TreePath[] treePathArr) {
        if (treePathArr == null) {
            return null;
        }
        int length = treePathArr.length;
        int[] iArr = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = getRowForPath(treePathArr[i2]);
        }
        return iArr;
    }

    protected Rectangle getNodeDimensions(Object obj, int i2, int i3, boolean z2, Rectangle rectangle) {
        NodeDimensions nodeDimensions = getNodeDimensions();
        if (nodeDimensions != null) {
            return nodeDimensions.getNodeDimensions(obj, i2, i3, z2, rectangle);
        }
        return null;
    }

    protected boolean isFixedRowHeight() {
        return this.rowHeight > 0;
    }
}
