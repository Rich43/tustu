package javafx.geometry;

import javafx.beans.NamedArg;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:javafx/geometry/Insets.class */
public class Insets {
    public static final Insets EMPTY = new Insets(0.0d, 0.0d, 0.0d, 0.0d);
    private double top;
    private double right;
    private double bottom;
    private double left;
    private int hash = 0;

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public Insets(@NamedArg(JSplitPane.TOP) double top, @NamedArg(JSplitPane.RIGHT) double right, @NamedArg(JSplitPane.BOTTOM) double bottom, @NamedArg(JSplitPane.LEFT) double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Insets(@NamedArg("topRightBottomLeft") double topRightBottomLeft) {
        this.top = topRightBottomLeft;
        this.right = topRightBottomLeft;
        this.bottom = topRightBottomLeft;
        this.left = topRightBottomLeft;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Insets) {
            Insets other = (Insets) obj;
            return this.top == other.top && this.right == other.right && this.bottom == other.bottom && this.left == other.left;
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (37 * 17) + Double.doubleToLongBits(this.top);
            long bits2 = (37 * ((37 * ((37 * bits) + Double.doubleToLongBits(this.right))) + Double.doubleToLongBits(this.bottom))) + Double.doubleToLongBits(this.left);
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Insets [top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ", left=" + this.left + "]";
    }
}
