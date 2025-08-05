package javafx.scene.effect;

import javafx.scene.effect.ImageInputBuilder;
import javafx.scene.image.Image;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/effect/ImageInputBuilder.class */
public class ImageInputBuilder<B extends ImageInputBuilder<B>> implements Builder<ImageInput> {
    private int __set;
    private Image source;

    /* renamed from: x, reason: collision with root package name */
    private double f12655x;

    /* renamed from: y, reason: collision with root package name */
    private double f12656y;

    protected ImageInputBuilder() {
    }

    public static ImageInputBuilder<?> create() {
        return new ImageInputBuilder<>();
    }

    public void applyTo(ImageInput x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setSource(this.source);
        }
        if ((set & 2) != 0) {
            x2.setX(this.f12655x);
        }
        if ((set & 4) != 0) {
            x2.setY(this.f12656y);
        }
    }

    public B source(Image x2) {
        this.source = x2;
        this.__set |= 1;
        return this;
    }

    public B x(double x2) {
        this.f12655x = x2;
        this.__set |= 2;
        return this;
    }

    public B y(double x2) {
        this.f12656y = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ImageInput build2() {
        ImageInput x2 = new ImageInput();
        applyTo(x2);
        return x2;
    }
}
