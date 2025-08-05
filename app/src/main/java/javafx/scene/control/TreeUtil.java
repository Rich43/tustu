package javafx.scene.control;

import java.util.List;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeUtil.class */
class TreeUtil {
    TreeUtil() {
    }

    static <T> int getExpandedDescendantCount(TreeItem<T> node, boolean treeItemCountDirty) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            return 1;
        }
        return node.getExpandedDescendentCount(treeItemCountDirty);
    }

    static int updateExpandedItemCount(TreeItem treeItem, boolean treeItemCountDirty, boolean isShowRoot) {
        if (treeItem == null) {
            return 0;
        }
        if (!treeItem.isExpanded()) {
            return 1;
        }
        int count = getExpandedDescendantCount(treeItem, treeItemCountDirty);
        if (!isShowRoot) {
            count--;
        }
        return count;
    }

    static <T> TreeItem<T> getItem(TreeItem<T> parent, int itemIndex, boolean treeItemCountDirty) {
        List<TreeItem<T>> children;
        if (parent == null) {
            return null;
        }
        if (itemIndex == 0) {
            return parent;
        }
        if (itemIndex >= getExpandedDescendantCount(parent, treeItemCountDirty) || (children = parent.getChildren()) == null) {
            return null;
        }
        int idx = itemIndex - 1;
        int max = children.size();
        for (int i2 = 0; i2 < max; i2++) {
            TreeItem<T> child = children.get(i2);
            if (idx == 0) {
                return child;
            }
            if (child.isLeaf() || !child.isExpanded()) {
                idx--;
            } else {
                int expandedChildCount = getExpandedDescendantCount(child, treeItemCountDirty);
                if (idx >= expandedChildCount) {
                    idx -= expandedChildCount;
                } else {
                    TreeItem<T> result = getItem(child, idx, treeItemCountDirty);
                    if (result != null) {
                        return result;
                    }
                    idx--;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a9, code lost:
    
        if (r10 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00ae, code lost:
    
        if (r8 == 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b3, code lost:
    
        if (r13 == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00b6, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00bb, code lost:
    
        if (r7 == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00c7, code lost:
    
        return r8 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:?, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static <T> int getRow(javafx.scene.control.TreeItem<T> r4, javafx.scene.control.TreeItem<T> r5, boolean r6, boolean r7) {
        /*
            r0 = r4
            if (r0 != 0) goto L6
            r0 = -1
            return r0
        L6:
            r0 = r7
            if (r0 == 0) goto L14
            r0 = r4
            r1 = r5
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L14
            r0 = 0
            return r0
        L14:
            r0 = 0
            r8 = r0
            r0 = r4
            r9 = r0
            r0 = r4
            javafx.scene.control.TreeItem r0 = r0.getParent()
            r10 = r0
            r0 = 0
            r13 = r0
        L23:
            r0 = r9
            r1 = r5
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto La7
            r0 = r10
            if (r0 == 0) goto La7
            r0 = r10
            boolean r0 = r0.isExpanded()
            if (r0 != 0) goto L3f
            r0 = 1
            r13 = r0
            goto La7
        L3f:
            r0 = r10
            javafx.collections.ObservableList<javafx.scene.control.TreeItem<T>> r0 = r0.children
            r12 = r0
            r0 = r12
            r1 = r9
            int r0 = r0.indexOf(r1)
            r14 = r0
            r0 = r14
            r1 = 1
            int r0 = r0 - r1
            r15 = r0
        L57:
            r0 = r15
            r1 = -1
            if (r0 <= r1) goto L96
            r0 = r12
            r1 = r15
            java.lang.Object r0 = r0.get(r1)
            javafx.scene.control.TreeItem r0 = (javafx.scene.control.TreeItem) r0
            r11 = r0
            r0 = r11
            if (r0 != 0) goto L73
            goto L90
        L73:
            r0 = r8
            r1 = r11
            r2 = r6
            int r1 = getExpandedDescendantCount(r1, r2)
            int r0 = r0 + r1
            r8 = r0
            r0 = r11
            r1 = r5
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L90
            r0 = r7
            if (r0 != 0) goto L8d
            r0 = -1
            return r0
        L8d:
            r0 = r8
            return r0
        L90:
            int r15 = r15 + (-1)
            goto L57
        L96:
            r0 = r10
            r9 = r0
            r0 = r10
            javafx.scene.control.TreeItem r0 = r0.getParent()
            r10 = r0
            int r8 = r8 + 1
            goto L23
        La7:
            r0 = r10
            if (r0 != 0) goto Lb1
            r0 = r8
            if (r0 == 0) goto Lb6
        Lb1:
            r0 = r13
            if (r0 == 0) goto Lba
        Lb6:
            r0 = -1
            goto Lc7
        Lba:
            r0 = r7
            if (r0 == 0) goto Lc3
            r0 = r8
            goto Lc7
        Lc3:
            r0 = r8
            r1 = 1
            int r0 = r0 - r1
        Lc7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.TreeUtil.getRow(javafx.scene.control.TreeItem, javafx.scene.control.TreeItem, boolean, boolean):int");
    }
}
