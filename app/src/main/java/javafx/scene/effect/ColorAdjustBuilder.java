package javafx.scene.effect;

import javafx.scene.effect.ColorAdjustBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/ColorAdjustBuilder.class */
public class ColorAdjustBuilder<B extends ColorAdjustBuilder<B>> implements Builder<ColorAdjust> {
    private int __set;
    private double brightness;
    private double contrast;
    private double hue;
    private Effect input;
    private double saturation;

    protected ColorAdjustBuilder() {
    }

    public static ColorAdjustBuilder<?> create() {
        return new ColorAdjustBuilder<>();
    }

    public void applyTo(ColorAdjust x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBrightness(this.brightness);
        }
        if ((set & 2) != 0) {
            x2.setContrast(this.contrast);
        }
        if ((set & 4) != 0) {
            x2.setHue(this.hue);
        }
        if ((set & 8) != 0) {
            x2.setInput(this.input);
        }
        if ((set & 16) != 0) {
            x2.setSaturation(this.saturation);
        }
    }

    public B brightness(double x2) {
        this.brightness = x2;
        this.__set |= 1;
        return this;
    }

    public B contrast(double x2) {
        this.contrast = x2;
        this.__set |= 2;
        return this;
    }

    public B hue(double x2) {
        this.hue = x2;
        this.__set |= 4;
        return this;
    }

    public B input(Effect x2) {
        this.input = x2;
        this.__set |= 8;
        return this;
    }

    public B saturation(double x2) {
        this.saturation = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public ColorAdjust build() {
        ColorAdjust x2 = new ColorAdjust();
        applyTo(x2);
        return x2;
    }
}
