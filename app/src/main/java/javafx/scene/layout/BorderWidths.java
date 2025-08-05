package javafx.scene.layout;

import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderWidths.class */
public final class BorderWidths {
    public static final double AUTO = -1.0d;
    public static final BorderWidths DEFAULT = new BorderWidths(1.0d, 1.0d, 1.0d, 1.0d, false, false, false, false);
    public static final BorderWidths EMPTY = new BorderWidths(0.0d, 0.0d, 0.0d, 0.0d, false, false, false, false);
    public static final BorderWidths FULL = new BorderWidths(1.0d, 1.0d, 1.0d, 1.0d, true, true, true, true);
    final double top;
    final double right;
    final double bottom;
    final double left;
    final boolean topAsPercentage;
    final boolean rightAsPercentage;
    final boolean bottomAsPercentage;
    final boolean leftAsPercentage;
    private final int hash;

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

    public final boolean isTopAsPercentage() {
        return this.topAsPercentage;
    }

    public final boolean isRightAsPercentage() {
        return this.rightAsPercentage;
    }

    public final boolean isBottomAsPercentage() {
        return this.bottomAsPercentage;
    }

    public final boolean isLeftAsPercentage() {
        return this.leftAsPercentage;
    }

    public BorderWidths(@NamedArg(MetadataParser.WIDTH_TAG_NAME) double width) {
        this(width, width, width, width, false, false, false, false);
    }

    public BorderWidths(@NamedArg(JSplitPane.TOP) double top, @NamedArg(JSplitPane.RIGHT) double right, @NamedArg(JSplitPane.BOTTOM) double bottom, @NamedArg(JSplitPane.LEFT) double left) {
        this(top, right, bottom, left, false, false, false, false);
    }

    public BorderWidths(@NamedArg(JSplitPane.TOP) double top, @NamedArg(JSplitPane.RIGHT) double right, @NamedArg(JSplitPane.BOTTOM) double bottom, @NamedArg(JSplitPane.LEFT) double left, @NamedArg("topAsPercentage") boolean topAsPercentage, @NamedArg("rightAsPercentage") boolean rightAsPercentage, @NamedArg("bottomAsPercentage") boolean bottomAsPercentage, @NamedArg("leftAsPercentage") boolean leftAsPercentage) {
        if ((top != -1.0d && top < 0.0d) || ((right != -1.0d && right < 0.0d) || ((bottom != -1.0d && bottom < 0.0d) || (left != -1.0d && left < 0.0d)))) {
            throw new IllegalArgumentException("None of the widths can be < 0");
        }
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.topAsPercentage = topAsPercentage;
        this.rightAsPercentage = rightAsPercentage;
        this.bottomAsPercentage = bottomAsPercentage;
        this.leftAsPercentage = leftAsPercentage;
        long temp = this.top != 0.0d ? Double.doubleToLongBits(this.top) : 0L;
        int result = (int) (temp ^ (temp >>> 32));
        long temp2 = this.right != 0.0d ? Double.doubleToLongBits(this.right) : 0L;
        int result2 = (31 * result) + ((int) (temp2 ^ (temp2 >>> 32)));
        long temp3 = this.bottom != 0.0d ? Double.doubleToLongBits(this.bottom) : 0L;
        int result3 = (31 * result2) + ((int) (temp3 ^ (temp3 >>> 32)));
        long temp4 = this.left != 0.0d ? Double.doubleToLongBits(this.left) : 0L;
        this.hash = (31 * ((31 * ((31 * ((31 * ((31 * result3) + ((int) (temp4 ^ (temp4 >>> 32))))) + (this.topAsPercentage ? 1 : 0))) + (this.rightAsPercentage ? 1 : 0))) + (this.bottomAsPercentage ? 1 : 0))) + (this.leftAsPercentage ? 1 : 0);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BorderWidths that = (BorderWidths) o2;
        return this.hash == that.hash && Double.compare(that.bottom, this.bottom) == 0 && this.bottomAsPercentage == that.bottomAsPercentage && Double.compare(that.left, this.left) == 0 && this.leftAsPercentage == that.leftAsPercentage && Double.compare(that.right, this.right) == 0 && this.rightAsPercentage == that.rightAsPercentage && Double.compare(that.top, this.top) == 0 && this.topAsPercentage == that.topAsPercentage;
    }

    public int hashCode() {
        return this.hash;
    }
}
