package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ColorPickerBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ColorPickerBuilder.class */
public class ColorPickerBuilder<B extends ColorPickerBuilder<B>> extends ComboBoxBaseBuilder<Color, B> implements Builder<ColorPicker> {
    private boolean __set;
    private Collection<? extends Color> customColors;

    protected ColorPickerBuilder() {
    }

    public static ColorPickerBuilder<?> create() {
        return new ColorPickerBuilder<>();
    }

    public void applyTo(ColorPicker x2) {
        super.applyTo((ComboBoxBase) x2);
        if (this.__set) {
            x2.getCustomColors().addAll(this.customColors);
        }
    }

    public B customColors(Collection<? extends Color> x2) {
        this.customColors = x2;
        this.__set = true;
        return this;
    }

    public B customColors(Color... colorArr) {
        return (B) customColors(Arrays.asList(colorArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ColorPicker build2() {
        ColorPicker x2 = new ColorPicker();
        applyTo(x2);
        return x2;
    }
}
