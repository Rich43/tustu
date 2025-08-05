package javax.swing.tree;

import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Stack;
import javax.swing.event.TreeModelEvent;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/tree/FixedHeightLayoutCache.class */
public class FixedHeightLayoutCache extends AbstractLayoutCache {
    private FHTreeStateNode root;
    private int rowCount;
    private Stack<Stack<TreePath>> tempStacks = new Stack<>();
    private Rectangle boundsBuffer = new Rectangle();
    private Hashtable<TreePath, FHTreeStateNode> treePathMapping = new Hashtable<>();
    private SearchInfo info = new SearchInfo();

    public FixedHeightLayoutCache() {
        setRowHeight(1);
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setModel(TreeModel treeModel) {
        super.setModel(treeModel);
        rebuild(false);
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setRootVisible(boolean z2) {
        if (isRootVisible() != z2) {
            super.setRootVisible(z2);
            if (this.root != null) {
                if (z2) {
                    this.rowCount++;
                    this.root.adjustRowBy(1);
                } else {
                    this.rowCount--;
                    this.root.adjustRowBy(-1);
                }
                visibleNodesChanged();
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setRowHeight(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("FixedHeightLayoutCache only supports row heights greater than 0");
        }
        if (getRowHeight() != i2) {
            super.setRowHeight(i2);
            visibleNodesChanged();
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getRowCount() {
        return this.rowCount;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void invalidatePathBounds(TreePath treePath) {
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void invalidateSizes() {
        visibleNodesChanged();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public boolean isExpanded(TreePath treePath) {
        FHTreeStateNode nodeForPath;
        return (treePath == null || (nodeForPath = getNodeForPath(treePath, true, false)) == null || !nodeForPath.isExpanded()) ? false : true;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public Rectangle getBounds(TreePath treePath, Rectangle rectangle) {
        int indexOfChild;
        if (treePath == null) {
            return null;
        }
        FHTreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            return getBounds(nodeForPath, -1, rectangle);
        }
        TreePath parentPath = treePath.getParentPath();
        FHTreeStateNode nodeForPath2 = getNodeForPath(parentPath, true, false);
        if (nodeForPath2 != null && nodeForPath2.isExpanded() && (indexOfChild = this.treeModel.getIndexOfChild(parentPath.getLastPathComponent(), treePath.getLastPathComponent())) != -1) {
            return getBounds(nodeForPath2, indexOfChild, rectangle);
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public TreePath getPathForRow(int i2) {
        if (i2 >= 0 && i2 < getRowCount() && this.root.getPathForRow(i2, getRowCount(), this.info)) {
            return this.info.getPath();
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getRowForPath(TreePath treePath) {
        if (treePath == null || this.root == null) {
            return -1;
        }
        FHTreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            return nodeForPath.getRow();
        }
        TreePath parentPath = treePath.getParentPath();
        FHTreeStateNode nodeForPath2 = getNodeForPath(parentPath, true, false);
        if (nodeForPath2 != null && nodeForPath2.isExpanded()) {
            return nodeForPath2.getRowToModelIndex(this.treeModel.getIndexOfChild(parentPath.getLastPathComponent(), treePath.getLastPathComponent()));
        }
        return -1;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public TreePath getPathClosestTo(int i2, int i3) {
        if (getRowCount() == 0) {
            return null;
        }
        return getPathForRow(getRowContainingYLocation(i3));
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public int getVisibleChildCount(TreePath treePath) {
        FHTreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath == null) {
            return 0;
        }
        return nodeForPath.getTotalChildCount();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public Enumeration<TreePath> getVisiblePathsFrom(TreePath treePath) {
        if (treePath == null) {
            return null;
        }
        FHTreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        if (nodeForPath != null) {
            return new VisibleFHTreeStateNodeEnumeration(this, nodeForPath);
        }
        TreePath parentPath = treePath.getParentPath();
        FHTreeStateNode nodeForPath2 = getNodeForPath(parentPath, true, false);
        if (nodeForPath2 != null && nodeForPath2.isExpanded()) {
            return new VisibleFHTreeStateNodeEnumeration(nodeForPath2, this.treeModel.getIndexOfChild(parentPath.getLastPathComponent(), treePath.getLastPathComponent()));
        }
        return null;
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void setExpandedState(TreePath treePath, boolean z2) {
        FHTreeStateNode nodeForPath;
        if (z2) {
            ensurePathIsExpanded(treePath, true);
            return;
        }
        if (treePath != null) {
            TreePath parentPath = treePath.getParentPath();
            if (parentPath != null && (nodeForPath = getNodeForPath(parentPath, false, true)) != null) {
                nodeForPath.makeVisible();
            }
            FHTreeStateNode nodeForPath2 = getNodeForPath(treePath, true, false);
            if (nodeForPath2 != null) {
                nodeForPath2.collapse(true);
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public boolean getExpandedState(TreePath treePath) {
        FHTreeStateNode nodeForPath = getNodeForPath(treePath, true, false);
        return nodeForPath != null && nodeForPath.isVisible() && nodeForPath.isExpanded();
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesChanged(TreeModelEvent treeModelEvent) {
        int length;
        if (treeModelEvent != null) {
            FHTreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            int[] childIndices = treeModelEvent.getChildIndices();
            if (nodeForPath != null) {
                if (childIndices != null && (length = childIndices.length) > 0) {
                    Object userObject = nodeForPath.getUserObject();
                    for (int i2 = 0; i2 < length; i2++) {
                        FHTreeStateNode childAtModelIndex = nodeForPath.getChildAtModelIndex(childIndices[i2]);
                        if (childAtModelIndex != null) {
                            childAtModelIndex.setUserObject(this.treeModel.getChild(userObject, childIndices[i2]));
                        }
                    }
                    if (nodeForPath.isVisible() && nodeForPath.isExpanded()) {
                        visibleNodesChanged();
                        return;
                    }
                    return;
                }
                if (nodeForPath == this.root && nodeForPath.isVisible() && nodeForPath.isExpanded()) {
                    visibleNodesChanged();
                }
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesInserted(TreeModelEvent treeModelEvent) {
        if (treeModelEvent != null) {
            FHTreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            int[] childIndices = treeModelEvent.getChildIndices();
            if (nodeForPath != null && childIndices != null && (childIndices.length) > 0) {
                boolean z2 = nodeForPath.isVisible() && nodeForPath.isExpanded();
                for (int i2 : childIndices) {
                    nodeForPath.childInsertedAtModelIndex(i2, z2);
                }
                if (z2 && this.treeSelectionModel != null) {
                    this.treeSelectionModel.resetRowSelection();
                }
                if (nodeForPath.isVisible()) {
                    visibleNodesChanged();
                }
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
        int length;
        if (treeModelEvent != null) {
            FHTreeStateNode nodeForPath = getNodeForPath(SwingUtilities2.getTreePath(treeModelEvent, getModel()), false, false);
            int[] childIndices = treeModelEvent.getChildIndices();
            if (nodeForPath != null && childIndices != null && (length = childIndices.length) > 0) {
                treeModelEvent.getChildren();
                boolean z2 = nodeForPath.isVisible() && nodeForPath.isExpanded();
                for (int i2 = length - 1; i2 >= 0; i2--) {
                    nodeForPath.removeChildAtModelIndex(childIndices[i2], z2);
                }
                if (z2) {
                    if (this.treeSelectionModel != null) {
                        this.treeSelectionModel.resetRowSelection();
                    }
                    if (this.treeModel.getChildCount(nodeForPath.getUserObject()) == 0 && nodeForPath.isLeaf()) {
                        nodeForPath.collapse(false);
                    }
                    visibleNodesChanged();
                    return;
                }
                if (nodeForPath.isVisible()) {
                    visibleNodesChanged();
                }
            }
        }
    }

    @Override // javax.swing.tree.AbstractLayoutCache
    public void treeStructureChanged(TreeModelEvent treeModelEvent) {
        if (treeModelEvent != null) {
            TreePath treePath = SwingUtilities2.getTreePath(treeModelEvent, getModel());
            FHTreeStateNode nodeForPath = getNodeForPath(treePath, false, false);
            if (nodeForPath == this.root || (nodeForPath == null && ((treePath == null && this.treeModel != null && this.treeModel.getRoot() == null) || (treePath != null && treePath.getPathCount() <= 1)))) {
                rebuild(true);
                return;
            }
            if (nodeForPath != null) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) nodeForPath.getParent();
                boolean zIsExpanded = nodeForPath.isExpanded();
                boolean zIsVisible = nodeForPath.isVisible();
                int index = fHTreeStateNode.getIndex(nodeForPath);
                nodeForPath.collapse(false);
                fHTreeStateNode.remove(index);
                if (zIsVisible && zIsExpanded) {
                    fHTreeStateNode.resetChildrenRowsFrom(nodeForPath.getRow(), index, nodeForPath.getChildIndex());
                    getNodeForPath(treePath, false, true).expand();
                }
                if (this.treeSelectionModel != null && zIsVisible && zIsExpanded) {
                    this.treeSelectionModel.resetRowSelection();
                }
                if (zIsVisible) {
                    visibleNodesChanged();
                }
            }
        }
    }

    private void visibleNodesChanged() {
    }

    private Rectangle getBounds(FHTreeStateNode fHTreeStateNode, int i2, Rectangle rectangle) {
        int rowToModelIndex;
        Object child;
        boolean zIsExpanded;
        int level;
        if (i2 == -1) {
            rowToModelIndex = fHTreeStateNode.getRow();
            child = fHTreeStateNode.getUserObject();
            zIsExpanded = fHTreeStateNode.isExpanded();
            level = fHTreeStateNode.getLevel();
        } else {
            rowToModelIndex = fHTreeStateNode.getRowToModelIndex(i2);
            child = this.treeModel.getChild(fHTreeStateNode.getUserObject(), i2);
            zIsExpanded = false;
            level = fHTreeStateNode.getLevel() + 1;
        }
        Rectangle nodeDimensions = getNodeDimensions(child, rowToModelIndex, level, zIsExpanded, this.boundsBuffer);
        if (nodeDimensions == null) {
            return null;
        }
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        rectangle.f12372x = nodeDimensions.f12372x;
        rectangle.height = getRowHeight();
        rectangle.f12373y = rowToModelIndex * rectangle.height;
        rectangle.width = nodeDimensions.width;
        return rectangle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustRowCountBy(int i2) {
        this.rowCount += i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMapping(FHTreeStateNode fHTreeStateNode) {
        this.treePathMapping.put(fHTreeStateNode.getTreePath(), fHTreeStateNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeMapping(FHTreeStateNode fHTreeStateNode) {
        this.treePathMapping.remove(fHTreeStateNode.getTreePath());
    }

    private FHTreeStateNode getMapping(TreePath treePath) {
        return this.treePathMapping.get(treePath);
    }

    private void rebuild(boolean z2) {
        Object root;
        this.treePathMapping.clear();
        if (this.treeModel != null && (root = this.treeModel.getRoot()) != null) {
            this.root = createNodeForValue(root, 0);
            this.root.path = new TreePath(root);
            addMapping(this.root);
            if (isRootVisible()) {
                this.rowCount = 1;
                this.root.row = 0;
            } else {
                this.rowCount = 0;
                this.root.row = -1;
            }
            this.root.expand();
        } else {
            this.root = null;
            this.rowCount = 0;
        }
        if (z2 && this.treeSelectionModel != null) {
            this.treeSelectionModel.clearSelection();
        }
        visibleNodesChanged();
    }

    private int getRowContainingYLocation(int i2) {
        if (getRowCount() == 0) {
            return -1;
        }
        return Math.max(0, Math.min(getRowCount() - 1, i2 / getRowHeight()));
    }

    private boolean ensurePathIsExpanded(TreePath treePath, boolean z2) {
        FHTreeStateNode nodeForPath;
        if (treePath != null) {
            if (this.treeModel.isLeaf(treePath.getLastPathComponent())) {
                treePath = treePath.getParentPath();
                z2 = true;
            }
            if (treePath != null && (nodeForPath = getNodeForPath(treePath, false, true)) != null) {
                nodeForPath.makeVisible();
                if (z2) {
                    nodeForPath.expand();
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FHTreeStateNode createNodeForValue(Object obj, int i2) {
        return new FHTreeStateNode(obj, i2, -1);
    }

    /* JADX WARN: Finally extract failed */
    private FHTreeStateNode getNodeForPath(TreePath treePath, boolean z2, boolean z3) {
        Stack<TreePath> stackPop;
        if (treePath != null) {
            FHTreeStateNode mapping = getMapping(treePath);
            if (mapping != null) {
                if (z2 && !mapping.isVisible()) {
                    return null;
                }
                return mapping;
            }
            if (z2) {
                return null;
            }
            if (this.tempStacks.size() == 0) {
                stackPop = new Stack<>();
            } else {
                stackPop = this.tempStacks.pop();
            }
            try {
                stackPop.push(treePath);
                for (TreePath parentPath = treePath.getParentPath(); parentPath != null; parentPath = parentPath.getParentPath()) {
                    FHTreeStateNode mapping2 = getMapping(parentPath);
                    if (mapping2 != null) {
                        while (mapping2 != null) {
                            if (stackPop.size() <= 0) {
                                break;
                            }
                            mapping2 = mapping2.createChildFor(stackPop.pop().getLastPathComponent());
                        }
                        FHTreeStateNode fHTreeStateNode = mapping2;
                        stackPop.removeAllElements();
                        this.tempStacks.push(stackPop);
                        return fHTreeStateNode;
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

    /* loaded from: rt.jar:javax/swing/tree/FixedHeightLayoutCache$FHTreeStateNode.class */
    private class FHTreeStateNode extends DefaultMutableTreeNode {
        protected boolean isExpanded;
        protected int childIndex;
        protected int childCount;
        protected int row;
        protected TreePath path;

        public FHTreeStateNode(Object obj, int i2, int i3) {
            super(obj);
            this.childIndex = i2;
            this.row = i3;
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void setParent(MutableTreeNode mutableTreeNode) {
            super.setParent(mutableTreeNode);
            if (mutableTreeNode != null) {
                this.path = ((FHTreeStateNode) mutableTreeNode).getTreePath().pathByAddingChild(getUserObject());
                FixedHeightLayoutCache.this.addMapping(this);
            }
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void remove(int i2) {
            ((FHTreeStateNode) getChildAt(i2)).removeFromMapping();
            super.remove(i2);
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.MutableTreeNode
        public void setUserObject(Object obj) {
            super.setUserObject(obj);
            if (this.path != null) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
                if (fHTreeStateNode != null) {
                    resetChildrenPaths(fHTreeStateNode.getTreePath());
                } else {
                    resetChildrenPaths(null);
                }
            }
        }

        public int getChildIndex() {
            return this.childIndex;
        }

        public TreePath getTreePath() {
            return this.path;
        }

        public FHTreeStateNode getChildAtModelIndex(int i2) {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                if (((FHTreeStateNode) getChildAt(childCount)).childIndex == i2) {
                    return (FHTreeStateNode) getChildAt(childCount);
                }
            }
            return null;
        }

        public boolean isVisible() {
            FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
            if (fHTreeStateNode == null) {
                return true;
            }
            return fHTreeStateNode.isExpanded() && fHTreeStateNode.isVisible();
        }

        public int getRow() {
            return this.row;
        }

        public int getRowToModelIndex(int i2) {
            int row = getRow() + 1;
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getChildAt(i3);
                if (fHTreeStateNode.childIndex >= i2) {
                    if (fHTreeStateNode.childIndex == i2) {
                        return fHTreeStateNode.row;
                    }
                    if (i3 == 0) {
                        return getRow() + 1 + i2;
                    }
                    return fHTreeStateNode.row - (fHTreeStateNode.childIndex - i2);
                }
            }
            return ((getRow() + 1) + getTotalChildCount()) - (this.childCount - i2);
        }

        public int getTotalChildCount() {
            if (isExpanded()) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
                if (fHTreeStateNode != null) {
                    int index = fHTreeStateNode.getIndex(this);
                    if (index + 1 < fHTreeStateNode.getChildCount()) {
                        FHTreeStateNode fHTreeStateNode2 = (FHTreeStateNode) fHTreeStateNode.getChildAt(index + 1);
                        return (fHTreeStateNode2.row - this.row) - (fHTreeStateNode2.childIndex - this.childIndex);
                    }
                }
                int totalChildCount = this.childCount;
                for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                    totalChildCount += ((FHTreeStateNode) getChildAt(childCount)).getTotalChildCount();
                }
                return totalChildCount;
            }
            return 0;
        }

        public boolean isExpanded() {
            return this.isExpanded;
        }

        public int getVisibleLevel() {
            if (FixedHeightLayoutCache.this.isRootVisible()) {
                return getLevel();
            }
            return getLevel() - 1;
        }

        protected void resetChildrenPaths(TreePath treePath) {
            FixedHeightLayoutCache.this.removeMapping(this);
            if (treePath == null) {
                this.path = new TreePath(getUserObject());
            } else {
                this.path = treePath.pathByAddingChild(getUserObject());
            }
            FixedHeightLayoutCache.this.addMapping(this);
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                ((FHTreeStateNode) getChildAt(childCount)).resetChildrenPaths(this.path);
            }
        }

        protected void removeFromMapping() {
            if (this.path != null) {
                FixedHeightLayoutCache.this.removeMapping(this);
                for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                    ((FHTreeStateNode) getChildAt(childCount)).removeFromMapping();
                }
            }
        }

        protected FHTreeStateNode createChildFor(Object obj) {
            int rowToModelIndex;
            int indexOfChild = FixedHeightLayoutCache.this.treeModel.getIndexOfChild(getUserObject(), obj);
            if (indexOfChild >= 0) {
                FHTreeStateNode fHTreeStateNodeCreateNodeForValue = FixedHeightLayoutCache.this.createNodeForValue(obj, indexOfChild);
                if (isVisible()) {
                    rowToModelIndex = getRowToModelIndex(indexOfChild);
                } else {
                    rowToModelIndex = -1;
                }
                fHTreeStateNodeCreateNodeForValue.row = rowToModelIndex;
                int childCount = getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    if (((FHTreeStateNode) getChildAt(i2)).childIndex > indexOfChild) {
                        insert(fHTreeStateNodeCreateNodeForValue, i2);
                        return fHTreeStateNodeCreateNodeForValue;
                    }
                }
                add(fHTreeStateNodeCreateNodeForValue);
                return fHTreeStateNodeCreateNodeForValue;
            }
            return null;
        }

        protected void adjustRowBy(int i2) {
            this.row += i2;
            if (this.isExpanded) {
                for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                    ((FHTreeStateNode) getChildAt(childCount)).adjustRowBy(i2);
                }
            }
        }

        protected void adjustRowBy(int i2, int i3) {
            if (this.isExpanded) {
                for (int childCount = getChildCount() - 1; childCount >= i3; childCount--) {
                    ((FHTreeStateNode) getChildAt(childCount)).adjustRowBy(i2);
                }
            }
            FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
            if (fHTreeStateNode != null) {
                fHTreeStateNode.adjustRowBy(i2, fHTreeStateNode.getIndex(this) + 1);
            }
        }

        protected void didExpand() {
            int rowAndChildren = setRowAndChildren(this.row);
            FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
            int i2 = (rowAndChildren - this.row) - 1;
            if (fHTreeStateNode != null) {
                fHTreeStateNode.adjustRowBy(i2, fHTreeStateNode.getIndex(this) + 1);
            }
            FixedHeightLayoutCache.this.adjustRowCountBy(i2);
        }

        protected int setRowAndChildren(int i2) {
            this.row = i2;
            if (!isExpanded()) {
                return this.row + 1;
            }
            int rowAndChildren = this.row + 1;
            int i3 = 0;
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getChildAt(i4);
                int i5 = rowAndChildren + (fHTreeStateNode.childIndex - i3);
                i3 = fHTreeStateNode.childIndex + 1;
                if (fHTreeStateNode.isExpanded) {
                    rowAndChildren = fHTreeStateNode.setRowAndChildren(i5);
                } else {
                    rowAndChildren = i5 + 1;
                    fHTreeStateNode.row = i5;
                }
            }
            return (rowAndChildren + this.childCount) - i3;
        }

        protected void resetChildrenRowsFrom(int i2, int i3, int i4) {
            int rowAndChildren = i2;
            int i5 = i4;
            int childCount = getChildCount();
            for (int i6 = i3; i6 < childCount; i6++) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getChildAt(i6);
                int i7 = rowAndChildren + (fHTreeStateNode.childIndex - i5);
                i5 = fHTreeStateNode.childIndex + 1;
                if (fHTreeStateNode.isExpanded) {
                    rowAndChildren = fHTreeStateNode.setRowAndChildren(i7);
                } else {
                    rowAndChildren = i7 + 1;
                    fHTreeStateNode.row = i7;
                }
            }
            int i8 = rowAndChildren + (this.childCount - i5);
            FHTreeStateNode fHTreeStateNode2 = (FHTreeStateNode) getParent();
            if (fHTreeStateNode2 == null) {
                FixedHeightLayoutCache.this.rowCount = i8;
            } else {
                fHTreeStateNode2.resetChildrenRowsFrom(i8, fHTreeStateNode2.getIndex(this) + 1, this.childIndex + 1);
            }
        }

        protected void makeVisible() {
            FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
            if (fHTreeStateNode != null) {
                fHTreeStateNode.expandParentAndReceiver();
            }
        }

        protected void expandParentAndReceiver() {
            FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getParent();
            if (fHTreeStateNode != null) {
                fHTreeStateNode.expandParentAndReceiver();
            }
            expand();
        }

        protected void expand() {
            if (!this.isExpanded && !isLeaf()) {
                boolean zIsVisible = isVisible();
                this.isExpanded = true;
                this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(getUserObject());
                if (zIsVisible) {
                    didExpand();
                }
                if (zIsVisible && FixedHeightLayoutCache.this.treeSelectionModel != null) {
                    FixedHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
                }
            }
        }

        protected void collapse(boolean z2) {
            if (this.isExpanded) {
                if (isVisible() && z2) {
                    int totalChildCount = getTotalChildCount();
                    this.isExpanded = false;
                    FixedHeightLayoutCache.this.adjustRowCountBy(-totalChildCount);
                    adjustRowBy(-totalChildCount, 0);
                } else {
                    this.isExpanded = false;
                }
                if (z2 && isVisible() && FixedHeightLayoutCache.this.treeSelectionModel != null) {
                    FixedHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
                }
            }
        }

        @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
        public boolean isLeaf() {
            TreeModel model = FixedHeightLayoutCache.this.getModel();
            if (model != null) {
                return model.isLeaf(getUserObject());
            }
            return true;
        }

        protected void addNode(FHTreeStateNode fHTreeStateNode) {
            boolean z2 = false;
            int childIndex = fHTreeStateNode.getChildIndex();
            int i2 = 0;
            int childCount = getChildCount();
            while (i2 < childCount) {
                if (((FHTreeStateNode) getChildAt(i2)).getChildIndex() > childIndex) {
                    z2 = true;
                    insert(fHTreeStateNode, i2);
                    i2 = childCount;
                }
                i2++;
            }
            if (!z2) {
                add(fHTreeStateNode);
            }
        }

        protected void removeChildAtModelIndex(int i2, boolean z2) {
            FHTreeStateNode childAtModelIndex = getChildAtModelIndex(i2);
            if (childAtModelIndex != null) {
                int row = childAtModelIndex.getRow();
                int index = getIndex(childAtModelIndex);
                childAtModelIndex.collapse(false);
                remove(index);
                adjustChildIndexs(index, -1);
                this.childCount--;
                if (z2) {
                    resetChildrenRowsFrom(row, index, i2);
                    return;
                }
                return;
            }
            int childCount = getChildCount();
            int i3 = 0;
            while (i3 < childCount) {
                if (((FHTreeStateNode) getChildAt(i3)).childIndex < i2) {
                    i3++;
                } else {
                    if (z2) {
                        adjustRowBy(-1, i3);
                        FixedHeightLayoutCache.this.adjustRowCountBy(-1);
                    }
                    while (i3 < childCount) {
                        ((FHTreeStateNode) getChildAt(i3)).childIndex--;
                        i3++;
                    }
                    this.childCount--;
                    return;
                }
            }
            if (z2) {
                adjustRowBy(-1, childCount);
                FixedHeightLayoutCache.this.adjustRowCountBy(-1);
            }
            this.childCount--;
        }

        protected void adjustChildIndexs(int i2, int i3) {
            int childCount = getChildCount();
            for (int i4 = i2; i4 < childCount; i4++) {
                ((FHTreeStateNode) getChildAt(i4)).childIndex += i3;
            }
        }

        protected void childInsertedAtModelIndex(int i2, boolean z2) {
            int childCount = getChildCount();
            int i3 = 0;
            while (i3 < childCount) {
                if (((FHTreeStateNode) getChildAt(i3)).childIndex < i2) {
                    i3++;
                } else {
                    if (z2) {
                        adjustRowBy(1, i3);
                        FixedHeightLayoutCache.this.adjustRowCountBy(1);
                    }
                    while (i3 < childCount) {
                        ((FHTreeStateNode) getChildAt(i3)).childIndex++;
                        i3++;
                    }
                    this.childCount++;
                    return;
                }
            }
            if (z2) {
                adjustRowBy(1, childCount);
                FixedHeightLayoutCache.this.adjustRowCountBy(1);
            }
            this.childCount++;
        }

        protected boolean getPathForRow(int i2, int i3, SearchInfo searchInfo) {
            if (this.row == i2) {
                searchInfo.node = this;
                searchInfo.isNodeParentNode = false;
                searchInfo.childIndex = this.childIndex;
                return true;
            }
            FHTreeStateNode fHTreeStateNode = null;
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                FHTreeStateNode fHTreeStateNode2 = (FHTreeStateNode) getChildAt(i4);
                if (fHTreeStateNode2.row > i2) {
                    if (i4 == 0) {
                        searchInfo.node = this;
                        searchInfo.isNodeParentNode = true;
                        searchInfo.childIndex = (i2 - this.row) - 1;
                        return true;
                    }
                    int i5 = (1 + fHTreeStateNode2.row) - (fHTreeStateNode2.childIndex - fHTreeStateNode.childIndex);
                    if (i2 < i5) {
                        return fHTreeStateNode.getPathForRow(i2, i5, searchInfo);
                    }
                    searchInfo.node = this;
                    searchInfo.isNodeParentNode = true;
                    searchInfo.childIndex = (i2 - i5) + fHTreeStateNode.childIndex + 1;
                    return true;
                }
                fHTreeStateNode = fHTreeStateNode2;
            }
            if (fHTreeStateNode != null) {
                int i6 = (i3 - (this.childCount - fHTreeStateNode.childIndex)) + 1;
                if (i2 < i6) {
                    return fHTreeStateNode.getPathForRow(i2, i6, searchInfo);
                }
                searchInfo.node = this;
                searchInfo.isNodeParentNode = true;
                searchInfo.childIndex = (i2 - i6) + fHTreeStateNode.childIndex + 1;
                return true;
            }
            int i7 = (i2 - this.row) - 1;
            if (i7 >= this.childCount) {
                return false;
            }
            searchInfo.node = this;
            searchInfo.isNodeParentNode = true;
            searchInfo.childIndex = i7;
            return true;
        }

        protected int getCountTo(int i2) {
            int totalChildCount = i2 + 1;
            int i3 = 0;
            int childCount = getChildCount();
            while (i3 < childCount) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getChildAt(i3);
                if (fHTreeStateNode.childIndex >= i2) {
                    i3 = childCount;
                } else {
                    totalChildCount += fHTreeStateNode.getTotalChildCount();
                }
                i3++;
            }
            if (this.parent != null) {
                return totalChildCount + ((FHTreeStateNode) getParent()).getCountTo(this.childIndex);
            }
            if (!FixedHeightLayoutCache.this.isRootVisible()) {
                return totalChildCount - 1;
            }
            return totalChildCount;
        }

        protected int getNumExpandedChildrenTo(int i2) {
            int totalChildCount = i2;
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) getChildAt(i3);
                if (fHTreeStateNode.childIndex >= i2) {
                    return totalChildCount;
                }
                totalChildCount += fHTreeStateNode.getTotalChildCount();
            }
            return totalChildCount;
        }

        protected void didAdjustTree() {
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/FixedHeightLayoutCache$SearchInfo.class */
    private class SearchInfo {
        protected FHTreeStateNode node;
        protected boolean isNodeParentNode;
        protected int childIndex;

        private SearchInfo() {
        }

        protected TreePath getPath() {
            if (this.node == null) {
                return null;
            }
            if (this.isNodeParentNode) {
                return this.node.getTreePath().pathByAddingChild(FixedHeightLayoutCache.this.treeModel.getChild(this.node.getUserObject(), this.childIndex));
            }
            return this.node.path;
        }
    }

    /* loaded from: rt.jar:javax/swing/tree/FixedHeightLayoutCache$VisibleFHTreeStateNodeEnumeration.class */
    private class VisibleFHTreeStateNodeEnumeration implements Enumeration<TreePath> {
        protected FHTreeStateNode parent;
        protected int nextIndex;
        protected int childCount;

        protected VisibleFHTreeStateNodeEnumeration(FixedHeightLayoutCache fixedHeightLayoutCache, FHTreeStateNode fHTreeStateNode) {
            this(fHTreeStateNode, -1);
        }

        protected VisibleFHTreeStateNodeEnumeration(FHTreeStateNode fHTreeStateNode, int i2) {
            this.parent = fHTreeStateNode;
            this.nextIndex = i2;
            this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(this.parent.getUserObject());
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
                FHTreeStateNode childAtModelIndex = this.parent.getChildAtModelIndex(this.nextIndex);
                if (childAtModelIndex == null) {
                    treePath = this.parent.getTreePath().pathByAddingChild(FixedHeightLayoutCache.this.treeModel.getChild(this.parent.getUserObject(), this.nextIndex));
                } else {
                    treePath = childAtModelIndex.getTreePath();
                }
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
            if (this.parent == FixedHeightLayoutCache.this.root) {
                this.parent = null;
                return false;
            }
            while (this.parent != null) {
                FHTreeStateNode fHTreeStateNode = (FHTreeStateNode) this.parent.getParent();
                if (fHTreeStateNode != null) {
                    this.nextIndex = this.parent.childIndex;
                    this.parent = fHTreeStateNode;
                    this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(this.parent.getUserObject());
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
            FHTreeStateNode childAtModelIndex = this.parent.getChildAtModelIndex(this.nextIndex);
            if (childAtModelIndex != null && childAtModelIndex.isExpanded()) {
                this.parent = childAtModelIndex;
                this.nextIndex = -1;
                this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(childAtModelIndex.getUserObject());
                return true;
            }
            return true;
        }
    }
}
