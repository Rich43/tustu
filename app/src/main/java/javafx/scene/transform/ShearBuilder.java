package javafx.scene.transform;

import javafx.scene.transform.ShearBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/transform/ShearBuilder.class */
public class ShearBuilder<B extends ShearBuilder<B>> implements Builder<Shear> {
    private int __set;
    private double pivotX;
    private double pivotY;

    /* renamed from: x, reason: collision with root package name */
    private double f12755x;

    /* renamed from: y, reason: collision with root package name */
    private double f12756y;

    protected ShearBuilder() {
    }

    public static ShearBuilder<?> create() {
        return new ShearBuilder<>();
    }

    public void applyTo(Shear x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setPivotX(this.pivotX);
        }
        if ((set & 2) != 0) {
            x2.setPivotY(this.pivotY);
        }
        if ((set & 4) != 0) {
            x2.setX(this.f12755x);
        }
        if ((set & 8) != 0) {
            x2.setY(this.f12756y);
        }
    }

    public B pivotX(double x2) {
        this.pivotX = x2;
        this.__set |= 1;
        return this;
    }

    public B pivotY(double x2) {
        this.pivotY = x2;
        this.__set |= 2;
        return this;
    }

    public B x(double x2) {
        this.f12755x = x2;
        this.__set |= 4;
        return this;
    }

    public B y(double x2) {
        this.f12756y = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Shear build() {
        Shear x2 = new Shear();
        applyTo(x2);
        return x2;
    }
}
