package javax.swing.event;

import java.util.EventObject;
import javax.swing.tree.TreePath;

/* loaded from: rt.jar:javax/swing/event/TreeModelEvent.class */
public class TreeModelEvent extends EventObject {
    protected TreePath path;
    protected int[] childIndices;
    protected Object[] children;

    public TreeModelEvent(Object obj, Object[] objArr, int[] iArr, Object[] objArr2) {
        this(obj, objArr == null ? null : new TreePath(objArr), iArr, objArr2);
    }

    public TreeModelEvent(Object obj, TreePath treePath, int[] iArr, Object[] objArr) {
        super(obj);
        this.path = treePath;
        this.childIndices = iArr;
        this.children = objArr;
    }

    public TreeModelEvent(Object obj, Object[] objArr) {
        this(obj, objArr == null ? null : new TreePath(objArr));
    }

    public TreeModelEvent(Object obj, TreePath treePath) {
        super(obj);
        this.path = treePath;
        this.childIndices = new int[0];
    }

    public TreePath getTreePath() {
        return this.path;
    }

    public Object[] getPath() {
        if (this.path != null) {
            return this.path.getPath();
        }
        return null;
    }

    public Object[] getChildren() {
        if (this.children != null) {
            int length = this.children.length;
            Object[] objArr = new Object[length];
            System.arraycopy(this.children, 0, objArr, 0, length);
            return objArr;
        }
        return null;
    }

    public int[] getChildIndices() {
        if (this.childIndices != null) {
            int length = this.childIndices.length;
            int[] iArr = new int[length];
            System.arraycopy(this.childIndices, 0, iArr, 0, length);
            return iArr;
        }
        return null;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getName() + " " + Integer.toString(hashCode()));
        if (this.path != null) {
            stringBuffer.append(" path " + ((Object) this.path));
        }
        if (this.childIndices != null) {
            stringBuffer.append(" indices [ ");
            for (int i2 = 0; i2 < this.childIndices.length; i2++) {
                stringBuffer.append(Integer.toString(this.childIndices[i2]) + " ");
            }
            stringBuffer.append("]");
        }
        if (this.children != null) {
            stringBuffer.append(" children [ ");
            for (int i3 = 0; i3 < this.children.length; i3++) {
                stringBuffer.append(this.children[i3] + " ");
            }
            stringBuffer.append("]");
        }
        return stringBuffer.toString();
    }
}
