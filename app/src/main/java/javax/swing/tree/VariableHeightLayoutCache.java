package javax.swing.tree;

import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.AbstractLayoutCache;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/tree/VariableHeightLayoutCache.class */
public class VariableHeightLayoutCache extends AbstractLayoutCache {
    private boolean updateNodeSizes;
    private TreeStateNode root;
    private Stack<Stack<TreePath>> tempStacks = new Stack<>();
    private Vector<Object> visibleNodes = new Vector<>();
    private Rectangle boundsBuffer = new Rectangle();
    private Hashtable<TreePath, TreeStateNode> treePathMapping = new Hashtable<>();

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setModel(TreeModel treeModel) {
        super.setModel(treeModel);
        rebuild(false);
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setRootVisible(boolean z2) {
        if (isRootVisible() != z2 && this.root != null) {
            if (z2) {
                this.root.updatePreferredSize(0);
                this.visibleNodes.insertElementAt(this.root, 0);
            } else if (this.visibleNodes.size() > 0) {
                this.visibleNodes.removeElementAt(0);
                if (this.treeSelectionModel != null) {
                    this.treeSelectionModel.removeSelectionPath(this.root.getTreePath());
                }
            }
            if (this.treeSelectionModel != null) {
                this.treeSelectionModel.resetRowSelection();
            }
            if (getRowCount() > 0) {
                getNode(0).setYOrigin(0);
            }
            updateYLocationsFrom(0);
            visibleNodesChanged();
        }
        super.setRootVisible(z2);
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setRowHeight(int i2) {
        if (i2 != getRowHeight()) {
            super.setRowHeight(i2);
            invalidateSizes();
            visibleNodesChanged();
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setNodeDimensions(AbstractLayoutCache.NodeDimensions nodeDimensions) {
        super.setNodeDimensions(nodeDimensions);
        invalidateSizes();
        visibleNodesChanged();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setExpandedState(TreePath treePath, boolean z2) {
        if (treePath != null) {
            if (z2) {
                ensurePathIsExpanded(treePath, true);
                return;
            }
            TreeStateNode nodeForPath = getNodeForPath(treePath, false, true);
            if (nodeForPath != null) {
                nodeForPath.makeVisible();
                nodeForPath.collapse();
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public boolean getExpandedState(TreePath treePath) {
        TreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        return nodeForPath != null && nodeForPath.isVisible() && nodeForPath.isExpanded();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public Rectangle getBounds(TreePath treePath, Rectangle rectangle) {
        TreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            if (this.updateNodeSizes) {
                updateNodeSizes(false);
            }
            return nodeForPath.getNodeBounds(rectangle);
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public TreePath getPathForRow(int i2) {
        if (i2 >= 0 && i2 < getRowCount()) {
            return getNode(i2).getTreePath();
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getRowForPath(TreePath treePath) {
        TreeStateNode nodeForPath;
        if (treePath != null && (nodeForPath = getNodeForPath(treePath, true, false)) != null) {
            return nodeForPath.getRow();
        }
        return -1;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getRowCount() {
        return this.visibleNodes.size();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void invalidatePathBounds(TreePath treePath) {
        TreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            nodeForPath.markSizeInvalid();
            if (nodeForPath.isVisible()) {
                updateYLocationsFrom(nodeForPath.getRow());
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getPreferredHeight() {
        int rowCount = getRowCount();
        if (rowCount > 0) {
            TreeStateNode node = getNode(rowCount - 1);
            return node.getYOrigin() + node.getPreferredHeight();
        }
        return 0;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getPreferredWidth(Rectangle rectangle) {
        if (this.updateNodeSizes) {
            updateNodeSizes(false);
        }
        return getMaxNodeWidth();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public TreePath getPathClosestTo(int i2, int i3) {
        if (getRowCount() == 0) {
            return null;
        }
        if (this.updateNodeSizes) {
            updateNodeSizes(false);
        }
        return getNode(getRowContainingYLocation(i3)).getTreePath();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public Enumeration<TreePath> getVisiblePathsFrom(TreePath treePath) {
        TreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            return new VisibleTreeStateNodeEnumeration(this, nodeForPath);
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getVisibleChildCount(TreePath treePath) {
        TreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            return nodeForPath.getVisibleChildCount();
        }
        return 0;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void invalidateSizes() {
        if (this.root != null) {
            this.root.deepMarkSizeInvalid();
        }
        if (!isFixedRowHeight() && this.visibleNodes.size() > 0) {
            updateNodeSizes(true);
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public boolean isExpanded(TreePath treePath) {
        TreeStateNode nodeForPath;
        return (treePath == null || (nodeForPath = getNodeForPath(treePath, true, false)) == null || !nodeForPath.isExpanded()) ? false : true;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesChanged(TreeModelEvent treeModelEvent) {
        int row;
        if (treeModelEvent != null) {
            int[] childIndices = treeModelEvent.getChildIndices();
            TreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            if (nodeForPath != null) {
                Object value = nodeForPath.getValue();
                nodeForPath.updatePreferredSize();
                if (nodeForPath.hasBeenExpanded() && childIndices != null) {
                    for (int i2 = 0; i2 < childIndices.length; i2++) {
                        TreeStateNode treeStateNode = (TreeStateNode) nodeForPath.getChildAt(childIndices[i2]);
                        treeStateNode.setUserObject(this.treeModel.getChild(value, childIndices[i2]));
                        treeStateNode.updatePreferredSize();
                    }
                } else if (nodeForPath == this.root) {
                    nodeForPath.updatePreferredSize();
                }
                if (!isFixedRowHeight() && (row = nodeForPath.getRow()) != -1) {
                    updateYLocationsFrom(row);
                }
                visibleNodesChanged();
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesInserted(TreeModelEvent treeModelEvent) {
        if (treeModelEvent != null) {
            int[] childIndices = treeModelEvent.getChildIndices();
            TreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            if (nodeForPath != null && childIndices != null && childIndices.length > 0) {
                if (nodeForPath.hasBeenExpanded()) {
                    int childCount = nodeForPath.getChildCount();
                    nodeForPath.getValue();
                    boolean z2 = (nodeForPath == this.root && !this.rootVisible) || (nodeForPath.getRow() != -1 && nodeForPath.isExpanded());
                    for (int i2 : childIndices) {
                        createNodeAt(nodeForPath, i2);
                    }
                    if (childCount == 0) {
                        nodeForPath.updatePreferredSize();
                    }
                    if (this.treeSelectionModel != null) {
                        this.treeSelectionModel.resetRowSelection();
                    }
                    if (!isFixedRowHeight() && (z2 || (childCount == 0 && nodeForPath.isVisible()))) {
                        if (nodeForPath == this.root) {
                            updateYLocationsFrom(0);
                        } else {
                            updateYLocationsFrom(nodeForPath.getRow());
                        }
                        visibleNodesChanged();
                        return;
                    }
                    if (z2) {
                        visibleNodesChanged();
                        return;
                    }
                    return;
                }
                if (this.treeModel.getChildCount(nodeForPath.getValue()) - childIndices.length == 0) {
                    nodeForPath.updatePreferredSize();
                    if (!isFixedRowHeight() && nodeForPath.isVisible()) {
                        updateYLocationsFrom(nodeForPath.getRow());
                    }
                }
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
        int row;
        if (treeModelEvent != null) {
            int[] childIndices = treeModelEvent.getChildIndices();
            TreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            if (nodeForPath != null && childIndices != null && childIndices.length > 0) {
                if (nodeForPath.hasBeenExpanded()) {
                    boolean z2 = (nodeForPath == this.root && !this.rootVisible) || (nodeForPath.getRow() != -1 && nodeForPath.isExpanded());
                    for (int length = childIndices.length - 1; length >= 0; length--) {
                        TreeStateNode treeStateNode = (TreeStateNode) nodeForPath.getChildAt(childIndices[length]);
                        if (treeStateNode.isExpanded()) {
                            treeStateNode.collapse(false);
                        }
                        if (z2 && (row = treeStateNode.getRow()) != -1) {
                            this.visibleNodes.removeElementAt(row);
                        }
                        nodeForPath.remove(childIndices[length]);
                    }
                    if (nodeForPath.getChildCount() == 0) {
                        nodeForPath.updatePreferredSize();
                        if (nodeForPath.isExpanded() && nodeForPath.isLeaf()) {
                            nodeForPath.collapse(false);
                        }
                    }
                    if (this.treeSelectionModel != null) {
                        this.treeSelectionModel.resetRowSelection();
                    }
                    if (!isFixedRowHeight() && (z2 || (nodeForPath.getChildCount() == 0 && nodeForPath.isVisible()))) {
                        if (nodeForPath == this.root) {
                            if (getRowCount() > 0) {
                                getNode(0).setYOrigin(0);
                            }
                            updateYLocationsFrom(0);
                        } else {
                            updateYLocationsFrom(nodeForPath.getRow());
                        }
                        visibleNodesChanged();
                        return;
                    }
                    if (z2) {
                        visibleNodesChanged();
                        return;
                    }
                    return;
                }
                if (this.treeModel.getChildCount(nodeForPath.getValue()) == 0) {
                    nodeForPath.updatePreferredSize();
                    if (!isFixedRowHeight() && nodeForPath.isVisible()) {
                        updateYLocationsFrom(nodeForPath.getRow());
                    }
                }
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeStructureChanged(TreeModelEvent treeModelEvent) {
        if (treeModelEvent != null) {
            TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, getModel());
            TreeStateNode nodeForPath = getNodeForPath(treePath, false, false);
            if (nodeForPath == this.root || (nodeForPath == null && ((treePath == null && this.treeModel != null && this.treeModel.getRoot() == null) || (treePath != null && treePath.getPathCount() == 1)))) {
                rebuild(true);
                return;
            }
            if (nodeForPath != null) {
                boolean zIsExpanded = nodeForPath.isExpanded();
                boolean z2 = nodeForPath.getRow() != -1;
                TreeStateNode treeStateNode = (TreeStateNode) nodeForPath.getParent();
                int index = treeStateNode.getIndex(nodeForPath);
                if (z2 && zIsExpanded) {
                    nodeForPath.collapse(false);
                }
                if (z2) {
                    this.visibleNodes.removeElement(nodeForPath);
                }
                nodeForPath.removeFromParent();
                createNodeAt(treeStateNode, index);
                TreeStateNode treeStateNode2 = (TreeStateNode) treeStateNode.getChildAt(index);
                if (z2 && zIsExpanded) {
                    treeStateNode2.expand(false);
                }
                int row = treeStateNode2.getRow();
                if (!isFixedRowHeight() && z2) {
                    if (row == 0) {
                        updateYLocationsFrom(row);
                    } else {
                        updateYLocationsFrom(row - 1);
                    }
                    visibleNodesChanged();
                    return;
                }
                if (z2) {
                    visibleNodesChanged();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void visibleNodesChanged() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMapping(TreeStateNode treeStateNode) {
        this.treePathMapping.put(treeStateNode.getTreePath(), treeStateNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeMapping(TreeStateNode treeStateNode) {
        this.treePathMapping.remove(treeStateNode.getTreePath());
    }

    private TreeStateNode getMapping(TreePath treePath) {
        return this.treePathMapping.get(treePath);
    }

    private Rectangle getBounds(int i2, Rectangle rectangle) {
        if (this.updateNodeSizes) {
            updateNodeSizes(false);
        }
        if (i2 >= 0 && i2 < getRowCount()) {
            return getNode(i2).getNodeBounds(rectangle);
        }
        return null;
    }

    private void rebuild(boolean z2) {
        Object root;
        this.treePathMapping.clear();
        if (this.treeModel != null && (root = this.treeModel.getRoot()) != null) {
            this.root = createNodeForValue(root);
            this.root.path = new TreePath(root);
            addMapping(this.root);
            this.root.updatePreferredSize(0);
            this.visibleNodes.removeAllElements();
            if (isRootVisible()) {
                this.visibleNodes.addElement(this.root);
            }
            if (!this.root.isExpanded()) {
                this.root.expand();
            } else {
                Enumeration enumerationChildren = this.root.children();
                while (enumerationChildren.hasMoreElements()) {
                    this.visibleNodes.addElement(enumerationChildren.nextElement2());
                }
                if (!isFixedRowHeight()) {
                    updateYLocationsFrom(0);
                }
            }
        } else {
            this.visibleNodes.removeAllElements();
            this.root = null;
        }
        if (z2 && this.treeSelectionModel != null) {
            this.treeSelectionModel.clearSelection();
        }
        visibleNodesChanged();
    }

    private TreeStateNode createNodeAt(TreeStateNode treeStateNode, int i2) {
        int row;
        TreeStateNode treeStateNodeCreateNodeForValue = createNodeForValue(this.treeModel.getChild(treeStateNode.getValue(), i2));
        treeStateNode.insert(treeStateNodeCreateNodeForValue, i2);
        treeStateNodeCreateNodeForValue.updatePreferredSize(-1);
        boolean z2 = treeStateNode == this.root;
        if (treeStateNodeCreateNodeForValue != null && treeStateNode.isExpanded() && (treeStateNode.getRow() != -1 || z2)) {
            if (i2 == 0) {
                if (z2 && !isRootVisible()) {
                    row = 0;
                } else {
                    row = treeStateNode.getRow() + 1;
                }
            } else if (i2 == treeStateNode.getChildCount()) {
                row = treeStateNode.getLastVisibleNode().getRow() + 1;
            } else {
                row = ((TreeStateNode) treeStateNode.getChildAt(i2 - 1)).getLastVisibleNode().getRow() + 1;
            }
            this.visibleNodes.insertElementAt(treeStateNodeCreateNodeForValue, row);
        }
        return treeStateNodeCreateNodeForValue;
    }

    /* JADX WARN: Finally extract failed */
    private TreeStateNode getNodeForPath(TreePath treePath, boolean z2, boolean z3) {
        Stack<TreePath> stackPop;
        if (treePath != null) {
            TreeStateNode mapping = getMapping(treePath);
            if (mapping != null) {
                if (z2 && !mapping.isVisible()) {
                    return null;
                }
                return mapping;
            }
            if (this.tempStacks.size() == 0) {
                stackPop = new Stack<>();
            } else {
                stackPop = this.tempStacks.pop();
            }
            try {
                stackPop.push(treePath);
                for (TreePath parentPath = treePath.getParentPath(); parentPath != null; parentPath = parentPath.getParentPath()) {
                    TreeStateNode mapping2 = getMapping(parentPath);
                    if (mapping2 != null) {
                        while (mapping2 != null) {
                            if (stackPop.size() <= 0) {
                                break;
                            }
                            TreePath treePathPop = stackPop.pop();
                            mapping2.getLoadedChildren(z3);
                            int indexOfChild = this.treeModel.getIndexOfChild(mapping2.getUserObject(), treePathPop.getLastPathComponent());
                            if (indexOfChild == -1 || indexOfChild >= mapping2.getChildCount() || (z2 && !mapping2.isVisible())) {
                                mapping2 = null;
                            } else {
                                mapping2 = (TreeStateNode) mapping2.getChildAt(indexOfChild);
                            }
                        }
                        TreeStateNode treeStateNode = mapping2;
                        stackPop.removeAllElements();
                        this.tempStacks.push(stackPop);
                        return treeStateNode;
                    }
                    stackPop.push(parentPath);
                }
                stackPop.removeAllElements();
                this.tempStacks.push(stackPop);
                return null;
            } catch (Throwable th) {
                stackPop.removeAllElements();
                this.tempStacks.push(stackPop);
                throw th;
            }
        }
        return null;
    }

    private void updateYLocationsFrom(int i2) {
        if (i2 >= 0 && i2 < getRowCount()) {
            TreeStateNode node = getNode(i2);
            int yOrigin = node.getYOrigin() + node.getPreferredHeight();
            int size = this.visibleNodes.size();
            for (int i3 = i2 + 1; i3 < size; i3++) {
                TreeStateNode treeStateNode = (TreeStateNode) this.visibleNodes.elementAt(i3);
                treeStateNode.setYOrigin(yOrigin);
                yOrigin += treeStateNode.getPreferredHeight();
            }
        }
    }

    private void updateNodeSizes(boolean z2) {
        this.updateNodeSizes = false;
        int preferredHeight = 0;
        int size = this.visibleNodes.size();
        for (int i2 = 0; i2 < size; i2++) {
            TreeStateNode treeStateNode = (TreeStateNode) this.visibleNodes.elementAt(i2);
            treeStateNode.setYOrigin(preferredHeight);
            if (z2 || !treeStateNode.hasValidSize()) {
                treeStateNode.updatePreferredSize(i2);
            }
            preferredHeight += treeStateNode.getPreferredHeight();
        }
    }

    private int getRowContainingYLocation(int i2) {
        if (isFixedRowHeight()) {
            if (getRowCount() == 0) {
                return -1;
            }
            return Math.max(0, Math.min(getRowCount() - 1, i2 / getRowHeight()));
        }
        int rowCount = getRowCount();
        int i3 = rowCount;
        if (rowCount <= 0) {
            return -1;
        }
        int i4 = 0;
        int rowCount2 = 0;
        while (i4 < i3) {
            rowCount2 = ((i3 - i4) / 2) + i4;
            TreeStateNode treeStateNode = (TreeStateNode) this.visibleNodes.elementAt(rowCount2);
            int yOrigin = treeStateNode.getYOrigin();
            int preferredHeight = yOrigin + treeStateNode.getPreferredHeight();
            if (i2 < yOrigin) {
                i3 = rowCount2 - 1;
            } else {
                if (i2 < preferredHeight) {
                    break;
                }
                i4 = rowCount2 + 1;
            }
        }
        if (i4 == i3) {
            rowCount2 = i4;
            if (rowCount2 >= getRowCount()) {
                rowCount2 = getRowCount() - 1;
            }
        }
        return rowCount2;
    }

    private void ensurePathIsExpanded(TreePath treePath, boolean z2) {
        TreeStateNode nodeForPath;
        if (treePath != null) {
            if (this.treeModel.isLeaf(treePath.getLastPathComponent())) {
                treePath = treePath.getParentPath();
                z2 = true;
            }
            if (treePath != null && (nodeForPath = getNodeForPath(treePath, false, true)) != null) {
                nodeForPath.makeVisible();
                if (z2) {
                    nodeForPath.expand();
                }
            }
        }
    }

    private TreeStateNode getNode(int i2) {
        return (TreeStateNode) this.visibleNodes.elementAt(i2);
    }

    private int getMaxNodeWidth() {
        int i2 = 0;
        for (int rowCount = getRowCount() - 1; rowCount >= 0; rowCount--) {
            TreeStateNode node = getNode(rowCount);
            int preferredWidth = node.getPreferredWidth() + node.getXOrigin();
            if (preferredWidth > i2) {
                i2 = preferredWidth;
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TreeStateNode createNodeForValue(Object obj) {
        return new TreeStateNode(obj);
    }

    /* loaded from: rt.jar:javax/swing/tree/VariableHeightLayoutCache$TreeStateNode.class */
    private class TreeStateNode extends DefaultMutableTreeNode {
        protected int preferredWidth;
        protected int preferredHeight;
        protected int xOrigin;
        protected int yOrigin;
        protected boolean expanded;
        protected boolean hasBeenExpanded;
        protected TreePath path;

        public TreeStateNode(Object obj) {
            super(obj);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void setParent(MutableTreeNode mutableTreeNode) {
            super.setParent(mutableTreeNode);
            if (mutableTreeNode != null) {
                this.path = ((TreeStateNode) mutableTreeNode).getTreePath().pathByAddingChild(getUserObject());
                VariableHeightLayoutCache.this.addMapping(this);
            }
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void remove(int i2) {
            ((TreeStateNode) getChildAt(i2)).removeFromMapping();
            super.remove(i2);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void setUserObject(Object obj) {
            super.setUserObject(obj);
            if (this.path != null) {
                TreeStateNode treeStateNode = (TreeStateNode) getParent();
                if (treeStateNode != null) {
                    resetChildrenPaths(treeStateNode.getTreePath());
                } else {
                    resetChildrenPaths(null);
                }
            }
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public Enumeration children() {
            if (!isExpanded()) {
                return DefaultMutableTreeNode.EMPTY_ENUMERATION;
            }
            return super.children();
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public boolean isLeaf() {
            return VariableHeightLayoutCache.this.getModel().isLeaf(getValue());
        }

        public Rectangle getNodeBounds(Rectangle rectangle) {
            if (rectangle == null) {
                rectangle = new Rectangle(getXOrigin(), getYOrigin(), getPreferredWidth(), getPreferredHeight());
            } else {
                rectangle.f12372x = getXOrigin();
                rectangle.f12373y = getYOrigin();
                rectangle.width = getPreferredWidth();
                rectangle.height = getPreferredHeight();
            }
            return rectangle;
        }

        public int getXOrigin() {
            if (!hasValidSize()) {
                updatePreferredSize(getRow());
            }
            return this.xOrigin;
        }

        public int getYOrigin() {
            if (VariableHeightLayoutCache.this.isFixedRowHeight()) {
                int row = getRow();
                if (row == -1) {
                    return -1;
                }
                return VariableHeightLayoutCache.this.getRowHeight() * row;
            }
            return this.yOrigin;
        }

        public int getPreferredHeight() {
            if (VariableHeightLayoutCache.this.isFixedRowHeight()) {
                return VariableHeightLayoutCache.this.getRowHeight();
            }
            if (!hasValidSize()) {
                updatePreferredSize(getRow());
            }
            return this.preferredHeight;
        }

        public int getPreferredWidth() {
            if (!hasValidSize()) {
                updatePreferredSize(getRow());
            }
            return this.preferredWidth;
        }

        public boolean hasValidSize() {
            return this.preferredHeight != 0;
        }

        public int getRow() {
            return VariableHeightLayoutCache.this.visibleNodes.indexOf(this);
        }

        public boolean hasBeenExpanded() {
            return this.hasBeenExpanded;
        }

        public boolean isExpanded() {
            return this.expanded;
        }

        public TreeStateNode getLastVisibleNode() {
            TreeStateNode treeStateNode;
            TreeStateNode treeStateNode2 = this;
            while (true) {
                treeStateNode = treeStateNode2;
                if (!treeStateNode.isExpanded() || treeStateNode.getChildCount() <= 0) {
                    break;
                }
                treeStateNode2 = (TreeStateNode) treeStateNode.getLastChild();
            }
            return treeStateNode;
        }

        public boolean isVisible() {
            if (this == VariableHeightLayoutCache.this.root) {
                return true;
            }
            TreeStateNode treeStateNode = (TreeStateNode) getParent();
            return treeStateNode != null && treeStateNode.isExpanded() && treeStateNode.isVisible();
        }

        public int getModelChildCount() {
            if (this.hasBeenExpanded) {
                return super.getChildCount();
            }
            return VariableHeightLayoutCache.this.getModel().getChildCount(getValue());
        }

        public int getVisibleChildCount() {
            int visibleChildCount = 0;
            if (isExpanded()) {
                int childCount = getChildCount();
                visibleChildCount = 0 + childCount;
                for (int i2 = 0; i2 < childCount; i2++) {
                    visibleChildCount += ((TreeStateNode) getChildAt(i2)).getVisibleChildCount();
                }
            }
            return visibleChildCount;
        }

        public void toggleExpanded() {
            if (isExpanded()) {
                collapse();
            } else {
                expand();
            }
        }

        public void makeVisible() {
            TreeStateNode treeStateNode = (TreeStateNode) getParent();
            if (treeStateNode != null) {
                treeStateNode.expandParentAndReceiver();
            }
        }

        public void expand() {
            expand(true);
        }

        public void collapse() {
            collapse(true);
        }

        public Object getValue() {
            return getUserObject();
        }

        public TreePath getTreePath() {
            return this.path;
        }

        protected void resetChildrenPaths(TreePath treePath) {
            VariableHeightLayoutCache.this.removeMapping(this);
            if (treePath == null) {
                this.path = new TreePath(getUserObject());
            } else {
                this.path = treePath.pathByAddingChild(getUserObject());
            }
            VariableHeightLayoutCache.this.addMapping(this);
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                ((TreeStateNode) getChildAt(childCount)).resetChildrenPaths(this.path);
            }
        }

        protected void setYOrigin(int i2) {
            this.yOrigin = i2;
        }

        protected void shiftYOriginBy(int i2) {
            this.yOrigin += i2;
        }

        protected void updatePreferredSize() {
            updatePreferredSize(getRow());
        }

        protected void updatePreferredSize(int i2) {
            Rectangle nodeDimensions = VariableHeightLayoutCache.this.getNodeDimensions(getUserObject(), i2, getLevel(), isExpanded(), VariableHeightLayoutCache.this.boundsBuffer);
            if (nodeDimensions == null) {
                this.xOrigin = 0;
                this.preferredHeight = 0;
                this.preferredWidth = 0;
                VariableHeightLayoutCache.this.updateNodeSizes = true;
                return;
            }
            if (nodeDimensions.height == 0) {
                this.xOrigin = 0;
                this.preferredHeight = 0;
                this.preferredWidth = 0;
                VariableHeightLayoutCache.this.updateNodeSizes = true;
                return;
            }
            this.xOrigin = nodeDimensions.f12372x;
            this.preferredWidth = nodeDimensions.width;
            if (VariableHeightLayoutCache.this.isFixedRowHeight()) {
                this.preferredHeight = VariableHeightLayoutCache.this.getRowHeight();
            } else {
                this.preferredHeight = nodeDimensions.height;
            }
        }

        protected void markSizeInvalid() {
            this.preferredHeight = 0;
        }

        protected void deepMarkSizeInvalid() {
            markSizeInvalid();
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                ((TreeStateNode) getChildAt(childCount)).deepMarkSizeInvalid();
            }
        }

        protected Enumeration getLoadedChildren(boolean z2) {
            if (!z2 || this.hasBeenExpanded) {
                return super.children();
            }
            Object value = getValue();
            TreeModel model = VariableHeightLayoutCache.this.getModel();
            int childCount = model.getChildCount(value);
            this.hasBeenExpanded = true;
            int row = getRow();
            if (row == -1) {
                for (int i2 = 0; i2 < childCount; i2++) {
                    TreeStateNode treeStateNodeCreateNodeForValue = VariableHeightLayoutCache.this.createNodeForValue(model.getChild(value, i2));
                    add(treeStateNodeCreateNodeForValue);
                    treeStateNodeCreateNodeForValue.updatePreferredSize(-1);
                }
            } else {
                int i3 = row + 1;
                for (int i4 = 0; i4 < childCount; i4++) {
                    TreeStateNode treeStateNodeCreateNodeForValue2 = VariableHeightLayoutCache.this.createNodeForValue(model.getChild(value, i4));
                    add(treeStateNodeCreateNodeForValue2);
                    int i5 = i3;
                    i3++;
                    treeStateNodeCreateNodeForValue2.updatePreferredSize(i5);
                }
            }
            return super.children();
        }

        protected void didAdjustTree() {
        }

        protected void expandParentAndReceiver() {
            TreeStateNode treeStateNode = (TreeStateNode) getParent();
            if (treeStateNode != null) {
                treeStateNode.expandParentAndReceiver();
            }
            expand();
        }

        protected void expand(boolean z2) {
            int yOrigin;
            int i2;
            if (!isExpanded() && !isLeaf()) {
                boolean zIsFixedRowHeight = VariableHeightLayoutCache.this.isFixedRowHeight();
                int preferredHeight = getPreferredHeight();
                int row = getRow();
                this.expanded = true;
                updatePreferredSize(row);
                if (!this.hasBeenExpanded) {
                    Object value = getValue();
                    TreeModel model = VariableHeightLayoutCache.this.getModel();
                    int childCount = model.getChildCount(value);
                    this.hasBeenExpanded = true;
                    if (row == -1) {
                        for (int i3 = 0; i3 < childCount; i3++) {
                            TreeStateNode treeStateNodeCreateNodeForValue = VariableHeightLayoutCache.this.createNodeForValue(model.getChild(value, i3));
                            add(treeStateNodeCreateNodeForValue);
                            treeStateNodeCreateNodeForValue.updatePreferredSize(-1);
                        }
                    } else {
                        int i4 = row + 1;
                        for (int i5 = 0; i5 < childCount; i5++) {
                            TreeStateNode treeStateNodeCreateNodeForValue2 = VariableHeightLayoutCache.this.createNodeForValue(model.getChild(value, i5));
                            add(treeStateNodeCreateNodeForValue2);
                            treeStateNodeCreateNodeForValue2.updatePreferredSize(i4);
                        }
                    }
                }
                int i6 = row;
                Enumeration enumerationPreorderEnumeration = preorderEnumeration();
                enumerationPreorderEnumeration.nextElement2();
                if (zIsFixedRowHeight) {
                    yOrigin = 0;
                } else if (this == VariableHeightLayoutCache.this.root && !VariableHeightLayoutCache.this.isRootVisible()) {
                    yOrigin = 0;
                } else {
                    yOrigin = getYOrigin() + getPreferredHeight();
                }
                if (!zIsFixedRowHeight) {
                    while (enumerationPreorderEnumeration.hasMoreElements()) {
                        TreeStateNode treeStateNode = (TreeStateNode) enumerationPreorderEnumeration.nextElement2();
                        if (!VariableHeightLayoutCache.this.updateNodeSizes && !treeStateNode.hasValidSize()) {
                            treeStateNode.updatePreferredSize(i6 + 1);
                        }
                        treeStateNode.setYOrigin(yOrigin);
                        yOrigin += treeStateNode.getPreferredHeight();
                        i6++;
                        VariableHeightLayoutCache.this.visibleNodes.insertElementAt(treeStateNode, i6);
                    }
                } else {
                    while (enumerationPreorderEnumeration.hasMoreElements()) {
                        i6++;
                        VariableHeightLayoutCache.this.visibleNodes.insertElementAt((TreeStateNode) enumerationPreorderEnumeration.nextElement2(), i6);
                    }
                }
                if (z2 && (row != i6 || getPreferredHeight() != preferredHeight)) {
                    if (!zIsFixedRowHeight && (i2 = i6 + 1) < VariableHeightLayoutCache.this.getRowCount()) {
                        int yOrigin2 = (yOrigin - (getYOrigin() + getPreferredHeight())) + (getPreferredHeight() - preferredHeight);
                        for (int size = VariableHeightLayoutCache.this.visibleNodes.size() - 1; size >= i2; size--) {
                            ((TreeStateNode) VariableHeightLayoutCache.this.visibleNodes.elementAt(size)).shiftYOriginBy(yOrigin2);
                        }
                    }
                    didAdjustTree();
                    VariableHeightLayoutCache.this.visibleNodesChanged();
                }
                if (VariableHeightLayoutCache.this.treeSelectionModel != null) {
                    VariableHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
                }
            }
        }

        protected void collapse(boolean z2) {
            int preferredHeight;
            if (isExpanded()) {
                Enumeration enumerationPreorderEnumeration = preorderEnumeration();
                enumerationPreorderEnumeration.nextElement2();
                int i2 = 0;
                boolean zIsFixedRowHeight = VariableHeightLayoutCache.this.isFixedRowHeight();
                if (zIsFixedRowHeight) {
                    preferredHeight = 0;
                } else {
                    preferredHeight = getPreferredHeight() + getYOrigin();
                }
                int preferredHeight2 = getPreferredHeight();
                int i3 = preferredHeight;
                int row = getRow();
                if (!zIsFixedRowHeight) {
                    while (enumerationPreorderEnumeration.hasMoreElements()) {
                        TreeStateNode treeStateNode = (TreeStateNode) enumerationPreorderEnumeration.nextElement2();
                        if (treeStateNode.isVisible()) {
                            i2++;
                            preferredHeight = treeStateNode.getYOrigin() + treeStateNode.getPreferredHeight();
                        }
                    }
                } else {
                    while (enumerationPreorderEnumeration.hasMoreElements()) {
                        if (((TreeStateNode) enumerationPreorderEnumeration.nextElement2()).isVisible()) {
                            i2++;
                        }
                    }
                }
                for (int i4 = i2 + row; i4 > row; i4--) {
                    VariableHeightLayoutCache.this.visibleNodes.removeElementAt(i4);
                }
                this.expanded = false;
                if (row == -1) {
                    markSizeInvalid();
                } else if (z2) {
                    updatePreferredSize(row);
                }
                if (row != -1 && z2 && (i2 > 0 || preferredHeight2 != getPreferredHeight())) {
                    int preferredHeight3 = i3 + (getPreferredHeight() - preferredHeight2);
                    if (!zIsFixedRowHeight && row + 1 < VariableHeightLayoutCache.this.getRowCount() && preferredHeight3 != preferredHeight) {
                        int i5 = preferredHeight3 - preferredHeight;
                        int size = VariableHeightLayoutCache.this.visibleNodes.size();
                        for (int i6 = row + 1; i6 < size; i6++) {
                            ((TreeStateNode) VariableHeightLayoutCache.this.visibleNodes.elementAt(i6)).shiftYOriginBy(i5);
                        }
                    }
                    didAdjustTree();
                    VariableHeightLayoutCache.this.visibleNodesChanged();
                }
                if (VariableHeightLayoutCache.this.treeSelectionModel != null && i2 > 0 && row != -1) {
                    VariableHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
                }
            }
        }

        protected void removeFromMapping() {
            if (this.path != null) {
                VariableHeightLayoutCache.this.removeMapping(this);
                for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                    ((TreeStateNode) getChildAt(childCount)).removeFromMapping();
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/VariableHeightLayoutCache$VisibleTreeStateNodeEnumeration.class */
    private class VisibleTreeStateNodeEnumeration implements Enumeration<TreePath> {
        protected TreeStateNode parent;
        protected int nextIndex;
        protected int childCount;

        protected VisibleTreeStateNodeEnumeration(VariableHeightLayoutCache variableHeightLayoutCache, TreeStateNode treeStateNode) {
            this(treeStateNode, -1);
        }

        protected VisibleTreeStateNodeEnumeration(TreeStateNode treeStateNode, int i2) {
            this.parent = treeStateNode;
            this.nextIndex = i2;
            this.childCount = this.parent.getChildCount();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.parent != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public TreePath nextElement2() {
            TreePath treePath;
            if (!hasMoreElements()) {
                throw new NoSuchElementException("No more visible paths");
            }
            if (this.nextIndex == -1) {
                treePath = this.parent.getTreePath();
            } else {
                treePath = ((TreeStateNode) this.parent.getChildAt(this.nextIndex)).getTreePath();
            }
            updateNextObject();
            return treePath;
        }

        protected void updateNextObject() {
            if (!updateNextIndex()) {
                findNextValidParent();
            }
        }

        protected boolean findNextValidParent() {
            if (this.parent == VariableHeightLayoutCache.this.root) {
                this.parent = null;
                return false;
            }
            while (this.parent != null) {
                TreeStateNode treeStateNode = (TreeStateNode) this.parent.getParent();
                if (treeStateNode != null) {
                    this.nextIndex = treeStateNode.getIndex(this.parent);
                    this.parent = treeStateNode;
                    this.childCount = this.parent.getChildCount();
                    if (updateNextIndex()) {
                        return true;
                    }
                } else {
                    this.parent = null;
                }
            }
            return false;
        }

        protected boolean updateNextIndex() {
            if ((this.nextIndex == -1 && !this.parent.isExpanded()) || this.childCount == 0) {
                return false;
            }
            int i2 = this.nextIndex + 1;
            this.nextIndex = i2;
            if (i2 >= this.childCount) {
                return false;
            }
            TreeStateNode treeStateNode = (TreeStateNode) this.parent.getChildAt(this.nextIndex);
            if (treeStateNode != null && treeStateNode.isExpanded()) {
                this.parent = treeStateNode;
                this.nextIndex = -1;
                this.childCount = treeStateNode.getChildCount();
                return true;
            }
            return true;
        }
    }
}
