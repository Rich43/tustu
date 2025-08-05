package javafx.scene.effect;

import javafx.scene.effect.MotionBlurBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/MotionBlurBuilder.class */
public class MotionBlurBuilder<B extends MotionBlurBuilder<B>> implements Builder<MotionBlur> {
    private int __set;
    private double angle;
    private Effect input;
    private double radius;

    protected MotionBlurBuilder() {
    }

    public static MotionBlurBuilder<?> create() {
        return new MotionBlurBuilder<>();
    }

    public void applyTo(MotionBlur x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAngle(this.angle);
        }
        if ((set & 2) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 4) != 0) {
            x2.setRadius(this.radius);
        }
    }

    public B angle(double x2) {
        this.angle = x2;
        this.__set |= 1;
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 2;
        return this;
    }

    public B radius(double x2) {
        this.radius = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public MotionBlur build() {
        MotionBlur x2 = new MotionBlur();
        applyTo(x2);
        return x2;
    }
}
