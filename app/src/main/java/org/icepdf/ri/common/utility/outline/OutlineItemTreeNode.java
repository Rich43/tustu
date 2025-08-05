package org.icepdf.ri.common.utility.outline;

import javax.swing.tree.DefaultMutableTreeNode;
import org.icepdf.core.pobjects.OutlineItem;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/outline/OutlineItemTreeNode.class */
public class OutlineItemTreeNode extends DefaultMutableTreeNode {
    private OutlineItem item;
    private boolean loadedChildren = false;

    public OutlineItemTreeNode(OutlineItem item) {
        this.item = item;
        setUserObject(item.getTitle());
    }

    public OutlineItem getOutlineItem() {
        return this.item;
    }

    public void recursivelyClearOutlineItems() {
        this.item = null;
        if (this.loadedChildren) {
            int count = getChildCount();
            for (int i2 = 0; i2 < count; i2++) {
                OutlineItemTreeNode node = (OutlineItemTreeNode) getChildAt(i2);
                node.recursivelyClearOutlineItems();
            }
        }
    }

    @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
    public int getChildCount() {
        ensureChildrenLoaded();
        return super.getChildCount();
    }

    private void ensureChildrenLoaded() {
        if (!this.loadedChildren) {
            this.loadedChildren = true;
            int count = this.item.getSubItemCount();
            for (int i2 = 0; i2 < count; i2++) {
                OutlineItem child = this.item.getSubItem(i2);
                OutlineItemTreeNode childTreeNode = new OutlineItemTreeNode(child);
                add(childTreeNode);
            }
        }
    }
}
