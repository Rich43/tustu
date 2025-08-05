package javafx.scene.paint;

import javafx.scene.paint.ColorBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/paint/ColorBuilder.class */
public class ColorBuilder<B extends ColorBuilder<B>> implements Builder<Color> {
    private double blue;
    private double green;
    private double opacity = 1.0d;
    private double red;

    protected ColorBuilder() {
    }

    public static ColorBuilder<?> create() {
        return new ColorBuilder<>();
    }

    public B blue(double x2) {
        this.blue = x2;
        return this;
    }

    public B green(double x2) {
        this.green = x2;
        return this;
    }

    public B opacity(double x2) {
        this.opacity = x2;
        return this;
    }

    public B red(double x2) {
        this.red = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Color build() {
        Color x2 = new Color(this.red, this.green, this.blue, this.opacity);
        return x2;
    }
}
