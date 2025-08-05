package javax.swing.tree;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/* loaded from: rt.jar:javax/swing/tree/DefaultTreeModel.class */
public class DefaultTreeModel implements Serializable, TreeModel {
    protected TreeNode root;
    protected EventListenerList listenerList;
    protected boolean asksAllowsChildren;

    @ConstructorProperties({"root"})
    public DefaultTreeModel(TreeNode treeNode) {
        this(treeNode, false);
    }

    public DefaultTreeModel(TreeNode treeNode, boolean z2) {
        this.listenerList = new EventListenerList();
        this.root = treeNode;
        this.asksAllowsChildren = z2;
    }

    public void setAsksAllowsChildren(boolean z2) {
        this.asksAllowsChildren = z2;
    }

    public boolean asksAllowsChildren() {
        return this.asksAllowsChildren;
    }

    public void setRoot(TreeNode treeNode) {
        TreeNode treeNode2 = this.root;
        this.root = treeNode;
        if (treeNode == null && treeNode2 != null) {
            fireTreeStructureChanged(this, null);
        } else {
            nodeStructureChanged(treeNode);
        }
    }

    @Override // javax.swing.tree.TreeModel
    public Object getRoot() {
        return this.root;
    }

    @Override // javax.swing.tree.TreeModel
    public int getIndexOfChild(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            return -1;
        }
        return ((TreeNode) obj).getIndex((TreeNode) obj2);
    }

    @Override // javax.swing.tree.TreeModel
    public Object getChild(Object obj, int i2) {
        return ((TreeNode) obj).getChildAt(i2);
    }

    @Override // javax.swing.tree.TreeModel
    public int getChildCount(Object obj) {
        return ((TreeNode) obj).getChildCount();
    }

    @Override // javax.swing.tree.TreeModel
    public boolean isLeaf(Object obj) {
        if (this.asksAllowsChildren) {
            return !((TreeNode) obj).getAllowsChildren();
        }
        return ((TreeNode) obj).isLeaf();
    }

    public void reload() {
        reload(this.root);
    }

    @Override // javax.swing.tree.TreeModel
    public void valueForPathChanged(TreePath treePath, Object obj) {
        MutableTreeNode mutableTreeNode = (MutableTreeNode) treePath.getLastPathComponent();
        mutableTreeNode.setUserObject(obj);
        nodeChanged(mutableTreeNode);
    }

    public void insertNodeInto(MutableTreeNode mutableTreeNode, MutableTreeNode mutableTreeNode2, int i2) {
        mutableTreeNode2.insert(mutableTreeNode, i2);
        nodesWereInserted(mutableTreeNode2, new int[]{i2});
    }

    public void removeNodeFromParent(MutableTreeNode mutableTreeNode) {
        MutableTreeNode mutableTreeNode2 = (MutableTreeNode) mutableTreeNode.getParent();
        if (mutableTreeNode2 == null) {
            throw new IllegalArgumentException("node does not have a parent.");
        }
        int[] iArr = {mutableTreeNode2.getIndex(mutableTreeNode)};
        mutableTreeNode2.remove(iArr[0]);
        nodesWereRemoved(mutableTreeNode2, iArr, new Object[]{mutableTreeNode});
    }

    public void nodeChanged(TreeNode treeNode) {
        if (this.listenerList != null && treeNode != null) {
            TreeNode parent = treeNode.getParent();
            if (parent != null) {
                int index = parent.getIndex(treeNode);
                if (index != -1) {
                    nodesChanged(parent, new int[]{index});
                    return;
                }
                return;
            }
            if (treeNode == getRoot()) {
                nodesChanged(treeNode, null);
            }
        }
    }

    public void reload(TreeNode treeNode) {
        if (treeNode != null) {
            fireTreeStructureChanged(this, getPathToRoot(treeNode), null, null);
        }
    }

    public void nodesWereInserted(TreeNode treeNode, int[] iArr) {
        if (this.listenerList != null && treeNode != null && iArr != null && iArr.length > 0) {
            int length = iArr.length;
            Object[] objArr = new Object[length];
            for (int i2 = 0; i2 < length; i2++) {
                objArr[i2] = treeNode.getChildAt(iArr[i2]);
            }
            fireTreeNodesInserted(this, getPathToRoot(treeNode), iArr, objArr);
        }
    }

    public void nodesWereRemoved(TreeNode treeNode, int[] iArr, Object[] objArr) {
        if (treeNode != null && iArr != null) {
            fireTreeNodesRemoved(this, getPathToRoot(treeNode), iArr, objArr);
        }
    }

    public void nodesChanged(TreeNode treeNode, int[] iArr) {
        if (treeNode != null) {
            if (iArr != null) {
                int length = iArr.length;
                if (length > 0) {
                    Object[] objArr = new Object[length];
                    for (int i2 = 0; i2 < length; i2++) {
                        objArr[i2] = treeNode.getChildAt(iArr[i2]);
                    }
                    fireTreeNodesChanged(this, getPathToRoot(treeNode), iArr, objArr);
                    return;
                }
                return;
            }
            if (treeNode == getRoot()) {
                fireTreeNodesChanged(this, getPathToRoot(treeNode), null, null);
            }
        }
    }

    public void nodeStructureChanged(TreeNode treeNode) {
        if (treeNode != null) {
            fireTreeStructureChanged(this, getPathToRoot(treeNode), null, null);
        }
    }

    public TreeNode[] getPathToRoot(TreeNode treeNode) {
        return getPathToRoot(treeNode, 0);
    }

    protected TreeNode[] getPathToRoot(TreeNode treeNode, int i2) {
        TreeNode[] pathToRoot;
        if (treeNode == null) {
            if (i2 == 0) {
                return null;
            }
            pathToRoot = new TreeNode[i2];
        } else {
            int i3 = i2 + 1;
            if (treeNode == this.root) {
                pathToRoot = new TreeNode[i3];
            } else {
                pathToRoot = getPathToRoot(treeNode.getParent(), i3);
            }
            pathToRoot[pathToRoot.length - i3] = treeNode;
        }
        return pathToRoot;
    }

    @Override // javax.swing.tree.TreeModel
    public void addTreeModelListener(TreeModelListener treeModelListener) {
        this.listenerList.add(TreeModelListener.class, treeModelListener);
    }

    @Override // javax.swing.tree.TreeModel
    public void removeTreeModelListener(TreeModelListener treeModelListener) {
        this.listenerList.remove(TreeModelListener.class, treeModelListener);
    }

    public TreeModelListener[] getTreeModelListeners() {
        return (TreeModelListener[]) this.listenerList.getListeners(TreeModelListener.class);
    }

    protected void fireTreeNodesChanged(Object obj, Object[] objArr, int[] iArr, Object[] objArr2) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeModelListener.class) {
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(obj, objArr, iArr, objArr2);
                }
                ((TreeModelListener) listenerList[length + 1]).treeNodesChanged(treeModelEvent);
            }
        }
    }

    protected void fireTreeNodesInserted(Object obj, Object[] objArr, int[] iArr, Object[] objArr2) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeModelListener.class) {
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(obj, objArr, iArr, objArr2);
                }
                ((TreeModelListener) listenerList[length + 1]).treeNodesInserted(treeModelEvent);
            }
        }
    }

    protected void fireTreeNodesRemoved(Object obj, Object[] objArr, int[] iArr, Object[] objArr2) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeModelListener.class) {
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(obj, objArr, iArr, objArr2);
                }
                ((TreeModelListener) listenerList[length + 1]).treeNodesRemoved(treeModelEvent);
            }
        }
    }

    protected void fireTreeStructureChanged(Object obj, Object[] objArr, int[] iArr, Object[] objArr2) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeModelListener.class) {
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(obj, objArr, iArr, objArr2);
                }
                ((TreeModelListener) listenerList[length + 1]).treeStructureChanged(treeModelEvent);
            }
        }
    }

    private void fireTreeStructureChanged(Object obj, TreePath treePath) {
        Object[] listenerList = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == TreeModelListener.class) {
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(obj, treePath);
                }
                ((TreeModelListener) listenerList[length + 1]).treeStructureChanged(treeModelEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector();
        objectOutputStream.defaultWriteObject();
        if (this.root != null && (this.root instanceof Serializable)) {
            vector.addElement("root");
            vector.addElement(this.root);
        }
        objectOutputStream.writeObject(vector);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Vector vector = (Vector) objectInputStream.readObject();
        if (0 < vector.size() && vector.elementAt(0).equals("root")) {
            int i2 = 0 + 1;
            this.root = (TreeNode) vector.elementAt(i2);
            int i3 = i2 + 1;
        }
    }
}
