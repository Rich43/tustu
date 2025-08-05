package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.NodeBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ShapeBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/ShapeBuilder.class */
public abstract class ShapeBuilder<B extends ShapeBuilder<B>> extends NodeBuilder<B> {
    private int __set;
    private Paint fill;
    private boolean smooth;
    private Paint stroke;
    private Collection<? extends Double> strokeDashArray;
    private double strokeDashOffset;
    private StrokeLineCap strokeLineCap;
    private StrokeLineJoin strokeLineJoin;
    private double strokeMiterLimit;
    private StrokeType strokeType;
    private double strokeWidth;

    protected ShapeBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Shape x2) {
        super.applyTo((Node) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setFill(this.fill);
                    break;
                case 1:
                    x2.setSmooth(this.smooth);
                    break;
                case 2:
                    x2.setStroke(this.stroke);
                    break;
                case 3:
                    x2.getStrokeDashArray().addAll(this.strokeDashArray);
                    break;
                case 4:
                    x2.setStrokeDashOffset(this.strokeDashOffset);
                    break;
                case 5:
                    x2.setStrokeLineCap(this.strokeLineCap);
                    break;
                case 6:
                    x2.setStrokeLineJoin(this.strokeLineJoin);
                    break;
                case 7:
                    x2.setStrokeMiterLimit(this.strokeMiterLimit);
                    break;
                case 8:
                    x2.setStrokeType(this.strokeType);
                    break;
                case 9:
                    x2.setStrokeWidth(this.strokeWidth);
                    break;
            }
        }
    }

    public B fill(Paint x2) {
        this.fill = x2;
        __set(0);
        return this;
    }

    public B smooth(boolean x2) {
        this.smooth = x2;
        __set(1);
        return this;
    }

    public B stroke(Paint x2) {
        this.stroke = x2;
        __set(2);
        return this;
    }

    public B strokeDashArray(Collection<? extends Double> x2) {
        this.strokeDashArray = x2;
        __set(3);
        return this;
    }

    public B strokeDashArray(Double... dArr) {
        return (B) strokeDashArray(Arrays.asList(dArr));
    }

    public B strokeDashOffset(double x2) {
        this.strokeDashOffset = x2;
        __set(4);
        return this;
    }

    public B strokeLineCap(StrokeLineCap x2) {
        this.strokeLineCap = x2;
        __set(5);
        return this;
    }

    public B strokeLineJoin(StrokeLineJoin x2) {
        this.strokeLineJoin = x2;
        __set(6);
        return this;
    }

    public B strokeMiterLimit(double x2) {
        this.strokeMiterLimit = x2;
        __set(7);
        return this;
    }

    public B strokeType(StrokeType x2) {
        this.strokeType = x2;
        __set(8);
        return this;
    }

    public B strokeWidth(double x2) {
        this.strokeWidth = x2;
        __set(9);
        return this;
    }
}
