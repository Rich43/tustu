package javafx.scene.paint;

import javafx.scene.image.Image;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/paint/ImagePatternBuilder.class */
public final class ImagePatternBuilder implements Builder<ImagePattern> {
    private double height;
    private Image image;
    private boolean proportional;
    private double width;

    /* renamed from: x, reason: collision with root package name */
    private double f12713x;

    /* renamed from: y, reason: collision with root package name */
    private double f12714y;

    protected ImagePatternBuilder() {
    }

    public static ImagePatternBuilder create() {
        return new ImagePatternBuilder();
    }

    public ImagePatternBuilder height(double x2) {
        this.height = x2;
        return this;
    }

    public ImagePatternBuilder image(Image x2) {
        this.image = x2;
        return this;
    }

    public ImagePatternBuilder proportional(boolean x2) {
        this.proportional = x2;
        return this;
    }

    public ImagePatternBuilder width(double x2) {
        this.width = x2;
        return this;
    }

    public ImagePatternBuilder x(double x2) {
        this.f12713x = x2;
        return this;
    }

    public ImagePatternBuilder y(double x2) {
        this.f12714y = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ImagePattern build2() {
        ImagePattern x2 = new ImagePattern(this.image, this.f12713x, this.f12714y, this.width, this.height, this.proportional);
        return x2;
    }
}
