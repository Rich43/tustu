package javafx.scene.transform;

import javafx.geometry.Point3D;
import javafx.scene.transform.RotateBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/transform/RotateBuilder.class */
public class RotateBuilder<B extends RotateBuilder<B>> implements Builder<Rotate> {
    private int __set;
    private double angle;
    private Point3D axis;
    private double pivotX;
    private double pivotY;
    private double pivotZ;

    protected RotateBuilder() {
    }

    public static RotateBuilder<?> create() {
        return new RotateBuilder<>();
    }

    public void applyTo(Rotate x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAngle(this.angle);
        }
        if ((set & 2) != 0) {
            x2.setAxis(this.axis);
        }
        if ((set & 4) != 0) {
            x2.setPivotX(this.pivotX);
        }
        if ((set & 8) != 0) {
            x2.setPivotY(this.pivotY);
        }
        if ((set & 16) != 0) {
            x2.setPivotZ(this.pivotZ);
        }
    }

    public B angle(double x2) {
        this.angle = x2;
        this.__set |= 1;
        return this;
    }

    public B axis(Point3D x2) {
        this.axis = x2;
        this.__set |= 2;
        return this;
    }

    public B pivotX(double x2) {
        this.pivotX = x2;
        this.__set |= 4;
        return this;
    }

    public B pivotY(double x2) {
        this.pivotY = x2;
        this.__set |= 8;
        return this;
    }

    public B pivotZ(double x2) {
        this.pivotZ = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Rotate build() {
        Rotate x2 = new Rotate();
        applyTo(x2);
        return x2;
    }
}
