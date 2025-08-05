package javafx.scene.image;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.NodeBuilder;
import javafx.scene.image.ImageViewBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/image/ImageViewBuilder.class */
public class ImageViewBuilder<B extends ImageViewBuilder<B>> extends NodeBuilder<B> implements Builder<ImageView> {
    private int __set;
    private double fitHeight;
    private double fitWidth;
    private Image image;
    private boolean preserveRatio;
    private boolean smooth;
    private Rectangle2D viewport;

    /* renamed from: x, reason: collision with root package name */
    private double f12662x;

    /* renamed from: y, reason: collision with root package name */
    private double f12663y;

    protected ImageViewBuilder() {
    }

    public static ImageViewBuilder<?> create() {
        return new ImageViewBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(ImageView x2) {
        super.applyTo((Node) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setFitHeight(this.fitHeight);
                    break;
                case 1:
                    x2.setFitWidth(this.fitWidth);
                    break;
                case 2:
                    x2.setImage(this.image);
                    break;
                case 3:
                    x2.setPreserveRatio(this.preserveRatio);
                    break;
                case 4:
                    x2.setSmooth(this.smooth);
                    break;
                case 5:
                    x2.setViewport(this.viewport);
                    break;
                case 6:
                    x2.setX(this.f12662x);
                    break;
                case 7:
                    x2.setY(this.f12663y);
                    break;
            }
        }
    }

    public B fitHeight(double x2) {
        this.fitHeight = x2;
        __set(0);
        return this;
    }

    public B fitWidth(double x2) {
        this.fitWidth = x2;
        __set(1);
        return this;
    }

    public B image(Image x2) {
        this.image = x2;
        __set(2);
        return this;
    }

    public B preserveRatio(boolean x2) {
        this.preserveRatio = x2;
        __set(3);
        return this;
    }

    public B smooth(boolean x2) {
        this.smooth = x2;
        __set(4);
        return this;
    }

    public B viewport(Rectangle2D x2) {
        this.viewport = x2;
        __set(5);
        return this;
    }

    public B x(double x2) {
        this.f12662x = x2;
        __set(6);
        return this;
    }

    public B y(double x2) {
        this.f12663y = x2;
        __set(7);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ImageView build2() {
        ImageView x2 = new ImageView();
        applyTo(x2);
        return x2;
    }
}
