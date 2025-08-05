package java.awt;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/Insets.class */
public class Insets implements Cloneable, Serializable {
    public int top;
    public int left;
    public int bottom;
    public int right;
    private static final long serialVersionUID = -2272572637695466749L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Insets(int i2, int i3, int i4, int i5) {
        this.top = i2;
        this.left = i3;
        this.bottom = i4;
        this.right = i5;
    }

    public void set(int i2, int i3, int i4, int i5) {
        this.top = i2;
        this.left = i3;
        this.bottom = i4;
        this.right = i5;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Insets) {
            Insets insets = (Insets) obj;
            return this.top == insets.top && this.left == insets.left && this.bottom == insets.bottom && this.right == insets.right;
        }
        return false;
    }

    public int hashCode() {
        int i2 = this.left + this.bottom;
        int i3 = this.right + this.top;
        int i4 = ((i2 * (i2 + 1)) / 2) + this.left;
        int i5 = ((i3 * (i3 + 1)) / 2) + this.top;
        int i6 = i4 + i5;
        return ((i6 * (i6 + 1)) / 2) + i5;
    }

    public String toString() {
        return getClass().getName() + "[top=" + this.top + ",left=" + this.left + ",bottom=" + this.bottom + ",right=" + this.right + "]";
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
