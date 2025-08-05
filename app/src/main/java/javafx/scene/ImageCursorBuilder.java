package javafx.scene;

import javafx.scene.ImageCursorBuilder;
import javafx.scene.image.Image;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/ImageCursorBuilder.class */
public class ImageCursorBuilder<B extends ImageCursorBuilder<B>> implements Builder<ImageCursor> {
    private double hotspotX;
    private double hotspotY;
    private Image image;

    protected ImageCursorBuilder() {
    }

    public static ImageCursorBuilder<?> create() {
        return new ImageCursorBuilder<>();
    }

    public B hotspotX(double x2) {
        this.hotspotX = x2;
        return this;
    }

    public B hotspotY(double x2) {
        this.hotspotY = x2;
        return this;
    }

    public B image(Image x2) {
        this.image = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ImageCursor build2() {
        ImageCursor x2 = new ImageCursor(this.image, this.hotspotX, this.hotspotY);
        return x2;
    }
}
