package javafx.scene.layout;

import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderImage.class */
public class BorderImage {
    final Image image;
    final BorderRepeat repeatX;
    final BorderRepeat repeatY;
    final BorderWidths widths;
    final BorderWidths slices;
    final boolean filled;
    final Insets insets;
    final Insets innerEdge;
    final Insets outerEdge;
    private final int hash;

    public final Image getImage() {
        return this.image;
    }

    public final BorderRepeat getRepeatX() {
        return this.repeatX;
    }

    public final BorderRepeat getRepeatY() {
        return this.repeatY;
    }

    public final BorderWidths getWidths() {
        return this.widths;
    }

    public final BorderWidths getSlices() {
        return this.slices;
    }

    public final boolean isFilled() {
        return this.filled;
    }

    public final Insets getInsets() {
        return this.insets;
    }

    public BorderImage(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image, @NamedArg("widths") BorderWidths widths, @NamedArg("insets") Insets insets, @NamedArg("slices") BorderWidths slices, @NamedArg("filled") boolean filled, @NamedArg("repeatX") BorderRepeat repeatX, @NamedArg("repeatY") BorderRepeat repeatY) {
        if (image == null) {
            throw new NullPointerException("Image cannot be null");
        }
        this.image = image;
        this.widths = widths == null ? BorderWidths.DEFAULT : widths;
        this.insets = insets == null ? Insets.EMPTY : insets;
        this.slices = slices == null ? BorderImageSlices.DEFAULT.widths : slices;
        this.filled = filled;
        this.repeatX = repeatX == null ? BorderRepeat.STRETCH : repeatX;
        this.repeatY = repeatY == null ? this.repeatX : repeatY;
        this.outerEdge = new Insets(Math.max(0.0d, -this.insets.getTop()), Math.max(0.0d, -this.insets.getRight()), Math.max(0.0d, -this.insets.getBottom()), Math.max(0.0d, -this.insets.getLeft()));
        this.innerEdge = new Insets(this.insets.getTop() + this.widths.getTop(), this.insets.getRight() + this.widths.getRight(), this.insets.getBottom() + this.widths.getBottom(), this.insets.getLeft() + this.widths.getLeft());
        int result = this.image.hashCode();
        this.hash = (31 * ((31 * ((31 * ((31 * ((31 * result) + this.widths.hashCode())) + this.slices.hashCode())) + this.repeatX.hashCode())) + this.repeatY.hashCode())) + (this.filled ? 1 : 0);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BorderImage that = (BorderImage) o2;
        return this.hash == that.hash && this.filled == that.filled && this.image.equals(that.image) && this.repeatX == that.repeatX && this.repeatY == that.repeatY && this.slices.equals(that.slices) && this.widths.equals(that.widths);
    }

    public int hashCode() {
        return this.hash;
    }
}
