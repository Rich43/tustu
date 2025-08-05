package javafx.scene.effect;

import javafx.scene.effect.LightingBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/LightingBuilder.class */
public class LightingBuilder<B extends LightingBuilder<B>> implements Builder<Lighting> {
    private int __set;
    private Effect bumpInput;
    private Effect contentInput;
    private double diffuseConstant;
    private Light light;
    private double specularConstant;
    private double specularExponent;
    private double surfaceScale;

    protected LightingBuilder() {
    }

    public static LightingBuilder<?> create() {
        return new LightingBuilder<>();
    }

    public void applyTo(Lighting x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBumpInput(this.bumpInput);
        }
        if ((set & 2) != 0) {
            x2.setContentInput(this.contentInput);
        }
        if ((set & 4) != 0) {
            x2.setDiffuseConstant(this.diffuseConstant);
        }
        if ((set & 8) != 0) {
            x2.setLight(this.light);
        }
        if ((set & 16) != 0) {
            x2.setSpecularConstant(this.specularConstant);
        }
        if ((set & 32) != 0) {
            x2.setSpecularExponent(this.specularExponent);
        }
        if ((set & 64) != 0) {
            x2.setSurfaceScale(this.surfaceScale);
        }
    }

    public B bumpInput(Effect x2) {
        this.bumpInput = x2;
        this.__set |= 1;
        return this;
    }

    public B contentInput(Effect x2) {
        this.contentInput = x2;
        this.__set |= 2;
        return this;
    }

    public B diffuseConstant(double x2) {
        this.diffuseConstant = x2;
        this.__set |= 4;
        return this;
    }

    public B light(Light x2) {
        this.light = x2;
        this.__set |= 8;
        return this;
    }

    public B specularConstant(double x2) {
        this.specularConstant = x2;
        this.__set |= 16;
        return this;
    }

    public B specularExponent(double x2) {
        this.specularExponent = x2;
        this.__set |= 32;
        return this;
    }

    public B surfaceScale(double x2) {
        this.surfaceScale = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Lighting build2() {
        Lighting x2 = new Lighting();
        applyTo(x2);
        return x2;
    }
}
