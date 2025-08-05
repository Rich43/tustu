package javafx.scene.shape;

import javafx.scene.shape.CubicCurveBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/CubicCurveBuilder.class */
public class CubicCurveBuilder<B extends CubicCurveBuilder<B>> extends ShapeBuilder<B> implements Builder<CubicCurve> {
    private int __set;
    private double controlX1;
    private double controlX2;
    private double controlY1;
    private double controlY2;
    private double endX;
    private double endY;
    private double startX;
    private double startY;

    protected CubicCurveBuilder() {
    }

    public static CubicCurveBuilder<?> create() {
        return new CubicCurveBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(CubicCurve x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setControlX1(this.controlX1);
                    break;
                case 1:
                    x2.setControlX2(this.controlX2);
                    break;
                case 2:
                    x2.setControlY1(this.controlY1);
                    break;
                case 3:
                    x2.setControlY2(this.controlY2);
                    break;
                case 4:
                    x2.setEndX(this.endX);
                    break;
                case 5:
                    x2.setEndY(this.endY);
                    break;
                case 6:
                    x2.setStartX(this.startX);
                    break;
                case 7:
                    x2.setStartY(this.startY);
                    break;
            }
        }
    }

    public B controlX1(double x2) {
        this.controlX1 = x2;
        __set(0);
        return this;
    }

    public B controlX2(double x2) {
        this.controlX2 = x2;
        __set(1);
        return this;
    }

    public B controlY1(double x2) {
        this.controlY1 = x2;
        __set(2);
        return this;
    }

    public B controlY2(double x2) {
        this.controlY2 = x2;
        __set(3);
        return this;
    }

    public B endX(double x2) {
        this.endX = x2;
        __set(4);
        return this;
    }

    public B endY(double x2) {
        this.endY = x2;
        __set(5);
        return this;
    }

    public B startX(double x2) {
        this.startX = x2;
        __set(6);
        return this;
    }

    public B startY(double x2) {
        this.startY = x2;
        __set(7);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public CubicCurve build2() {
        CubicCurve x2 = new CubicCurve();
        applyTo(x2);
        return x2;
    }
}
