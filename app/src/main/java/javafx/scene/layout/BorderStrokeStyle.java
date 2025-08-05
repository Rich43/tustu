package javafx.scene.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderStrokeStyle.class */
public final class BorderStrokeStyle {
    private static final List<Double> DOTTED_LIST = Collections.unmodifiableList(asList(0.0d, 2.0d));
    private static final List<Double> DASHED_LIST = Collections.unmodifiableList(asList(2.0d, 1.4d));
    public static final BorderStrokeStyle NONE = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 0.0d, 0.0d, null);
    public static final BorderStrokeStyle DOTTED = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.ROUND, 10.0d, 0.0d, DOTTED_LIST);
    public static final BorderStrokeStyle DASHED = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10.0d, 0.0d, DASHED_LIST);
    public static final BorderStrokeStyle SOLID = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10.0d, 0.0d, null);
    private final StrokeType type;
    private final StrokeLineJoin lineJoin;
    private final StrokeLineCap lineCap;
    private final double miterLimit;
    private final double dashOffset;
    private final List<Double> dashArray;
    private final int hash;

    public final StrokeType getType() {
        return this.type;
    }

    public final StrokeLineJoin getLineJoin() {
        return this.lineJoin;
    }

    public final StrokeLineCap getLineCap() {
        return this.lineCap;
    }

    public final double getMiterLimit() {
        return this.miterLimit;
    }

    public final double getDashOffset() {
        return this.dashOffset;
    }

    public final List<Double> getDashArray() {
        return this.dashArray;
    }

    public BorderStrokeStyle(@NamedArg("type") StrokeType type, @NamedArg("lineJoin") StrokeLineJoin lineJoin, @NamedArg("lineCap") StrokeLineCap lineCap, @NamedArg("miterLimit") double miterLimit, @NamedArg("dashOffset") double dashOffset, @NamedArg("dashArray") List<Double> dashArray) {
        this.type = type != null ? type : StrokeType.CENTERED;
        this.lineJoin = lineJoin != null ? lineJoin : StrokeLineJoin.MITER;
        this.lineCap = lineCap != null ? lineCap : StrokeLineCap.BUTT;
        this.miterLimit = miterLimit;
        this.dashOffset = dashOffset;
        if (dashArray == null) {
            this.dashArray = Collections.emptyList();
        } else if (dashArray == DASHED_LIST || dashArray == DOTTED_LIST) {
            this.dashArray = dashArray;
        } else {
            List<Double> list = new ArrayList<>(dashArray);
            this.dashArray = Collections.unmodifiableList(list);
        }
        int result = this.type.hashCode();
        int result2 = (31 * ((31 * result) + this.lineJoin.hashCode())) + this.lineCap.hashCode();
        long temp = this.miterLimit != 0.0d ? Double.doubleToLongBits(this.miterLimit) : 0L;
        int result3 = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        long temp2 = this.dashOffset != 0.0d ? Double.doubleToLongBits(this.dashOffset) : 0L;
        this.hash = (31 * ((31 * result3) + ((int) (temp2 ^ (temp2 >>> 32))))) + this.dashArray.hashCode();
    }

    public String toString() {
        if (this == NONE) {
            return "BorderStyle.NONE";
        }
        if (this == DASHED) {
            return "BorderStyle.DASHED";
        }
        if (this == DOTTED) {
            return "BorderStyle.DOTTED";
        }
        if (this == SOLID) {
            return "BorderStyle.SOLID";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("BorderStyle: ");
        buffer.append((Object) this.type);
        buffer.append(", ");
        buffer.append((Object) this.lineJoin);
        buffer.append(", ");
        buffer.append((Object) this.lineCap);
        buffer.append(", ");
        buffer.append(this.miterLimit);
        buffer.append(", ");
        buffer.append(this.dashOffset);
        buffer.append(", [");
        if (this.dashArray != null) {
            buffer.append((Object) this.dashArray);
        }
        buffer.append("]");
        return buffer.toString();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (this == NONE && o2 != NONE) {
            return false;
        }
        if ((o2 == NONE && this != NONE) || o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BorderStrokeStyle that = (BorderStrokeStyle) o2;
        return this.hash == that.hash && Double.compare(that.dashOffset, this.dashOffset) == 0 && Double.compare(that.miterLimit, this.miterLimit) == 0 && this.dashArray.equals(that.dashArray) && this.lineCap == that.lineCap && this.lineJoin == that.lineJoin && this.type == that.type;
    }

    public int hashCode() {
        return this.hash;
    }

    private static List<Double> asList(double... items) {
        List<Double> list = new ArrayList<>(items.length);
        for (double d2 : items) {
            list.add(Double.valueOf(d2));
        }
        return list;
    }
}
