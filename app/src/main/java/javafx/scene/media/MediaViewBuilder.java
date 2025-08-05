package javafx.scene.media;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.NodeBuilder;
import javafx.scene.media.MediaViewBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/media/MediaViewBuilder.class */
public class MediaViewBuilder<B extends MediaViewBuilder<B>> extends NodeBuilder<B> implements Builder<MediaView> {
    private int __set;
    private double fitHeight;
    private double fitWidth;
    private MediaPlayer mediaPlayer;
    private EventHandler<MediaErrorEvent> onError;
    private boolean preserveRatio;
    private boolean smooth;
    private Rectangle2D viewport;

    /* renamed from: x, reason: collision with root package name */
    private double f12709x;

    /* renamed from: y, reason: collision with root package name */
    private double f12710y;

    protected MediaViewBuilder() {
    }

    public static MediaViewBuilder<?> create() {
        return new MediaViewBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(MediaView x2) {
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
                    x2.setMediaPlayer(this.mediaPlayer);
                    break;
                case 3:
                    x2.setOnError(this.onError);
                    break;
                case 4:
                    x2.setPreserveRatio(this.preserveRatio);
                    break;
                case 5:
                    x2.setSmooth(this.smooth);
                    break;
                case 6:
                    x2.setViewport(this.viewport);
                    break;
                case 7:
                    x2.setX(this.f12709x);
                    break;
                case 8:
                    x2.setY(this.f12710y);
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

    public B mediaPlayer(MediaPlayer x2) {
        this.mediaPlayer = x2;
        __set(2);
        return this;
    }

    public B onError(EventHandler<MediaErrorEvent> x2) {
        this.onError = x2;
        __set(3);
        return this;
    }

    public B preserveRatio(boolean x2) {
        this.preserveRatio = x2;
        __set(4);
        return this;
    }

    public B smooth(boolean x2) {
        this.smooth = x2;
        __set(5);
        return this;
    }

    public B viewport(Rectangle2D x2) {
        this.viewport = x2;
        __set(6);
        return this;
    }

    public B x(double x2) {
        this.f12709x = x2;
        __set(7);
        return this;
    }

    public B y(double x2) {
        this.f12710y = x2;
        __set(8);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public MediaView build2() {
        MediaView x2 = new MediaView();
        applyTo(x2);
        return x2;
    }
}
