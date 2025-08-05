package javafx.scene.transform;

import javafx.scene.transform.TranslateBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/transform/TranslateBuilder.class */
public class TranslateBuilder<B extends TranslateBuilder<B>> implements Builder<Translate> {
    private int __set;

    /* renamed from: x, reason: collision with root package name */
    private double f12760x;

    /* renamed from: y, reason: collision with root package name */
    private double f12761y;

    /* renamed from: z, reason: collision with root package name */
    private double f12762z;

    protected TranslateBuilder() {
    }

    public static TranslateBuilder<?> create() {
        return new TranslateBuilder<>();
    }

    public void applyTo(Translate x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setX(this.f12760x);
        }
        if ((set & 2) != 0) {
            x2.setY(this.f12761y);
        }
        if ((set & 4) != 0) {
            x2.setZ(this.f12762z);
        }
    }

    public B x(double x2) {
        this.f12760x = x2;
        this.__set |= 1;
        return this;
    }

    public B y(double x2) {
        this.f12761y = x2;
        this.__set |= 2;
        return this;
    }

    public B z(double x2) {
        this.f12762z = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Translate build() {
        Translate x2 = new Translate();
        applyTo(x2);
        return x2;
    }
}
