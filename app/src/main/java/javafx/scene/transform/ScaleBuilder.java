package javafx.scene.transform;

import javafx.scene.transform.ScaleBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/transform/ScaleBuilder.class */
public class ScaleBuilder<B extends ScaleBuilder<B>> implements Builder<Scale> {
    private int __set;
    private double pivotX;
    private double pivotY;
    private double pivotZ;

    /* renamed from: x, reason: collision with root package name */
    private double f12750x;

    /* renamed from: y, reason: collision with root package name */
    private double f12751y;

    /* renamed from: z, reason: collision with root package name */
    private double f12752z;

    protected ScaleBuilder() {
    }

    public static ScaleBuilder<?> create() {
        return new ScaleBuilder<>();
    }

    public void applyTo(Scale x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setPivotX(this.pivotX);
        }
        if ((set & 2) != 0) {
            x2.setPivotY(this.pivotY);
        }
        if ((set & 4) != 0) {
            x2.setPivotZ(this.pivotZ);
        }
        if ((set & 8) != 0) {
            x2.setX(this.f12750x);
        }
        if ((set & 16) != 0) {
            x2.setY(this.f12751y);
        }
        if ((set & 32) != 0) {
            x2.setZ(this.f12752z);
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

    public B pivotZ(double x2) {
        this.pivotZ = x2;
        this.__set |= 4;
        return this;
    }

    public B x(double x2) {
        this.f12750x = x2;
        this.__set |= 8;
        return this;
    }

    public B y(double x2) {
        this.f12751y = x2;
        this.__set |= 16;
        return this;
    }

    public B z(double x2) {
        this.f12752z = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Scale build() {
        Scale x2 = new Scale();
        applyTo(x2);
        return x2;
    }
}
