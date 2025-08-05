package javax.swing.event;

import java.util.EventObject;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/event/TreeSelectionEvent.class */
public class TreeSelectionEvent extends EventObject {
    protected TreePath[] paths;
    protected boolean[] areNew;
    protected TreePath oldLeadSelectionPath;
    protected TreePath newLeadSelectionPath;

    public TreeSelectionEvent(Object obj, TreePath[] treePathArr, boolean[] zArr, TreePath treePath, TreePath treePath2) {
        super(obj);
        this.paths = treePathArr;
        this.areNew = zArr;
        this.oldLeadSelectionPath = treePath;
        this.newLeadSelectionPath = treePath2;
    }

    public TreeSelectionEvent(Object obj, TreePath treePath, boolean z2, TreePath treePath2, TreePath treePath3) {
        super(obj);
        this.paths = new TreePath[1];
        this.paths[0] = treePath;
        this.areNew = new boolean[1];
        this.areNew[0] = z2;
        this.oldLeadSelectionPath = treePath2;
        this.newLeadSelectionPath = treePath3;
    }

    public TreePath[] getPaths() {
        int length = this.paths.length;
        TreePath[] treePathArr = new TreePath[length];
        System.arraycopy(this.paths, 0, treePathArr, 0, length);
        return treePathArr;
    }

    public TreePath getPath() {
        return this.paths[0];
    }

    public boolean isAddedPath() {
        return this.areNew[0];
    }

    public boolean isAddedPath(TreePath treePath) {
        for (int length = this.paths.length - 1; length >= 0; length--) {
            if (this.paths[length].equals(treePath)) {
                return this.areNew[length];
            }
        }
        throw new IllegalArgumentException("path is not a path identified by the TreeSelectionEvent");
    }

    public boolean isAddedPath(int i2) {
        if (this.paths == null || i2 < 0 || i2 >= this.paths.length) {
            throw new IllegalArgumentException("index is beyond range of added paths identified by TreeSelectionEvent");
        }
        return this.areNew[i2];
    }

    public TreePath getOldLeadSelectionPath() {
        return this.oldLeadSelectionPath;
    }

    public TreePath getNewLeadSelectionPath() {
        return this.newLeadSelectionPath;
    }

    public Object cloneWithSource(Object obj) {
        return new TreeSelectionEvent(obj, this.paths, this.areNew, this.oldLeadSelectionPath, this.newLeadSelectionPath);
    }
}
