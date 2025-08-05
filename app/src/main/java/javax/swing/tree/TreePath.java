package javax.swing.tree;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/tree/TreePath.class */
public class TreePath implements Serializable {
    private TreePath parentPath;
    private Object lastPathComponent;

    @ConstructorProperties({"path"})
    public TreePath(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException("path in TreePath must be non null and not empty.");
        }
        this.lastPathComponent = objArr[objArr.length - 1];
        if (this.lastPathComponent == null) {
            throw new IllegalArgumentException("Last path component must be non-null");
        }
        if (objArr.length > 1) {
            this.parentPath = new TreePath(objArr, objArr.length - 1);
        }
    }

    public TreePath(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("path in TreePath must be non null.");
        }
        this.lastPathComponent = obj;
        this.parentPath = null;
    }

    protected TreePath(TreePath treePath, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("path in TreePath must be non null.");
        }
        this.parentPath = treePath;
        this.lastPathComponent = obj;
    }

    protected TreePath(Object[] objArr, int i2) {
        this.lastPathComponent = objArr[i2 - 1];
        if (this.lastPathComponent == null) {
            throw new IllegalArgumentException("Path elements must be non-null");
        }
        if (i2 > 1) {
            this.parentPath = new TreePath(objArr, i2 - 1);
        }
    }

    protected TreePath() {
    }

    public Object[] getPath() {
        int pathCount = getPathCount();
        int i2 = pathCount - 1;
        Object[] objArr = new Object[pathCount];
        TreePath parentPath = this;
        while (true) {
            TreePath treePath = parentPath;
            if (treePath != null) {
                int i3 = i2;
                i2--;
                objArr[i3] = treePath.getLastPathComponent();
                parentPath = treePath.getParentPath();
            } else {
                return objArr;
            }
        }
    }

    public Object getLastPathComponent() {
        return this.lastPathComponent;
    }

    public int getPathCount() {
        int i2 = 0;
        TreePath parentPath = this;
        while (true) {
            TreePath treePath = parentPath;
            if (treePath != null) {
                i2++;
                parentPath = treePath.getParentPath();
            } else {
                return i2;
            }
        }
    }

    public Object getPathComponent(int i2) {
        int pathCount = getPathCount();
        if (i2 < 0 || i2 >= pathCount) {
            throw new IllegalArgumentException("Index " + i2 + " is out of the specified range");
        }
        TreePath parentPath = this;
        for (int i3 = pathCount - 1; i3 != i2; i3--) {
            parentPath = parentPath.getParentPath();
        }
        return parentPath.getLastPathComponent();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TreePath) {
            TreePath parentPath = (TreePath) obj;
            if (getPathCount() != parentPath.getPathCount()) {
                return false;
            }
            TreePath parentPath2 = this;
            while (true) {
                TreePath treePath = parentPath2;
                if (treePath != null) {
                    if (!treePath.getLastPathComponent().equals(parentPath.getLastPathComponent())) {
                        return false;
                    }
                    parentPath = parentPath.getParentPath();
                    parentPath2 = treePath.getParentPath();
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return getLastPathComponent().hashCode();
    }

    public boolean isDescendant(TreePath treePath) {
        if (treePath == this) {
            return true;
        }
        if (treePath != null) {
            int pathCount = getPathCount();
            int pathCount2 = treePath.getPathCount();
            if (pathCount2 < pathCount) {
                return false;
            }
            while (true) {
                int i2 = pathCount2;
                pathCount2--;
                if (i2 > pathCount) {
                    treePath = treePath.getParentPath();
                } else {
                    return equals(treePath);
                }
            }
        } else {
            return false;
        }
    }

    public TreePath pathByAddingChild(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Null child not allowed");
        }
        return new TreePath(this, obj);
    }

    public TreePath getParentPath() {
        return this.parentPath;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("[");
        int pathCount = getPathCount();
        for (int i2 = 0; i2 < pathCount; i2++) {
            if (i2 > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(getPathComponent(i2));
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
