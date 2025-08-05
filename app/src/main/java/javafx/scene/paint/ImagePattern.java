package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/paint/ImagePattern.class */
public final class ImagePattern extends Paint {
    private Image image;

    /* renamed from: x, reason: collision with root package name */
    private double f12711x;

    /* renamed from: y, reason: collision with root package name */
    private double f12712y;
    private double width;
    private double height;
    private boolean proportional;
    private Object platformPaint;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ImagePattern.class.desiredAssertionStatus();
    }

    public final Image getImage() {
        return this.image;
    }

    public final double getX() {
        return this.f12711x;
    }

    public final double getY() {
        return this.f12712y;
    }

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    @Override // javafx.scene.paint.Paint
    public final boolean isOpaque() {
        return ((com.sun.prism.paint.ImagePattern) acc_getPlatformPaint()).isOpaque();
    }

    public ImagePattern(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image) {
        this.width = 1.0d;
        this.height = 1.0d;
        this.proportional = true;
        if (image == null) {
            throw new NullPointerException("Image must be non-null.");
        }
        if (image.getProgress() < 1.0d) {
            throw new IllegalArgumentException("Image not yet loaded");
        }
        this.image = image;
    }

    public ImagePattern(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg(MetadataParser.WIDTH_TAG_NAME) double width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) double height, @NamedArg("proportional") boolean proportional) {
        this.width = 1.0d;
        this.height = 1.0d;
        this.proportional = true;
        if (image == null) {
            throw new NullPointerException("Image must be non-null.");
        }
        if (image.getProgress() < 1.0d) {
            throw new IllegalArgumentException("Image not yet loaded");
        }
        this.image = image;
        this.f12711x = x2;
        this.f12712y = y2;
        this.width = width;
        this.height = height;
        this.proportional = proportional;
    }

    @Override // javafx.scene.paint.Paint
    boolean acc_isMutable() {
        return Toolkit.getImageAccessor().isAnimation(this.image);
    }

    @Override // javafx.scene.paint.Paint
    void acc_addListener(AbstractNotifyListener platformChangeListener) {
        Toolkit.getImageAccessor().getImageProperty(this.image).addListener(platformChangeListener);
    }

    @Override // javafx.scene.paint.Paint
    void acc_removeListener(AbstractNotifyListener platformChangeListener) {
        Toolkit.getImageAccessor().getImageProperty(this.image).removeListener(platformChangeListener);
    }

    @Override // javafx.scene.paint.Paint
    Object acc_getPlatformPaint() {
        if (acc_isMutable() || this.platformPaint == null) {
            this.platformPaint = Toolkit.getToolkit().getPaint(this);
            if (!$assertionsDisabled && this.platformPaint == null) {
                throw new AssertionError();
            }
        }
        return this.platformPaint;
    }
}
