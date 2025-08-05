package java.awt;

import java.awt.geom.Dimension2D;
import java.beans.Transient;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/Dimension.class */
public class Dimension extends Dimension2D implements Serializable {
    public int width;
    public int height;
    private static final long serialVersionUID = 4723952579491349524L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Dimension() {
        this(0, 0);
    }

    public Dimension(Dimension dimension) {
        this(dimension.width, dimension.height);
    }

    public Dimension(int i2, int i3) {
        this.width = i2;
        this.height = i3;
    }

    @Override // java.awt.geom.Dimension2D
    public double getWidth() {
        return this.width;
    }

    @Override // java.awt.geom.Dimension2D
    public double getHeight() {
        return this.height;
    }

    @Override // java.awt.geom.Dimension2D
    public void setSize(double d2, double d3) {
        this.width = (int) Math.ceil(d2);
        this.height = (int) Math.ceil(d3);
    }

    @Transient
    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int i2, int i3) {
        this.width = i2;
        this.height = i3;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Dimension) {
            Dimension dimension = (Dimension) obj;
            return this.width == dimension.width && this.height == dimension.height;
        }
        return false;
    }

    public int hashCode() {
        int i2 = this.width + this.height;
        return ((i2 * (i2 + 1)) / 2) + this.width;
    }

    public String toString() {
        return getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
    }
}
